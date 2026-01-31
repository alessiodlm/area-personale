package it.unimol.area_personale.service;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import it.unimol.area_personale.model.EventoRicarica;
import it.unimol.area_personale.model.LogEvento;
import it.unimol.area_personale.model.Notifica;
import it.unimol.area_personale.repository.EventoRicaricaRepository;
import it.unimol.area_personale.repository.LogEventoRepository;
import it.unimol.area_personale.repository.NotificaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RicaricaService {

    private final EventoRicaricaRepository eventoRepo;
    private final MongoTemplate mongoTemplate;
    private final NotificaRepository notificaRepo;
    private final LogEventoRepository logRepo;


public EventoRicarica registraEvento(EventoRicarica evento) {
    EventoRicarica.EsitoRicarica esitoGenerato =
        evento.getImporto() <= 50
            ? EventoRicarica.EsitoRicarica.COMPLETATA
            : EventoRicarica.EsitoRicarica.FALLITA;

    evento.setEsito(esitoGenerato);
    EventoRicarica salvato = eventoRepo.save(evento);

    Notifica notifica = Notifica.builder()
            .userId(salvato.getUserId())
            .messaggio("Ricarica di " + salvato.getImporto() + "€ "
                + (salvato.getEsito() == EventoRicarica.EsitoRicarica.COMPLETATA ? "completata" : "non avvenuta") + ".")
            .timestamp(LocalDateTime.now())
            .build();
    notificaRepo.save(notifica);

    LogEvento log = LogEvento.builder()
            .azione("RicaricaUtente")
            .userId(salvato.getUserId())
            .timestamp(LocalDateTime.now())
            .esitoTecnico("OK")
            .dettagli(LogEvento.DettagliRicarica.builder()
                    .importo(salvato.getImporto())
                    .operatore(salvato.getOperatore())
                    .esito(salvato.getEsito().toString())
                    .build())
            .build();
    logRepo.save(log);

    return salvato;
}

    public List<EventoRicarica> storicoPerUtente(String userId) {
        return eventoRepo.findByUserIdOrderByTimestampDesc(userId);
    }

    
    public double calcolaSaldo(String userId) {
    MatchOperation match = Aggregation.match(
                Criteria.where("userId").is(userId)
                        .and("esito").is(EventoRicarica.EsitoRicarica.COMPLETATA)
        );

        GroupOperation group = Aggregation.group("userId")
                .sum("importo").as("saldo");

        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<Document> result = mongoTemplate.aggregate(
                aggregation, "eventi_ricarica", Document.class
        );

        Document doc = result.getUniqueMappedResult();
        return (doc != null) ? doc.getDouble("saldo") : 0.0;
    }
}
