package com.lame.sbconstant.product.visit;

import com.lame.detect.visit.FieldStatementVisit;
import com.lame.detect.vo.ClassField;
import com.lame.detect.vo.ClassMeta;
import com.lame.detect.vo.LineExtraMeta;
import core.analy.Java8Parser;
import core.analy.Java8ParserBaseVisitor;
import lombok.Getter;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodFieldVisit extends Java8ParserBaseVisitor<Void> {
    @Getter
    FieldStatementVisit fieldStatementVisit;
    @Getter
    TokenStreamRewriter rewriter;

    ClassMeta correlation;

    public MethodFieldVisit(TokenStream tokens,ClassMeta correlation) {
        this.rewriter  =new TokenStreamRewriter(tokens);
        this.fieldStatementVisit = new FieldStatementVisit(rewriter, correlation);
        this.correlation = correlation;
    }

    public String fuck(String key, String val) {
        return String.format("\n\tpublic static final %s = %s;", key, val);
    }
    @Override
    public Void visitClassBody(Java8Parser.ClassBodyContext ctx) {
        StringBuilder sb = new StringBuilder();
        List<LineExtraMeta> lineExtraMetas = correlation.getLineExtraMetas();

        ClassMeta correlation = this.correlation.getCorrelation();
        Set<String> filter = new HashSet<>();
        if (correlation != null) {
            for (ClassField field : correlation.getFields()) {
                filter.add(field.getHumpName());
            }
        }
        for (LineExtraMeta lineExtraMeta : lineExtraMetas) {
            if (filter.contains(lineExtraMeta.getValue())) {
                continue;
            }
            sb.append(fuck(lineExtraMeta.getVariableName(), lineExtraMeta.getValue()));
        }
        rewriter.insertAfter(ctx.start, sb.toString());
        return super.visitClassBody(ctx);
    }

    @Override
    public Void visitMethodInvocation(Java8Parser.MethodInvocationContext ctx) {
        String text = ctx.getText();
        String[] filterMethod = new String[]{
                "log.info(", "log.error(", "Result.error",
                "setMessage","error500","error400",
                "Result.ok(", "System.out.println","errorMessage.add"
        };
        boolean ignore = false;
        for (String s : filterMethod) {
            if (text.contains(s)) {
                ignore = true;
                break;
            }
        }
        if (!ignore) {
            fieldStatementVisit.visit(ctx);
        }
        return super.visitMethodInvocation(ctx);
    }

    @Override
    public Void visitLocalVariableDeclarationStatement(Java8Parser.LocalVariableDeclarationStatementContext ctx) {
        fieldStatementVisit.visit(ctx);
        return super.visitLocalVariableDeclarationStatement(ctx);
    }
}
