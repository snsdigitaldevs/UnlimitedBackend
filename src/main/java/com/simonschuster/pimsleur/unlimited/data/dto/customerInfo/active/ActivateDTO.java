package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active;

import java.util.List;

public class ActivateDTO {
  private   List<ActivateResultDTO> activateResultDTOS;

  public ActivateDTO(List<ActivateResultDTO> activateResultDTOS) {
    this.activateResultDTOS = activateResultDTOS;
  }

    public List<ActivateResultDTO> getActivateResultDTOS() {
        return activateResultDTOS;
    }
}
