package com.pawclinic.model;

public enum EstadoCita {

    PENDIENTE("Pendiente"),
    ASISTIO ("Asistió"),
    EN_CURSO("En curso"),
    COMPLETADA("Completada");

    private final String label;

    EstadoCita(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}