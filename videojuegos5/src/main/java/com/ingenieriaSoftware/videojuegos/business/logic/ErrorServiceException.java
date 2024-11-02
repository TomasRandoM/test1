package com.ingenieriaSoftware.videojuegos.business.logic;

/**
 * @author Tomas Rando
 * @version 1.0.0
 * Excepción personalizada
 */
public class ErrorServiceException extends Exception {
    public ErrorServiceException() {}

    public ErrorServiceException(String msg) {
        super(msg);
    }
}