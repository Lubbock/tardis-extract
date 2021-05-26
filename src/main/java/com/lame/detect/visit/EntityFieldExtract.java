package com.lame.detect.visit;

import com.lame.sbconstant.utils.Antlr4Utils;
import core.analy.Java8Parser;
import core.analy.Java8ParserBaseVisitor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityFieldExtract extends Java8ParserBaseVisitor<String> {

    List<String> typeName = new ArrayList<>();

    @Getter String packageName = "";

    @Override
    public String visitPackageDeclaration(Java8Parser.PackageDeclarationContext ctx) {
        packageName = Antlr4Utils.getFullText(ctx);
        return super.visitPackageDeclaration(ctx);
    }

    @Override
    public String visitFieldDeclaration(Java8Parser.FieldDeclarationContext ctx) {
        Java8Parser.UnannTypeContext unannTypeContext = ctx.unannType();
        Java8Parser.VariableDeclaratorListContext variableDeclaratorListContext = ctx.variableDeclaratorList();
        String variable = Antlr4Utils.getFullText(variableDeclaratorListContext);
        if (variable.contains("=")) {
            variable = StringUtils.substringBefore(variable, "=").trim();
        }
        return Antlr4Utils.getFullText(unannTypeContext) + " " + variable;
    }

    @Override
    public String visitClassBodyDeclaration(Java8Parser.ClassBodyDeclarationContext ctx) {
        String visit = visit(ctx.classMemberDeclaration());
        typeName.add(visit);
        return "";
    }


    public List<String> detectInfo() {
        return typeName;
    }
}
