package com.techconf.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Asistente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(min = 3) @Column(nullable = false)
    private String nombre;
    @NotBlank @Email @Column(nullable = false)
    private String correo;
    @NotNull @Min(value = 19, message = "El asistente debe ser mayor de 18 años.")
    @Column(nullable = false)
    private Integer edad;
    @ManyToOne(optional = false)
    @JoinColumn(name = "charla_id", nullable = false)
    @JsonIgnore
    private Charla charla;

    public Asistente() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public Charla getCharla() { return charla; }
    public void setCharla(Charla charla) { this.charla = charla; }
}
