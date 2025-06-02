package org.example.safetyconnection.companion.dto.response;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public record CompanionListResDTO(ArrayList<CompanionResDTO> companionResDTOs) implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  public CompanionListResDTO(ArrayList<CompanionResDTO> companionResDTOs) {
    this.companionResDTOs = companionResDTOs;
  }

}
