package it.unimol.area_personale.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.unimol.area_personale.model.LogEvento;
import it.unimol.area_personale.repository.LogEventoRepository;

@WebMvcTest(LogEventoController.class)
class LogEventoControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private LogEventoRepository logRepo;

    @Test
    void getLogsPerUtente_restituisceLista() throws Exception {
        LogEvento log = LogEvento.builder()
                .id("l1")
                .userId("u1")
                .azione("RicaricaUtente")
                .esitoTecnico("OK")
                .timestamp(LocalDateTime.now())
                .build();

        when(logRepo.findByUserId("u1")).thenReturn(List.of(log));

        mockMvc.perform(get("/api/logs/u1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].userId").value("u1"))
            .andExpect(jsonPath("$[0].azione").value("RicaricaUtente"));
    }
}
