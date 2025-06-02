package org.example.safetyconnection.companion.repository;

import org.example.safetyconnection.companion.entity.Companion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanionRepository extends JpaRepository<Companion, Long> {
    Companion findById(long userId);
}
