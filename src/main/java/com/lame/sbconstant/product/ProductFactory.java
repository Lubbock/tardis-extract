package com.lame.sbconstant.product;

import com.lame.detect.vo.FileType;
import com.lame.sbconstant.product.strategy.ClassInnerConstantStrategy;
import com.lame.sbconstant.product.strategy.SingleClassConstantStrategy;

/**
 * 常量工厂
 **/
public class ProductFactory {

    public static ProductContext getProductContext(FileType fileType) {
        ProductContext productContext = null;
        switch (fileType) {
            case ENTITY:
                productContext = new ProductContext(new SingleClassConstantStrategy());
                break;
            case DAO:
            case SERVICE:
            case COMMON:
            default:
                productContext = new ProductContext(new ClassInnerConstantStrategy());
                break;
        }
        return productContext;
    }
}
