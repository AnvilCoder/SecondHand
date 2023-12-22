package ru.ac.secondhand.utils.GEH;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiMessageError {

    private HttpStatus status;
    private String message;
}
