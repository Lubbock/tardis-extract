package com.lame.detect.visit;

import com.lame.detect.vo.ClassField;
import com.lame.detect.vo.ClassMeta;
import com.lame.detect.vo.LineExtraMeta;
import core.analy.Java8Parser;
import core.analy.Java8ParserBaseVisitor;
import lombok.Getter;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldStatementVisit extends Java8ParserBaseVisitor<Void> {
    @Getter
    List<LineExtraMeta> kv = new ArrayList<>();

    TokenStreamRewriter rewriter;

    ClassMeta correlation;

    public FieldStatementVisit() {
    }

    public FieldStatementVisit(TokenStreamRewriter rewriter, ClassMeta correlation) {
        this.correlation = correlation;
        this.rewriter = rewriter;
    }

    public static boolean isIncludeZhCn(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

        Matcher m = p.matcher(str);

        if (m.find()) {
            return true;
        }
        return false;
    }

    @Override
    public Void visitLiteral(Java8Parser.LiteralContext ctx) {
        TerminalNode stringLiteral = ctx.StringLiteral();
        TerminalNode integerLiteral = ctx.IntegerLiteral();
        TerminalNode booleanLiteral = ctx.BooleanLiteral();
        TerminalNode characterLiteral = ctx.CharacterLiteral();
        TerminalNode floatingPointLiteral = ctx.FloatingPointLiteral();
        LineExtraMeta lineExtraMeta = null;
        String name = "";
        String text = "";
        if (stringLiteral != null) {
            text = ctx.StringLiteral().getText();
            if (isIncludeZhCn(text)) {
                return null;
            }
            if (text.isEmpty()) {
                return null;
            }
            if (text.contains("-")) {
                return null;
            }
            if (text.contains(",")) {
                return null;
            }
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            name = "STR_" + s.substring(1, s.length() - 1);
            lineExtraMeta = new LineExtraMeta("String " + name, text);
            kv.add(lineExtraMeta);
        } else if (integerLiteral != null) {
            text = ctx.IntegerLiteral().getText();
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            name = "INT_" + s;
            lineExtraMeta = new LineExtraMeta("Integer " + name, text);
            kv.add(lineExtraMeta);
        } else if (booleanLiteral != null) {
            text = ctx.BooleanLiteral().getText();
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            name = "BOOL_" + s;
            lineExtraMeta = new LineExtraMeta("Boolean " + name, text);
            kv.add(lineExtraMeta);
        } else if (characterLiteral != null) {
            text = ctx.CharacterLiteral().getText();
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            name = "CHAR_" + s.substring(1, s.length() - 1);
            lineExtraMeta = new LineExtraMeta("String " + name, text);
            kv.add(lineExtraMeta);
        } else if (floatingPointLiteral != null) {
            text = ctx.FloatingPointLiteral().getText();
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            name = "FLOAT_" + s;
            lineExtraMeta = new LineExtraMeta("float " + name, text);
            kv.add(lineExtraMeta);
        }
        if (lineExtraMeta != null && rewriter != null) {
            if (correlation.getCorrelation() != null) {
                for (ClassField field : correlation.getCorrelation().getFields()) {
                    if (StringUtils.equals(field.getHumpName(), text.substring(1, text.length() - 1))) {
                        name = correlation.getCorrelation().getName() + "." + field.getUpHumpName();
                    }
                }
            };
            rewriter.replace(ctx.start, name);
        }
        return null;
    }
}
