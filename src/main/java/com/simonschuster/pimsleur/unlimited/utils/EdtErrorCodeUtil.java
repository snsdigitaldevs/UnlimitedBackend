package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;

import java.util.HashMap;
import java.util.Map;

public class EdtErrorCodeUtil {
    private static Map<Integer, String> errorMap = new HashMap<Integer, String>() {
        {
            // In App purchase
            put(-9301, "app store unavailable");
            put(-9302, "app store error");
            put(-9303, "app store validation error");
            put(-9304, "app store transaction pending");
            put(-9305, "app store transaction success with pending event");
            put(-9306, "app store receipt invalid");
            put(-1039, "user already purchased product");
        }
    };

    public static void throwError(Integer errorCode, String defaultErrorMassage) {
        String errorMassage = errorMap.get(errorCode);
        if (errorMassage == null) {
            throw new PimsleurException(defaultErrorMassage);
        } else {
            throw new ParamInvalidException(errorMassage);
        }
    }
}
