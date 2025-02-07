package com.example.pymesystem_backend.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String user_id;

    private String password;

    private String user_name;

    private String user_last_name;

    private String email;

    private String role;

}
