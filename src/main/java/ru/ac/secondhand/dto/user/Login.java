package ru.ac.secondhand.dto.user;

import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class Login {

    @Size(min = 8, max = 16)
    private String password;

    @Size(min = 4, max = 32)
    private String username;
}
