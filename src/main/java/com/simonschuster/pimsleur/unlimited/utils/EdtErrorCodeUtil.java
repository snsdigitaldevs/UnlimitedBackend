package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;

import java.util.HashMap;
import java.util.Map;

public class EdtErrorCodeUtil {

    private static Map<Integer, String> errorMap = new HashMap<>();

    static {
        // In App purchase
        errorMap.put(-1039, "user already purchased product");
        errorMap.put(-9301, "app store http error or empty http response");
        errorMap.put(-9302, "unexpected backend processing exception");
        errorMap.put(-9303,
            "app store reported an error while processing the receipt validation request(internal app store error)");
        errorMap.put(-9304, "app store transaction pending");
        errorMap.put(-9305, "app store transaction success with pending event");
        errorMap.put(-9306,
            "app store could not validate the receipt");// (i.e. possibly wrong receipt or forged)
        errorMap.put(-9312, "app store rejected the receipt data format or structure");
        errorMap.put(-9315, "bad secret key or receipt is invalid for prod/sandbox config");
        errorMap.put(-9316,
            "failed to parse the response from app store receipt validation");// (i.e. we couldn’t parse the response data as expected)
        errorMap.put(-101,
            "missing required param in api request");// (i.e. if receipt param is missing in the api request)

    }

    public static void throwError(Integer errorCode, String defaultErrorMassage) {
        String errorMassage = errorMap.get(errorCode);
        if (errorMassage == null) {
            throw new PimsleurException(defaultErrorMassage);
        } else {
            throw new ParamInvalidException(errorMassage);
        }
    }
}
