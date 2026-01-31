package it.unimol.area_personale.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.unimol.area_personale.model.LogEvento;

public interface LogEventoRepository extends MongoRepository<LogEvento, String> {
    List<LogEvento> findByUserId(String userId);
}
