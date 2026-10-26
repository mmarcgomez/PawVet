package com.pawclinic.controller;

import com.pawclinic.model.Cita;
import com.pawclinic.model.Cliente;
import com.pawclinic.model.Mascota;
import com.pawclinic.repository.CitaRepository;
import com.pawclinic.repository.ClienteRepository;
import com.pawclinic.repository.MascotaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // FORMULARIO NUEVA MASCOTA

    @GetMapping("/nueva")
    public String nuevaMascota(@RequestParam Long clienteId,
                               Model model) {

        Mascota mascota = new Mascota();

        model.addAttribute("mascota", mascota);
        model.addAttribute("clienteId", clienteId);

        return "mascotas/nueva-mascota";
    }

    // GUARDAR MASCOTA

    @PostMapping("/guardar")
    public String guardarMascota(@ModelAttribute Mascota mascota,
                                 @RequestParam Long clienteId) {

        Cliente cliente =
                clienteRepository.findById(clienteId)
                        .orElseThrow();

        mascota.setCliente(cliente);

        mascotaRepository.save(mascota);

        return "redirect:/clientes/" + clienteId;
    }
    @GetMapping("/razas")
    @ResponseBody
    public List<String> buscarRazas(
            @RequestParam String q,
            @RequestParam String especie) {

        RestTemplate restTemplate =
                new RestTemplate();

        String url;

        if(especie.equals("CANINO")) {

            url =
                    "https://api.thedogapi.com/v1/breeds/search?q=" + q;

        } else {

            url =
                    "https://api.thecatapi.com/v1/breeds/search?q=" + q;
        }

        HttpHeaders headers =
                new HttpHeaders();

        headers.set("x-api-key",
                "live_NnTkoMlllmEDXNMdCiskcz0m0XIbkxWNIz5KoTIbrLLTwvp8rm3d7lDh7q7Xy1N5");

        HttpEntity<String> entity =
                new HttpEntity<>(headers);

        ResponseEntity<List> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        List.class
                );

        List<Map<String, Object>> body =
                response.getBody();

        return body.stream()
                .map(r -> r.get("name").toString())
                .toList();
    }

    //FICHA MASCOTA
    @GetMapping("/{id}")
    public String verFichaMascota(
            @PathVariable Long id,
            Model model
    ) {

        Optional<Mascota> mascotaOpt =
                mascotaRepository.findById(id);

        if(mascotaOpt.isEmpty()) {
            return "redirect:/clientes";
        }

        Mascota mascota = mascotaOpt.get();

        List<Cita> proximasCitas =
                citaRepository
                        .findByMascotaIdAndFechaHoraAfterOrderByFechaHoraAsc(
                                id,
                                LocalDateTime.now()
                        );

        model.addAttribute("mascota", mascota);
        model.addAttribute("proximasCitas", proximasCitas);

        return "mascotas/ficha-mascota";
    }

    //BAJA
    @PostMapping("/baja/{id}")
    public String bajaMascota(
            @PathVariable Long id
    ) {

        Mascota mascota =
                mascotaRepository.findById(id)
                        .orElseThrow();

        mascota.setActivo(false);

        mascotaRepository.save(mascota);

        return "redirect:/clientes/" +
                mascota.getCliente().getId();
    }
}