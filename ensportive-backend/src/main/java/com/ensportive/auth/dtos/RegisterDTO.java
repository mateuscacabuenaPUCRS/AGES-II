package com.ensportive.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDTO {

    private String username;
    private String password;
    private Long studentId;
}
