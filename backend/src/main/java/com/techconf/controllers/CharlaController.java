package com.techconf.controllers;
import com.techconf.models.Charla;
import com.techconf.models.Asistente;
import com.techconf.repositories.AsistenteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import com.techconf.repositories.CharlaRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/charlas")
@CrossOrigin(origins = "http://localhost:4200")
public class CharlaController {
    private final CharlaRepository repository;
    private final AsistenteRepository asistentes;
    public CharlaController(CharlaRepository repository, AsistenteRepository asistentes) {
        this.repository = repository;
        this.asistentes = asistentes;
    }
    @GetMapping
    public List<Charla> obtenerTodas() { return repository.findAll(); }
    @PostMapping
    public Charla registrarCharla(@Valid @RequestBody Charla charla) {
        charla.setId(null);
        charla.getAsistentes().clear();
        return repository.save(charla);
    }
    @PostMapping("/{id}/asistentes")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Asistente inscribirAsistente(@PathVariable Long id, @Valid @RequestBody Asistente asistente) {
        Charla charla = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Charla no encontrada"));
        asistente.setId(null);
        asistente.setCharla(charla);
        charla.getAsistentes().add(asistente);
        return asistentes.save(asistente);
    }
}
