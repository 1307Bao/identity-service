package com.tutorial.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data //Annotation creates getter, setter, ...
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    boolean authenticated;
}
