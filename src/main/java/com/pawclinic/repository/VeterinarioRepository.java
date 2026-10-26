package com.pawclinic.repository;

import com.pawclinic.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarioRepository
        extends JpaRepository<Veterinario, Long> {
}
