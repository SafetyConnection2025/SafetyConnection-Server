package org.example.safetyconnection.QrcodeGenerator.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class QrGenerator {
  @Id
  String uid;

  String username;

  public QrGenerator(String username) {
    this.uid = UUID.randomUUID().toString();
    this.username = username;
  }
}
