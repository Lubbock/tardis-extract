package com.lame.detect.visit;

import core.analy.Java8Parser;
import core.analy.Java8ParserBaseVisitor;
import lombok.Getter;

public class MethodFieldExtract extends Java8ParserBaseVisitor<Void> {

    @Getter
    FieldStatementVisit fieldStatementVisit = new FieldStatementVisit();

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
