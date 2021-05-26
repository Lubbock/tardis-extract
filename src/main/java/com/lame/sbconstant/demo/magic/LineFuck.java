package com.lame.sbconstant.demo.magic;

import core.analy.Java8Parser;
import core.analy.Java8ParserBaseListener;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;

import java.util.HashMap;
import java.util.Map;

public class LineFuck extends Java8ParserBaseListener implements FuckAlibaba {
    TokenStreamRewriter rewriter;
    Map<String, String> kv = new HashMap<>();

    public LineFuck(TokenStream tokens, Map<String, String> kv) {
        rewriter = new TokenStreamRewriter(tokens);
        this.kv = kv;
    }

    public void enterClassBody(Java8Parser.ClassBodyContext ctx) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : kv.entrySet()) {
            sb.append(fuck(entry.getKey(), entry.getValue()));
        }
        rewriter.insertAfter(ctx.start, sb.toString());
    }

    @Override
    public void enterMethodBody(Java8Parser.MethodBodyContext ctx) {
        MagicVisitor visitor = new MagicVisitor(rewriter);
        visitor.visit(ctx);
    }

    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
//        MagicVisitor visitor = new MagicVisitor(rewriter);
//        visitor.visit(ctx);
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

    @Override
    public String fuck(String key, String val) {
        return String.format("\n\tpublic static final String %s = %s;", key, val);
    }
}
