package it.unimol.area_personale.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.unimol.area_personale.model.EventoRicarica;

public interface EventoRicaricaRepository extends MongoRepository<EventoRicarica, String> {

    List<EventoRicarica> findByUserIdOrderByTimestampDesc(String userId);
}
