package com.pawclinic.model;

import jakarta.persistence.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Especie especie;

    private String raza;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;


    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

    private String microchip;

    private boolean esterilizado;

    private boolean activo = true;

    // ===== RELACIÓN CLIENTE =====

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getMicrochip() {
        return microchip;
    }

    public void setMicrochip(String microchip) {
        this.microchip = microchip;
    }

    public boolean isEsterilizado() {
        return esterilizado;
    }

    public void setEsterilizado(boolean esterilizado) {
        this.esterilizado = esterilizado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // ===== METODO PARA CALCULAR LA EDAD QUE NO SE GUARDA EN LA BD
    @Transient
    public String getEdad() {

        if (fechaNacimiento == null) {
            return "Desconocida";
        }

        LocalDate nacimiento = new java.sql.Date(
                fechaNacimiento.getTime()
        ).toLocalDate();

        LocalDate hoy = LocalDate.now();

        Period periodo =
                Period.between(nacimiento, hoy);

        int anios = periodo.getYears();
        int meses = periodo.getMonths();

        return anios + " años y " + meses + " meses";
    }
}