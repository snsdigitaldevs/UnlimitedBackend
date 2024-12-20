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
	public static final int RESULT_COULD_NOT_AUTHENTICATE = -520;
	public static final int RESULT_GENERAL_ERROR = -1;
	public static final int RESULT_USER_ID_ALREADY_EXISTS = -1043;
	public static final int RESULT_RECORD_NOT_FOUND = -201;
	public static final int RESULT_APP_STORE_ERROR = -9302;
	public static final int RESULT_APPSTORE_RECEIPT_DATA_MISSING = -9315;
	public static final int RESULT_APPSTORE_VALIDATION_ERROR_RECEIPT_TOO_OLD = -9314;

	@JsonProperty("result_code")
	private Integer resultCode;

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
}
