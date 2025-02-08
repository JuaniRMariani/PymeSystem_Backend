package com.example.pymesystem_backend.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long userId;
    private String name;
    private String lastName;
    private String email;
    private String role;
}
