package com.pawclinic.controller;

import com.pawclinic.model.Cita;
import com.pawclinic.model.MotivoCita;
import com.pawclinic.repository.CitaRepository;
import com.pawclinic.service.CitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AgendaController {

    private final CitaService citaService;
    private final CitaRepository citaRepository;

    public AgendaController(CitaService citaService, CitaRepository citaRepository) {
        this.citaService = citaService; this.citaRepository = citaRepository;
    }

    @GetMapping("/agenda")
    public String agenda(

            @RequestParam(required = false)
            String fecha,

            Model model
    ) {

        LocalDate fechaSeleccionada;

        if (fecha == null || fecha.isBlank()) {

            fechaSeleccionada = LocalDate.now();

        } else {

            fechaSeleccionada = LocalDate.parse(fecha);
        }

        List<Cita> citas =
                citaService.obtenerCitasPorFecha(
                        fechaSeleccionada
                );

        model.addAttribute(
                "citas",
                citas
        );

        model.addAttribute(
                "fecha",
                fechaSeleccionada
        );

        model.addAttribute(
                "fechaAnterior",
                fechaSeleccionada.minusDays(1)
        );

        model.addAttribute(
                "fechaSiguiente",
                fechaSeleccionada.plusDays(1)
        );
        List<Cita> citasNormales =
                citas.stream()
                        .filter(c ->
                                c.getMotivo() != MotivoCita.URGENCIA_VITAL
                                        && c.getMotivo() != MotivoCita.URGENCIA_NO_VITAL
                                        && c.getMotivo() != MotivoCita.VISITA_SIN_CITA
                        )
                        .toList();

        List<Cita> urgencias =
                citas.stream()
                        .filter(c ->
                                c.getMotivo() == MotivoCita.URGENCIA_VITAL
                                        || c.getMotivo() == MotivoCita.URGENCIA_NO_VITAL
                                        || c.getMotivo() == MotivoCita.VISITA_SIN_CITA
                        )
                        .toList();

        model.addAttribute(
                "citasNormales",
                citasNormales
        );

        model.addAttribute(
                "urgencias",
                urgencias
        );
        return "agenda";
    }
}
