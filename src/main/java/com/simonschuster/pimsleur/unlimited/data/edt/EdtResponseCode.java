package com.simonschuster.pimsleur.unlimited.data.edt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result_code"
})
public class EdtResponseCode {

  public static final int NO_RESULT = 0;
  public static final int RESULT_OK = 1;
  public static final int RESULT_GENERAL_ERROR = -1;
  public static final int RESULT_USER_ID_ALREADY_EXISTS = -1043;

  @JsonProperty("result_code")
  private Integer resultCode;

  public Integer getResultCode() {
    return resultCode;
  }

  public void setResultCode(Integer resultCode) {
    this.resultCode = resultCode;
  }
}
