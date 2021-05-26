package com.lame.sbconstant.demo;

import core.analy.Java8Lexer;
import core.analy.Java8Parser;
import core.analy.Java8ParserBaseListener;
import com.lame.sbconstant.demo.magic.LineFuck;
import com.lame.sbconstant.demo.magic.MagicVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.HashMap;
import java.util.Map;

public class MagicJavaCopy {

    public static class JavaEmitter extends Java8ParserBaseListener{
        TokenStreamRewriter rewriter;
        Map<String, String> kv = new HashMap<>();

        public JavaEmitter(TokenStream tokens) {
            rewriter = new TokenStreamRewriter(tokens);
        }

        @Override
        public void enterNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
//            System.out.println("interface I" + ctx.Identifier() + " {");
        }

        @Override
        public void enterMethodBody(Java8Parser.MethodBodyContext ctx) {
            MagicVisitor visitor = new MagicVisitor(rewriter);
            visitor.visit(ctx);
            kv.putAll(visitor.getKv());
        }

        @Override
        public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
//            TokenStream tokens = parser.getTokenStream();
//            String type = "void";
//            System.out.println(ctx.methodModifier(0).getText() + "\t" + ctx.methodHeader().getText() + "{");
//            System.out.println(ctx.methodBody().block().blockStatements().getText());
//            MagicVisitor visitor = new MagicVisitor(rewriter);
//            visitor.visit(ctx);
//            kv.putAll(visitor.getKv());
        }

        @Override
        public void exitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
//            System.out.println("}");
        }

        @Override
        public void exitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
//            System.out.println("}");
        }
    }

    public static class InsertAfter extends Java8ParserBaseListener {

        TokenStreamRewriter rewriter;

        public InsertAfter(TokenStream tokens) {
            rewriter = new TokenStreamRewriter(tokens);
        }
        @Override
        public void enterClassBody(Java8Parser.ClassBodyContext ctx) {
            String field = "\n public static final long serialVersionUID=1L\n";
            rewriter.insertAfter(ctx.start, field);
        }
    }

    public static void main(String[] args) {
        String f = "/media/lame/0DD80F300DD80F30/koal/kcsp-admin/kcsp-boot/kcsp-boot-module-system/src/main/java/kl/kcsp/modules/system/controller/SysUserController.java";
        try {
            Lexer lexer = new Java8Lexer(CharStreams.fromFileName(f));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Java8Parser parser = new Java8Parser(tokens);
//            parser.setBuildParseTree(true);
            Java8Parser.CompilationUnitContext tree = parser.compilationUnit();

//            MagicParserTreeListener extractor = new MagicParserTreeListener();
//            ParseTreeWalker.DEFAULT.walk(extractor, tree);
            JavaEmitter convert = new JavaEmitter(parser.getTokenStream());
            ParseTreeWalker.DEFAULT.walk(convert, tree);
            Map<String, String> kv = convert.kv;
            LineFuck lineFuck = new LineFuck(parser.getTokenStream(), kv);
            ParseTreeWalker.DEFAULT.walk(lineFuck,tree);
//            InsertAfter insertAfter = new InsertAfter(parser.getTokenStream());
//            ParseTreeWalker.DEFAULT.walk(insertAfter, tree);
            System.out.println(lineFuck.getRewriter().getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
