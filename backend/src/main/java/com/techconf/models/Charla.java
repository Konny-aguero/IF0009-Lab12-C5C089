package com.techconf.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Charla {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(min = 5) @Column(nullable = false)
    private String titulo;
    @NotBlank @Column(nullable = false)
    private String expositor;
    @NotBlank @Pattern(regexp = "Principiante|Intermedio|Avanzado")
    private String nivel;
    @NotBlank @Email @Column(name = "email_contacto")
    private String emailContacto;
    @NotNull @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @NotNull @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    @ElementCollection
    @CollectionTable(name = "charla_etiquetas", joinColumns = @JoinColumn(name = "charla_id"))
    @Column(name = "etiqueta")
    @NotEmpty
    private List<@NotBlank String> etiquetas = new ArrayList<>();

    @AssertTrue(message = "La fecha de fin no puede ser anterior a la de inicio.")
    @JsonIgnore
    public boolean isRangoFechasValido() {
        return fechaInicio == null || fechaFin == null || !fechaFin.isBefore(fechaInicio);
    }
    public Charla() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getExpositor() { return expositor; }
    public void setExpositor(String expositor) { this.expositor = expositor; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public List<String> getEtiquetas() { return etiquetas; }
    public void setEtiquetas(List<String> etiquetas) { this.etiquetas = etiquetas; }
}
