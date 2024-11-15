package com.tutorial.identity_service.dto.request;

import com.tutorial.identity_service.validator.DoBConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;
    @DoBConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;
    List<String> roles;
}
