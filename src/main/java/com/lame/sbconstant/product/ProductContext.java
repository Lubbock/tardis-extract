package com.lame.sbconstant.product;

import com.lame.detect.vo.ClassMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

@Data
@AllArgsConstructor
public class ProductContext {
    private ProductStrategy productStrategy;

    public String product(Parser parser, ParseTree parseTree, ClassMeta classMeta, String fp) {
        return productStrategy.product(parser, parseTree, classMeta, fp);
    }
}
