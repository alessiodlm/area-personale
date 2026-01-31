package it.unimol.area_personale.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.unimol.area_personale.model.Notifica;

public interface NotificaRepository extends MongoRepository<Notifica, String> {
    List<Notifica> findByUserIdOrderByTimestampDesc(String userId);
}
