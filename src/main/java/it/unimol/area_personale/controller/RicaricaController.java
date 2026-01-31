package it.unimol.area_personale.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unimol.area_personale.dto.RicaricaRequest;
import it.unimol.area_personale.model.EventoRicarica;
import it.unimol.area_personale.model.Notifica;
import it.unimol.area_personale.repository.NotificaRepository;
import it.unimol.area_personale.service.RicaricaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/utenti")
@RequiredArgsConstructor
public class RicaricaController {

    private final RicaricaService ricaricaService;
    private final NotificaRepository notificaRepo;

    /**
     * Inserisce un nuovo evento di ricarica per l'utente specificato
     */
    @PostMapping("/{userId}/ricarica")
    public ResponseEntity<?> ricaricaUtente(
        @PathVariable String userId,
        @RequestBody RicaricaRequest request
) {
    // VALIDAZIONE semplice
    if (request.getImporto() <= 0) {
        return ResponseEntity.badRequest().body("L'importo deve essere maggiore di zero.");
    }

    EventoRicarica evento = new EventoRicarica(
            null,
            userId,
            request.getImporto(),
            request.getOperatore(),
            null,
            LocalDateTime.now()
    );

    EventoRicarica salvato = ricaricaService.registraEvento(evento);
    return ResponseEntity.ok(salvato);
}

    /**
     * Ritorna lo storico ricariche di un utente
     */
    @GetMapping("/{userId}/storico")
    public ResponseEntity<List<EventoRicarica>> storicoUtente(@PathVariable String userId) {
        return ResponseEntity.ok(ricaricaService.storicoPerUtente(userId));
    }

     /*ritorna il saldo dell'utente */
    @GetMapping("/{userId}/saldo")
public ResponseEntity<Double> saldoUtente(@PathVariable String userId) {
    double saldo = ricaricaService.calcolaSaldo(userId);
    return ResponseEntity.ok(saldo);
}

    @GetMapping("/{userId}/notifiche")
    public ResponseEntity<List<Notifica>> notificheUtente(@PathVariable String userId) {
    return ResponseEntity.ok(notificaRepo.findByUserIdOrderByTimestampDesc(userId));
}

}
