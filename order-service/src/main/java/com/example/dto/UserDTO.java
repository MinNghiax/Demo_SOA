package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private Long id;
    private String username;
    private String email;

    public UserDTO() {}

}
