package it.unimol.area_personale.model;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("utenti")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String telefono;

    private List<MetodoPagamento> metodiPagamento;

    private PianoAttivo pianoAttivo;

    // classi interne o separate
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetodoPagamento {
        private String tipo; // es. carta, PayPal
        private String numero;
        private String scadenza;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PianoAttivo {
        private String nome;
        private double prezzo;
        private boolean rinnovoMensile;
    }
}
