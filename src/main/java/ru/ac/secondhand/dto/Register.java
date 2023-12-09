package ru.ac.secondhand.dto;

import lombok.Data;
import ru.ac.secondhand.entity.enums.Role;

@Data
public class Register {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
