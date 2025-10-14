package org.example.entidades;

// Esta clase no cambia en absoluto. Sigue siendo una excepción estándar.
public class CitaException extends Exception {
    public CitaException(String message) {
        super(message);
    }
}