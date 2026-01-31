package it.unimol.area_personale.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unimol.area_personale.model.LogEvento;
import it.unimol.area_personale.repository.LogEventoRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogEventoController {

    private final LogEventoRepository logRepo;

    @GetMapping("/{userId}")
    public List<LogEvento> getLogPerUtente(@PathVariable String userId) {
        return logRepo.findByUserId(userId);
    }
}
