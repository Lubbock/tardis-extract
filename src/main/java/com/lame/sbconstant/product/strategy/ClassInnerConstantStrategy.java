package com.lame.sbconstant.product.strategy;

import com.lame.detect.vo.ClassMeta;
import com.lame.sbconstant.product.ProductStrategy;
import com.lame.sbconstant.product.visit.MethodFieldVisit;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ClassInnerConstantStrategy implements ProductStrategy {
    @Override
    public String product(Parser parser, ParseTree parseTree, ClassMeta classMeta, String fp) {
        TokenStream tokenStream = parser.getTokenStream();

        MethodFieldVisit lineFuck = new MethodFieldVisit(tokenStream, classMeta);
        lineFuck.visit(parseTree);
        String text = lineFuck.getRewriter().getText();
        System.out.println(text);
        return text;
    }
}
