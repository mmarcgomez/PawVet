package com.pawclinic.model;

public enum MotivoCita {

    VACUNACION(20),
    DESPARASITACION(20),
    REVISION(20),
    MICROCHIP(20),
    ANALITICA(20),
    RADIOGRAFÍA(20),
    //URGENCIAS
    URGENCIA_VITAL(20),
    URGENCIA_NO_VITAL(20),
    VISITA_SIN_CITA(20),

    OTROS(40),

    CIRUGIA_MENOR(80),
    CIRUGIA_MAYOR(120);

    private final int duracion;

    MotivoCita(int duracion) {
        this.duracion = duracion;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getLabel() {
        return switch (this) {
            case VACUNACION -> "Vacunación";
            case DESPARASITACION -> "Desparasitación";
            case REVISION -> "Revisión";
            case CIRUGIA_MAYOR -> "Cirugía mayor";
            case CIRUGIA_MENOR -> "Cirugía menor";
            case MICROCHIP -> "Microchip";
            case ANALITICA -> "Analítica";
            case RADIOGRAFÍA -> "Radiografía";
            case URGENCIA_VITAL -> "Urgencia Vital";
            case URGENCIA_NO_VITAL -> "Urgencia no Vital";
            case VISITA_SIN_CITA -> "Visita sin Cita";
            case OTROS -> "Otros";
        };
    }
}