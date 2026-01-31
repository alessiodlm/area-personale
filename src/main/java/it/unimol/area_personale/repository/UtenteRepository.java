package it.unimol.area_personale.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.unimol.area_personale.model.Utente;

public interface UtenteRepository extends MongoRepository<Utente, String> {
}
