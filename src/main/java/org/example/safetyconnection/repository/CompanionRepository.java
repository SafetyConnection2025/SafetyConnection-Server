package org.example.safetyconnection.repository;

import java.util.List;
import java.util.Optional;

import org.example.safetyconnection.entity.Companion;
import org.example.safetyconnection.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanionRepository extends JpaRepository<Companion, Long> {
    Companion findById(long userId);
    Optional<Companion> findByMemberAndCompanion(Member member, Member companion);

    List<Companion> findByMemberOrderByRecentRequestDateTimeDesc(Member member);
}
