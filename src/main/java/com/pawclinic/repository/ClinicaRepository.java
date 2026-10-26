package com.pawclinic.repository;

import com.pawclinic.model.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicaRepository
        extends JpaRepository<Clinica, Long> {
}