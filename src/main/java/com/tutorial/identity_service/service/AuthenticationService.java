package com.tutorial.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tutorial.identity_service.dto.request.AuthenticationRequest;
import com.tutorial.identity_service.dto.request.IntrospectRequest;
import com.tutorial.identity_service.dto.request.LogoutRequest;
import com.tutorial.identity_service.dto.response.AuthenticationResponse;
import com.tutorial.identity_service.dto.response.IntrospectResponse;
import com.tutorial.identity_service.entity.InvalidatedToken;
import com.tutorial.identity_service.entity.User;
import com.tutorial.identity_service.exception.AppRunTimeException;
import com.tutorial.identity_service.exception.ErrorCode;
import com.tutorial.identity_service.repository.InvalidatedTokenRepository;
import com.tutorial.identity_service.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppRunTimeException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppRunTimeException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .authenticated(authenticated)
                .token(token)
                .build();
    }

    String generateToken(User user) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) //Username
                .issuer("devteria.com") // Xác định token này được issuer từ ai
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can not create token");
            throw new RuntimeException(e);
        }
    }


    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        }
        catch (AppRunTimeException e){
            isValid = false;
        }

        return IntrospectResponse.builder()
            .valid(isValid)
            .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!verified && new Date().after(expiredTime))
        throw new AppRunTimeException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppRunTimeException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public void logout(LogoutRequest request)
            throws ParseException, JOSEException {

        var signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                                            .id(jit)
                                            .expiryDate(expiryTime)
                                            .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                role.getPermissions().forEach(permission -> {
                    stringJoiner.add(permission.getName());
                });
            });
        }

        return stringJoiner.toString();
    }
}
