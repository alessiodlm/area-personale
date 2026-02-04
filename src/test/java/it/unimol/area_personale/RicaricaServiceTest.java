package it.unimol.area_personale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import it.unimol.area_personale.dto.SaldoAgg;
import it.unimol.area_personale.model.EventoRicarica;
import it.unimol.area_personale.model.LogEvento;
import it.unimol.area_personale.model.Notifica;
import it.unimol.area_personale.repository.EventoRicaricaRepository;
import it.unimol.area_personale.repository.LogEventoRepository;
import it.unimol.area_personale.repository.NotificaRepository;
import it.unimol.area_personale.service.RicaricaService;

@ExtendWith(MockitoExtension.class)
class RicaricaServiceTest {

  @Mock private EventoRicaricaRepository eventoRepo;
  @Mock private MongoTemplate mongoTemplate;
  @Mock private NotificaRepository notificaRepo;
  @Mock private LogEventoRepository logRepo;

  @InjectMocks private RicaricaService service;

  @Test
  void registraEvento_importoSottoUguale50_esitoCompletata() {
    EventoRicarica evento = new EventoRicarica();
    evento.setUserId("u1");
    evento.setImporto(20.0);
    evento.setOperatore("OP");

    when(eventoRepo.save(any(EventoRicarica.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    EventoRicarica salvato = service.registraEvento(evento);

    assertEquals(EventoRicarica.EsitoRicarica.COMPLETATA, salvato.getEsito());
    verify(notificaRepo, times(1)).save(any(Notifica.class));
    verify(logRepo, times(1)).save(any(LogEvento.class));
  }

  @Test
  void registraEvento_importoMaggiore50_esitoFallita() {
    EventoRicarica evento = new EventoRicarica();
    evento.setUserId("u1");
    evento.setImporto(80.0);
    evento.setOperatore("OP");

    when(eventoRepo.save(any(EventoRicarica.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    EventoRicarica salvato = service.registraEvento(evento);

    assertEquals(EventoRicarica.EsitoRicarica.FALLITA, salvato.getEsito());
  }

  @Test
  void calcolaSaldo_docNull_ritornaZero() {
    @SuppressWarnings("unchecked")
    AggregationResults<SaldoAgg> emptyResults = mock(AggregationResults.class);
    when(emptyResults.getUniqueMappedResult()).thenReturn(null);

    when(mongoTemplate.aggregate(
            any(Aggregation.class),
            eq("eventi_ricarica"),
            eq(SaldoAgg.class)
    )).thenReturn(emptyResults);

    assertEquals(0.0, service.calcolaSaldo("u1"), 0.0001);
  }

  @Test
  void calcolaSaldo_docPresente_ritornaSaldo() {
    SaldoAgg saldoAgg = new SaldoAgg();
    saldoAgg.setSaldo(42.0);

    @SuppressWarnings("unchecked")
    AggregationResults<SaldoAgg> results = mock(AggregationResults.class);
    when(results.getUniqueMappedResult()).thenReturn(saldoAgg);

    when(mongoTemplate.aggregate(
            any(Aggregation.class),
            eq("eventi_ricarica"),
            eq(SaldoAgg.class)
    )).thenReturn(results);

    assertEquals(42.0, service.calcolaSaldo("u1"), 0.0001);
  }
}
