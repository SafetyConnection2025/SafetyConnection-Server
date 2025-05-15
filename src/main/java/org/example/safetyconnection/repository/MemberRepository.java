package org.example.safetyconnection.repository;

import org.example.safetyconnection.entity.Companion;
import org.example.safetyconnection.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
        Member findByUserId(long userId);
        void deleteById(long userId);

        Optional<Member> findByUsername(String username);  // Ensure 'name' matches the field in Users entity

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO companion (USER_ID, COMP_USER_ID) VALUES (:userId, :compUserID)", nativeQuery = true)
        void addCompanion(@Param("userId") Long userId,
                          @Param("compUserID") Long compUserID);

        @Query(value = "SELECT CONTACT_COUNT FROM companion WHERE USER_ID = :userId", nativeQuery = true)
        Integer findContactCountByUserIdAndCompUserName(@Param("userId") Long userId);

        @Query(value = "SELECT * FROM companion WHERE user_id = :userId", nativeQuery = true)
        Optional<List<Companion>> findAllCompanionsByUserId(@Param("userId") Long userId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM companion WHERE user_id = :userId AND comp_user_id = :companionId", nativeQuery = true)
        void deleteCompanionByUserIdAndCompanionId(@Param("userId") Long userId, @Param("companionId") Long companionId);

}
