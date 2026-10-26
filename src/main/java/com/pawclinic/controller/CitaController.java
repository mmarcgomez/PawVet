package com.pawclinic.controller;

import com.pawclinic.model.*;
import com.pawclinic.repository.CitaRepository;
import com.pawclinic.repository.ClienteRepository;
import com.pawclinic.repository.MascotaRepository;
import com.pawclinic.repository.VeterinarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;
    public CitaController(
            CitaRepository citaRepository,
            ClienteRepository clienteRepository,
            MascotaRepository mascotaRepository,
            VeterinarioRepository veterinarioRepository
    ) {
        this.citaRepository = citaRepository;
        this.clienteRepository = clienteRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    // =========================
    // PANTALLA NUEVA CITA
    // =========================

    @GetMapping("/nueva/{mascotaId}")
    public String nuevaCita(
            @PathVariable Long mascotaId,

            @RequestParam(required = false)
            String fecha,

            Model model
    ){

        Optional<Mascota> mascotaOpt =
                mascotaRepository.findById(mascotaId);

        if(mascotaOpt.isEmpty()) {
            return "redirect:/agenda";
        }

        Mascota mascota = mascotaOpt.get();

        model.addAttribute("mascota", mascota);

        model.addAttribute(
                "cliente",
                mascota.getCliente()
        );

        model.addAttribute(
                "motivos",
                MotivoCita.values()
        );
        List<Cita> historial =
                citaRepository.findByMascotaIdAndFechaHoraAfterOrderByFechaHoraAsc(
                        mascotaId,
                        LocalDateTime.now()
                );

        model.addAttribute(
                "historial",
                historial
        );

        // SELECCION DE FECHA
        LocalDate fechaSeleccionada;

        if(fecha == null || fecha.isBlank()) {

            fechaSeleccionada = LocalDate.now();

        } else {

            fechaSeleccionada = LocalDate.parse(fecha);
        }

        model.addAttribute(
                "fechaSeleccionada",
                fechaSeleccionada
        );

        // TODAS LAS HORAS
        List<LocalTime> horas =
                generarHoras();

        model.addAttribute(
                "horas",
                horas
        );

        // CITAS YA OCUPADAS
        List<Cita> citasDelDia =
                citaRepository.findByFechaHoraBetween(
                        fechaSeleccionada.atStartOfDay(),
                        fechaSeleccionada.atTime(23,59)
                );

        List<LocalTime> horasOcupadas = new ArrayList<>();

        for (Cita cita : citasDelDia) {

            LocalTime inicio =
                    cita.getFechaHora().toLocalTime();

            int duracion =
                    cita.getDuracionMin();

            LocalTime bloqueActual = inicio;

            while (bloqueActual.isBefore(
                    inicio.plusMinutes(duracion))) {

                horasOcupadas.add(bloqueActual);

                bloqueActual =
                        bloqueActual.plusMinutes(20);
            }
        }

        model.addAttribute(
                "horasOcupadas",
                horasOcupadas
        );

        return "citas/nueva-cita";
    }

    // =========================
    // GUARDAR CITA
    // =========================

    @PostMapping("/guardar")
    public String guardarCita(

            @RequestParam Long mascotaId,

            @RequestParam String fecha,

            @RequestParam String hora,

            @RequestParam String motivo,

            @RequestParam(required = false)
            String detalles
    ) {

        Optional<Mascota> mascotaOpt =
                mascotaRepository.findById(mascotaId);

        if(mascotaOpt.isEmpty()) {
            return "redirect:/agenda";
        }

        Mascota mascota =
                mascotaOpt.get();

        LocalDate localDate =
                LocalDate.parse(fecha);
        if(hora == null || hora.isBlank()){

            return "redirect:/citas/nueva/"
                    + mascotaId
                    + "?fecha="
                    + fecha
                    + "&error=hora";
        }
        LocalTime localTime =
                LocalTime.parse(hora);

        LocalDateTime fechaHora =
                LocalDateTime.of(
                        localDate,
                        localTime
                );

        MotivoCita motivoEnum =
                MotivoCita.valueOf(motivo);

        // =========================
        // VALIDAR SOLAPAMIENTOS
        // =========================

        LocalDateTime finNuevaCita =
                fechaHora.plusMinutes(
                        motivoEnum.getDuracion()
                );

        List<Cita> citasDia =
                citaRepository.findByFechaHoraBetween(
                        localDate.atStartOfDay(),
                        localDate.atTime(23, 59)
                );

        for(Cita citaExistente : citasDia){

            LocalDateTime inicioExistente =
                    citaExistente.getFechaHora();

            LocalDateTime finExistente =
                    inicioExistente.plusMinutes(
                            citaExistente.getDuracionMin()
                    );

            boolean seSolapan =
                    fechaHora.isBefore(finExistente)
                            &&
                            finNuevaCita.isAfter(inicioExistente);

            if(seSolapan){

                return "redirect:/citas/nueva/"
                        + mascotaId
                        + "?fecha="
                        + fecha
                        + "&error=solapada";
            }
        }

        // =========================
        // CREAR CITA
        // =========================

        Cita cita = new Cita();

        cita.setMascota(mascota);

        cita.setFechaHora(fechaHora);

        cita.setMotivo(motivoEnum);

        cita.setDuracionMin(
                motivoEnum.getDuracion()
        );

        cita.setEstado(
                EstadoCita.PENDIENTE
        );

        cita.setDetalles(detalles);

        cita.setClinica(
                mascota.getCliente()
                        .getClinica()
        );

        Optional<Veterinario> vetOpt =
                veterinarioRepository.findById(1L);

        vetOpt.ifPresent(cita::setVeterinario);

        citaRepository.save(cita);

        return "redirect:/clientes/"
                + mascota.getCliente().getId();
    }

    // =========================
    // GENERAR HORAS
    // =========================

    private List<LocalTime> generarHoras() {

        List<LocalTime> horas = new ArrayList<>();

        LocalTime inicio = LocalTime.of(10, 0);
        LocalTime fin = LocalTime.of(20, 40);

        while (!inicio.isAfter(fin)) {

            horas.add(inicio);

            inicio = inicio.plusMinutes(20);
        }

        return horas;
    }

    // Botón de COMPLETAR
    @PostMapping("/completar/{id}")
    public String completarCita(@PathVariable Long id) {

        Optional<Cita> citaOpt =
                citaRepository.findById(id);

        if(citaOpt.isPresent()) {

            Cita cita = citaOpt.get();

            if(cita.getEstado() == EstadoCita.COMPLETADA) {

                cita.setEstado(EstadoCita.PENDIENTE);

            } else {

                cita.setEstado(EstadoCita.COMPLETADA);
            }

            citaRepository.save(cita);
        }

        return "redirect:/agenda";
    }
    //LLamado a consulta
    @PostMapping("/en-consulta/{id}")
    public String enConsulta(@PathVariable Long id){

        Cita cita =
                citaRepository.findById(id)
                        .orElseThrow();

        cita.setEstado(
                EstadoCita.EN_CURSO
        );

        citaRepository.save(cita);

        return "redirect:/agenda";
    }

    //H de llegada
    @PostMapping("/asistio/{id}")
    public String asistio(@PathVariable Long id) {

        Optional<Cita> citaOpt =
                citaRepository.findById(id);

        if(citaOpt.isPresent()) {

            Cita cita = citaOpt.get();

            if(cita.getHoraLlegada() == null) {

                cita.setHoraLlegada(
                        LocalDateTime.now()
                );

            } else {

                cita.setHoraLlegada(null);
            }

            citaRepository.save(cita);
        }

        return "redirect:/agenda";
    }

    //URGENCIAS
    @GetMapping("/urgencia")
    public String nuevaUrgencia(
            @RequestParam(required = false)
            String busqueda,
            Model model){

        if(busqueda != null && !busqueda.isBlank()){

            model.addAttribute(
                    "clientesEncontrados",
                    clienteRepository
                            .findByDniContainingOrTelefonoContaining(
                                    busqueda,
                                    busqueda
                            )
            );
        }

        model.addAttribute(
                "busqueda",
                busqueda
        );

        return "citas/nueva-urgencia";
    }

    @PostMapping("/guardar-urgencia")
    public String guardarUrgencia(

            @RequestParam Long mascotaId,

            @RequestParam String motivo,

            @RequestParam String detalles

    ) {

        Optional<Mascota> mascotaOpt =
                mascotaRepository.findById(mascotaId);

        if(mascotaOpt.isEmpty()) {
            return "redirect:/agenda";
        }

        Mascota mascota = mascotaOpt.get();

        Cita cita = new Cita();

        cita.setMascota(mascota);

        // Hora actual
        cita.setFechaHora(
                LocalDateTime.now()
        );

        MotivoCita motivoEnum =
                MotivoCita.valueOf(motivo);

        cita.setMotivo(motivoEnum);

        cita.setDetalles(detalles);

        cita.setEstado(
                EstadoCita.PENDIENTE
        );

        // duración automática
        cita.setDuracionMin(
                motivoEnum.getDuracion()
        );

        cita.setClinica(
                mascota.getCliente()
                        .getClinica()
        );

        veterinarioRepository.findById(1L)
                .ifPresent(cita::setVeterinario);

        citaRepository.save(cita);

        return "redirect:/agenda";
    }

}