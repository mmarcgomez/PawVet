package com.pawclinic.repository;

import com.pawclinic.model.Cita;
import com.pawclinic.model.MotivoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByFechaHoraBetween(
            LocalDateTime inicio,
            LocalDateTime fin
    );
    boolean existsByFechaHora(LocalDateTime fechaHora);
    List<Cita> findByMascotaIdAndFechaHoraAfterOrderByFechaHoraAsc(
            Long mascotaId,
            LocalDateTime fechaHora
    );

    List<Cita> findByFechaHoraBetweenAndMotivoIn(
            LocalDateTime inicio,
            LocalDateTime fin,
            List<MotivoCita> motivos
    );
}