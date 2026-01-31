package it.unimol.area_personale;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import it.unimol.area_personale.repository.EventoRicaricaRepository;
import it.unimol.area_personale.repository.LogEventoRepository;
import it.unimol.area_personale.repository.NotificaRepository;
import it.unimol.area_personale.repository.UtenteRepository;

@SpringBootTest(classes = AreaPersonaleApplication.class)
@ActiveProfiles("test")
class AreaPersonaleApplicationTests {

    // serve perché nel codice usi MongoTemplate in qualche service
    @MockBean private MongoTemplate mongoTemplate;

    // repository richiesti dai controller/service
    @MockBean private LogEventoRepository logEventoRepository;
    @MockBean private EventoRicaricaRepository eventoRicaricaRepository;
    @MockBean private NotificaRepository notificaRepository;
    @MockBean private UtenteRepository utenteRepository;

    @Test
    void contextLoads() {}
}
