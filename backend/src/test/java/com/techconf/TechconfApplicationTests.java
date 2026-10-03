package com.techconf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TechconfApplicationTests {
    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired jakarta.persistence.EntityManager entityManager;
    @Test void semillasYSerializacionSinRecursion() throws Exception {
        mvc.perform(get("/api/charlas"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].etiquetas.length()").value(2))
            .andExpect(jsonPath("$[0].asistentes.length()").value(2))
            .andExpect(jsonPath("$[0].asistentes[0].charla").doesNotExist());
        assertEquals(5, jdbc.queryForObject("SELECT COUNT(*) FROM asistente", Integer.class));
    }
    @Test void inscribeYPersisteAsistente() throws Exception {
        mvc.perform(post("/api/charlas/1/asistentes").contentType(MediaType.APPLICATION_JSON)
            .content("{\"nombre\":\"Persona Prueba\",\"correo\":\"prueba@example.com\",\"edad\":19}"))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.charla").doesNotExist());
        mvc.perform(get("/api/charlas")).andExpect(jsonPath("$[0].asistentes.length()").value(3));
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM asistente WHERE correo='prueba@example.com' AND charla_id=1", Integer.class));
    }
    @Test void rechazaAsistenteInvalidoYCharlaInexistente() throws Exception {
        for (String datos : new String[]{
            "{\"nombre\":\"Ana Mora\",\"correo\":\"ana@example.com\",\"edad\":18}",
            "{\"nombre\":\"An\",\"correo\":\"ana@example.com\",\"edad\":19}",
            "{\"nombre\":\"Ana Mora\",\"correo\":\"invalido\",\"edad\":19}", "{}"}) {
            mvc.perform(post("/api/charlas/1/asistentes").contentType(MediaType.APPLICATION_JSON).content(datos))
                .andExpect(status().isBadRequest());
        }
        mvc.perform(post("/api/charlas/999/asistentes").contentType(MediaType.APPLICATION_JSON)
            .content("{\"nombre\":\"Ana Mora\",\"correo\":\"ana@example.com\",\"edad\":19}"))
            .andExpect(status().isNotFound());
    }
    private String charla(String inicio, String fin, String etiquetas) {
        return "{\"titulo\":\"Docker y DevOps\",\"expositor\":\"Ana Mora\",\"nivel\":\"Intermedio\",\"emailContacto\":\"ana@example.com\",\"fechaInicio\":\"" + inicio + "\",\"fechaFin\":\"" + fin + "\",\"etiquetas\":" + etiquetas + "}";
    }
    @Test void registraCharlaConTresEtiquetas() throws Exception {
        mvc.perform(post("/api/charlas").contentType(MediaType.APPLICATION_JSON)
            .content(charla("2026-11-20", "2026-11-20", "[\"Docker\",\"DevOps\",\"CI/CD\"]")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.etiquetas.length()").value(3));
        entityManager.flush();
        assertEquals(10, jdbc.queryForObject("SELECT COUNT(*) FROM charla_etiquetas", Integer.class));
    }
    @Test void rechazaFechasYEtiquetasInvalidas() throws Exception {
        for (String datos : new String[]{charla("2026-11-21", "2026-11-20", "[\"Java\"]"),
            charla("2026-11-20", "2026-11-20", "[]"), charla("2026-11-20", "2026-11-20", "[\"\"]"), "{}"}) {
            mvc.perform(post("/api/charlas").contentType(MediaType.APPLICATION_JSON).content(datos))
                .andExpect(status().isBadRequest());
        }
    }
}
