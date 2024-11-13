package com.tutorial.identity_service.enums;

/*
    1 USER -> many ROLE
    1 ROLE -> many USER
    1 ROLE -> many PERMISSION
    1 PERMISSION -> many ROLE
 */

public enum Role {
    ADMIN,
    USER
}
