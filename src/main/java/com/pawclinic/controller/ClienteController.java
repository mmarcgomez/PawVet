package com.pawclinic.controller;

import com.pawclinic.model.Cliente;
import com.pawclinic.model.Mascota;
import com.pawclinic.repository.ClienteRepository;
import com.pawclinic.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;

    public ClienteController(
            ClienteService clienteService,
            ClienteRepository clienteRepository
    ) {

        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    // FORMULARIO NUEVO CLIENTE

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {

        model.addAttribute("cliente", new Cliente());

        return "clientes/nuevo-cliente";
    }

    // GUARDAR CLIENTE

    @PostMapping("/guardar")
    public String guardarCliente(
            @ModelAttribute Cliente cliente
    ) {

        Optional<Cliente> existente =
                clienteService.buscarPorDni(cliente.getDni());

        if(existente.isPresent()) {

            return "redirect:/clientes/" +
                    existente.get().getId();
        }

        Cliente clienteGuardado =
                clienteService.guardarCliente(cliente);

        return "redirect:/clientes/" +
                clienteGuardado.getId();
    }

    // FICHA CLIENTE

    @GetMapping("/{id}")
    public String verFichaCliente(
            @PathVariable Long id,
            Model model
    ) {

        Optional<Cliente> cliente =
                clienteService.buscarPorId(id);

        if(cliente.isPresent()) {

            model.addAttribute(
                    "cliente",
                    cliente.get()
            );

            return "clientes/ficha-cliente";
        }

        return "redirect:/agenda";
    }

    // VALIDACIÓN DNI

    @GetMapping("/existe-dni")
    @ResponseBody
    public ResponseEntity<?> comprobarDni(
            @RequestParam String dni
    ) {

        Optional<Cliente> cliente =
                clienteService.buscarPorDni(dni);

        if(cliente.isPresent()) {
            return ResponseEntity.ok(
                    cliente.get().getId()
            );
        }

        return ResponseEntity.ok(null);
    }
    // LISTA CLIENTES
    @GetMapping
    public String listarClientes(
            @RequestParam(required = false) String busqueda,
            Model model
    ) {

        List<Cliente> clientes;

        if (busqueda != null && !busqueda.isBlank()) {

            clientes =
                    clienteRepository
                            .findByDniContainingOrTelefonoContaining(
                                    busqueda,
                                    busqueda
                            );

        } else {

            clientes =
                    clienteRepository.findByActivoTrue();
        }

        model.addAttribute("clientes", clientes);

        return "clientes/lista-clientes";
    }

    //EDITAR CLIENTE
    @GetMapping("/editar/{id}")
    public String editarCliente(
            @PathVariable Long id,
            Model model
    ) {

        Cliente cliente =
                clienteRepository.findById(id)
                        .orElseThrow();

        model.addAttribute(
                "cliente",
                cliente
        );

        return "clientes/editar-cliente";
    }
    //ACTUALIZAR
    @PostMapping("/actualizar")
    public String actualizarCliente(
            @ModelAttribute Cliente cliente
    ) {

        Cliente clienteActual =
                clienteRepository.findById(cliente.getId())
                        .orElseThrow();

        clienteActual.setNombre(cliente.getNombre());
        clienteActual.setApellidos(cliente.getApellidos());
        clienteActual.setTelefono(cliente.getTelefono());
        clienteActual.setEmail(cliente.getEmail());
        clienteActual.setCalle(cliente.getCalle());

        clienteRepository.save(clienteActual);

        return "redirect:/clientes/" +
                clienteActual.getId();
    }
    //DAR DE BAJA, si damos de baja al cliente a las mascotas tambien
    @PostMapping("/baja/{id}")
    public String darDeBaja(
            @PathVariable Long id
    ) {

        Cliente cliente =
                clienteRepository.findById(id)
                        .orElseThrow();

        cliente.setActivo(false);

        for(Mascota mascota : cliente.getMascotas()) {

            mascota.setActivo(false);
        }

        clienteRepository.save(cliente);

        return "redirect:/clientes";
    }
}