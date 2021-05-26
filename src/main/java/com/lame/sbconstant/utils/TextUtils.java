package com.lame.sbconstant.utils;

import com.google.common.base.CaseFormat;
import lombok.extern.java.Log;

@Log
public class TextUtils {
    public static String convertLineToHumpTest(String fieldName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }

    public static String convertHumpToLine(String fieldName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName);
    }
}
