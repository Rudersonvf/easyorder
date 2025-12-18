package br.com.ruderson.easyorder.exception.utils;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomError {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;
}
