package com.pawclinic.service;

import com.pawclinic.model.Cliente;
import com.pawclinic.repository.ClienteRepository;
import com.pawclinic.repository.ClinicaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClinicaRepository clinicaRepository;

    public ClienteService(ClienteRepository clienteRepository, ClinicaRepository clinicaRepository) {
        this.clienteRepository = clienteRepository;
        this.clinicaRepository = clinicaRepository;
    }

    public Cliente guardarCliente(Cliente cliente) {

        cliente.setActivo(true);
        cliente.setClinica(
                clinicaRepository.getReferenceById(1L)
        );

        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni);
    }
}