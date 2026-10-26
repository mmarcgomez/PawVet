package com.pawclinic.repository;

import com.pawclinic.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDni(String dni);
    List<Cliente> findByDniContainingOrTelefonoContaining(
            String dni,
            String telefono
    );
    List<Cliente> findByNombreContainingIgnoreCaseOrDniContainingOrTelefonoContaining(
            String nombre,
            String dni,
            String telefono
    );
    List<Cliente> findByActivoTrue();

    List<Cliente> findByActivoFalse();

}