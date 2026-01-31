package it.unimol.area_personale.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "logs_audit")
public class LogEvento {

    @Id
    private String id;

    private String azione;         // es: "RicaricaUtente"
    private String userId;
    private LocalDateTime timestamp;
    private String esitoTecnico;   // es: "OK" o "FALLITO"

    private DettagliRicarica dettagli; // info opzionali

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DettagliRicarica {
        private double importo;
        private String operatore;
        private String esito;  // COMPLETATA o FALLITA
    }
}
