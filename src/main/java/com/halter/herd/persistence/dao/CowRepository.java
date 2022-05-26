package com.halter.herd.persistence.dao;

import com.halter.herd.persistence.model.Cow;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CowRepository extends JpaRepository<Cow, UUID> {

  Cow findOneById(UUID id);
}
