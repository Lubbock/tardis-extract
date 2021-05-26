package com.lame.detect;

import com.lame.detect.strategy.MethodFieldDetectStrategy;
import com.lame.detect.strategy.EntityDetectStrategy;
import com.lame.detect.vo.FileType;

/**
 * 常量工厂
 **/
public class ConstantDetectFactory {

    public static DetectContext getDetectContext(FileType fileType) {
        DetectContext detectContext = null;
        switch (fileType) {
            case ENTITY:
                detectContext = new DetectContext(new EntityDetectStrategy());
                break;
            case DAO:
            case SERVICE:
            case COMMON:
            default:
                detectContext = new DetectContext(new MethodFieldDetectStrategy());
                break;
        }
        return detectContext;
    }
}
