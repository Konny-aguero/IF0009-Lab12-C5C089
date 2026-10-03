package com.techconf.controllers;
import com.techconf.models.Charla;
import com.techconf.repositories.CharlaRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/charlas")
@CrossOrigin(origins = "http://localhost:4200")
public class CharlaController {
    private final CharlaRepository repository;
    public CharlaController(CharlaRepository repository) { this.repository = repository; }
    @GetMapping
    public List<Charla> obtenerTodas() { return repository.findAll(); }
    @PostMapping
    public Charla registrarCharla(@Valid @RequestBody Charla charla) {
        charla.setId(null);
        return repository.save(charla);
    }
}
