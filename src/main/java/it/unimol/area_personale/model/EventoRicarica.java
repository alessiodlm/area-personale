package it.unimol.area_personale.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("eventi_ricarica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoRicarica {

    @Id
    private String id;

    private String userId; // collegamento all'utente
    private double importo;
    private String operatore; // es: Iliad, TIM, ecc.
    private EsitoRicarica esito;
    private LocalDateTime timestamp;

    public enum EsitoRicarica {
        COMPLETATA,
        FALLITA
    }
}
