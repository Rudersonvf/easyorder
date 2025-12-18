package br.com.ruderson.easyorder.exception;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String msg) {
        super(msg);
    }
}
