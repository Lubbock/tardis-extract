package com.lame.sbconstant.utils;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;

public class Antlr4Utils {
    public static String getFullText(ParseTree tree) {
        ParserRuleContext context = (ParserRuleContext) tree;
        if (context.children == null) {
            return "";
        }
        Token startToken = context.start;
        Token stopToken = context.stop;
        Interval interval = new Interval(startToken.getStartIndex(), stopToken.getStopIndex());
        return context.start.getInputStream().getText(interval);
    }
}
