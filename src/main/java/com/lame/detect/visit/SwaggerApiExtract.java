package com.lame.detect.visit;

import com.google.common.collect.Lists;
import com.lame.sbconstant.utils.Antlr4Utils;
import core.analy.Java8Parser;
import core.analy.Java8ParserBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class SwaggerApiExtract extends Java8ParserBaseVisitor<String> {

    @Override
    public String visitNormalAnnotation(Java8Parser.NormalAnnotationContext ctx) {

        ArrayList<String> apiOperation = Lists.newArrayList("");
        Java8Parser.TypeNameContext typeNameContext = ctx.typeName();
        ArrayList<String> accept = Lists.newArrayList("ApiOperation");
        if (typeNameContext != null) {
            String fullText = Antlr4Utils.getFullText(typeNameContext);
            if (StringUtils.equalsAny(fullText, accept.toArray(new String[0]))) {
                Java8Parser.ElementValuePairListContext elementValuePairListContext = ctx.elementValuePairList();
                if (elementValuePairListContext != null) {
                    Java8Parser.ElementValuePairContext elementValuePairContext = elementValuePairListContext.elementValuePair(0);
                    if (elementValuePairContext != null) {
                        TerminalNode identifier = elementValuePairContext.Identifier();
                        if (identifier != null) {
                            if (
                                    identifier.getText().equals("value")
                                            || identifier.getText().equals("notes")
                            ) {
                                String note = elementValuePairContext.elementValue().getText();
                                return note.substring(1, note.length() - 1);
                            }
                        }
                    }
                }
            }
        }
        return "";
    }
}
