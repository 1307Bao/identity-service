package com.tutorial.identity_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data //Annotation creates getter, setter, ...
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogoutRequest {
    String token;
}
