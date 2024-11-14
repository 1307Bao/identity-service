package com.tutorial.identity_service.dto.request;

import com.tutorial.identity_service.exception.ErrorCode;
import com.tutorial.identity_service.validator.DoBConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data //Annotation creates getter, setter, ...
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;

    @DoBConstraint(min = 18)
    @NotNull
    LocalDate dateOfBirth;
}
