package org.example.safetyconnection.QrcodeGenerator.Repository;

import org.example.safetyconnection.QrcodeGenerator.domain.QrGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrGeneratorRepository extends JpaRepository<QrGenerator, String> {
  QrGenerator findByUsername(String username);
  Optional<QrGenerator> findByUid(String uid);
}
