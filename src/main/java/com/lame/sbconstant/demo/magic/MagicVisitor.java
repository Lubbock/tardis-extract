package com.lame.sbconstant.demo.magic;

import core.analy.Java8Parser;
import core.analy.Java8ParserBaseVisitor;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MagicVisitor extends Java8ParserBaseVisitor<Void> {

    TokenStreamRewriter rewriter;

    Map<String, String> kv = new HashMap<>();

    public MagicVisitor(TokenStreamRewriter rewriter) {
        this.rewriter = rewriter;
    }

    @Override
    public Void visitLiteral(Java8Parser.LiteralContext ctx) {
        TerminalNode stringLiteral = ctx.StringLiteral();
        TerminalNode integerLiteral = ctx.IntegerLiteral();
        TerminalNode booleanLiteral = ctx.BooleanLiteral();
        TerminalNode characterLiteral = ctx.CharacterLiteral();
        TerminalNode floatingPointLiteral = ctx.FloatingPointLiteral();
        if (stringLiteral != null) {
            String text = ctx.StringLiteral().getText();
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            String name = "STR_" + s.substring(1, s.length() - 1);
            kv.put(name, text);
            rewriter.replace(ctx.start, name);
        } else if (integerLiteral != null) {
            String text = ctx.IntegerLiteral().getText();
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            String name = "INT_" + s;
            kv.put(name, text);
            rewriter.replace(ctx.start, name);
        } else if (booleanLiteral != null) {
            String text = ctx.BooleanLiteral().getText();
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            String name = "BOOL_" + s;
            kv.put(name, text);
            rewriter.replace(ctx.start, name);
        } else if (characterLiteral != null) {
            String text = ctx.CharacterLiteral().getText();
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            String name = "CHAR_" + s.substring(1, s.length() - 1);
            kv.put(name, text);
            rewriter.replace(ctx.start, name);
        } else if (floatingPointLiteral != null) {
            String text = ctx.FloatingPointLiteral().getText();
            String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
            String name = "FLOAT_" +s;
            kv.put(name, text);
            rewriter.replace(ctx.start, name);
        }
        return null;
    }

    public TokenStreamRewriter getRewriter() {
        return rewriter;
    }

    public void setRewriter(TokenStreamRewriter rewriter) {
        this.rewriter = rewriter;
    }

    public Map<String, String> getKv() {
        return kv;
    }

    public void setKv(Map<String, String> kv) {
        this.kv = kv;
    }
}
