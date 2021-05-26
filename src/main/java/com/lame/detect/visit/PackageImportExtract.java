package com.lame.detect.visit;

import com.lame.sbconstant.utils.Antlr4Utils;
import core.analy.Java8Parser;
import core.analy.Java8ParserBaseVisitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PackageImportExtract  extends Java8ParserBaseVisitor<Void> {
    @Getter List<String> detectImport = new ArrayList<>();
    @Getter String detectPackage = "";
    @Override
    public Void visitPackageName(Java8Parser.PackageNameContext ctx) {
        detectPackage = Antlr4Utils.getFullText(ctx);
        return null;
    }

    @Override
    public Void visitImportDeclaration(Java8Parser.ImportDeclarationContext ctx) {
        detectImport.add(Antlr4Utils.getFullText(ctx));
        return null;
    }

}
