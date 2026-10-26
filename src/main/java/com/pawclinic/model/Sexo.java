package com.pawclinic.model;

public enum Sexo {
    MACHO,
    HEMBRA,
    DESCONOCIDO;

    /**
     * Etiqueta legible para la UI.
     */
    public String getLabel() {
        return switch (this) {
            case MACHO -> "M";
            case HEMBRA -> "H";
            case DESCONOCIDO -> "Desconocido";

        };
    }
}
