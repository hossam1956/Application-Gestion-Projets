package com.Projet_E2425G3_1.GestionProjets.exception;

public class TaskAlreadyAssignedException extends RuntimeException {
    public TaskAlreadyAssignedException(String message) {
        super(message);
    }
}