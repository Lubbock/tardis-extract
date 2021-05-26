package com.lame.sbconstant.product;

import com.lame.detect.vo.ClassMeta;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

public interface ProductStrategy {
    String product(Parser parser, ParseTree parseTree, ClassMeta classMeta, String fp);
}
