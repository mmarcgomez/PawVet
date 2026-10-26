package com.pawclinic.service;

import com.pawclinic.model.Cita;
import com.pawclinic.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public List<Cita> obtenerCitasDeHoy() {

        LocalDate hoy = LocalDate.now();

        LocalDateTime inicio = hoy.atStartOfDay();

        LocalDateTime fin = hoy.atTime(23, 59, 59);

        return citaRepository.findByFechaHoraBetween(inicio, fin);
    }
    public List<Cita> obtenerCitasPorFecha(LocalDate fecha) {

        LocalDateTime inicio =
                fecha.atStartOfDay();

        LocalDateTime fin =
                fecha.atTime(23,59,59);

        return citaRepository.findByFechaHoraBetween(
                inicio,
                fin
        );
    }
}