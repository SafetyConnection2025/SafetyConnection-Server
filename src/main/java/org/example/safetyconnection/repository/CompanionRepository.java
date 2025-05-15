package org.example.safetyconnection.repository;

import org.example.safetyconnection.entity.Companion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanionRepository extends JpaRepository<Companion, Long> {
    Companion findById(long userId);
}
