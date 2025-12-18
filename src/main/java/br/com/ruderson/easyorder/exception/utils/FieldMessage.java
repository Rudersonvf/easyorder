package br.com.ruderson.easyorder.exception.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldMessage {
    private String fieldName;
    private String message;
}
