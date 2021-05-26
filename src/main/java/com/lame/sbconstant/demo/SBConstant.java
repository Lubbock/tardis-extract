package com.lame.sbconstant.demo;

import core.analy.Java8Lexer;
import core.analy.Java8Parser;
import core.analy.Java8ParserBaseListener;
import core.analy.Java8ParserBaseVisitor;
import com.lame.sbconstant.demo.magic.FuckAlibaba;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SBConstant {

    public static class ScanFuck extends Java8ParserBaseListener{
        TokenStreamRewriter rewriter;
        Map<String, String> kv = new HashMap<>();

        public ScanFuck(TokenStream tokens) {
            rewriter = new TokenStreamRewriter(tokens);
        }

        @Override
        public void enterMethodBody(Java8Parser.MethodBodyContext ctx) {
            DeclarationStatement declarationStatement = new DeclarationStatement(rewriter, kv);
            declarationStatement.visit(ctx);
            MethodInvokeStatement methodInvokeStatement = new MethodInvokeStatement(rewriter, kv);
            methodInvokeStatement.visit(ctx);
        }


        public TokenStreamRewriter getRewriter() {
            return rewriter;
        }


        public Map<String, String> getKv() {
            return kv;
        }

    }
    public static boolean isIncludeZhCn(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

        Matcher m = p.matcher(str);

        if (m.find()) {
            return true;

        }

        return false;

    }


    public static class InvokeStatement extends  Java8ParserBaseVisitor<Void>{

        TokenStreamRewriter rewriter;

        Map<String, String> kv = new HashMap<>();

        public InvokeStatement(TokenStreamRewriter rewriter, Map<String, String> kv) {
            this.rewriter = rewriter;
            this.kv = kv;
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
                if (isIncludeZhCn(text)) {
                    return null;
                }
                if (text.isEmpty()) {
                    return null;
                }
                if (text.contains("-")) {
                    return null;
                }
                String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
                String name = "STR_" + s.substring(1, s.length() - 1);
                kv.put("String " + name, text);
                rewriter.replace(ctx.start, name);
            } else if (integerLiteral != null) {
                String text = ctx.IntegerLiteral().getText();
                String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
                String name = "INT_" + s;
                kv.put("Integer " + name, text);
                rewriter.replace(ctx.start, name);
            } else if (booleanLiteral != null) {
                String text = ctx.BooleanLiteral().getText();
                String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
                String name = "BOOL_" + s;
                kv.put("boolean " + name, text);
                rewriter.replace(ctx.start, name);
            } else if (characterLiteral != null) {
                String text = ctx.CharacterLiteral().getText();
                String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
                String name = "CHAR_" + s.substring(1, s.length() - 1);
                kv.put("String " + name, text);
                rewriter.replace(ctx.start, name);
            } else if (floatingPointLiteral != null) {
                String text = ctx.FloatingPointLiteral().getText();
                String s = text.toUpperCase(Locale.ROOT).toUpperCase(Locale.ROOT);
                String name = "FLOAT_" +s;
                kv.put("float " + name, text);
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

    public static class DeclarationStatement extends Java8ParserBaseVisitor<Void> {

        TokenStreamRewriter rewriter;

        Map<String, String> kv;

        InvokeStatement invokeStatement;

        public DeclarationStatement(TokenStreamRewriter rewriter, Map<String, String> kv) {
            this.rewriter = rewriter;
            this.kv = kv;
            this.invokeStatement = new InvokeStatement(rewriter, kv);
        }


        @Override
        public Void visitLocalVariableDeclarationStatement(Java8Parser.LocalVariableDeclarationStatementContext ctx) {
            invokeStatement.visit(ctx);
            return null;
        }
    }

    public static class MethodInvokeStatement extends Java8ParserBaseVisitor<Void> {

        TokenStreamRewriter rewriter;

        Map<String, String> kv;

        InvokeStatement invokeStatement;

        public MethodInvokeStatement(TokenStreamRewriter rewriter, Map<String, String> kv) {
            this.rewriter = rewriter;
            this.kv = kv;
            this.invokeStatement = new InvokeStatement(rewriter, kv);
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
                invokeStatement.visit(ctx);
            }
            return null;
        }
    }

    public static class LineFuck extends Java8ParserBaseListener implements FuckAlibaba{
        TokenStreamRewriter rewriter;
        Map<String, String> kv;

        public LineFuck(TokenStream tokens, Map<String, String> kv) {
            this.rewriter = new TokenStreamRewriter(tokens);
            this.kv = kv;
        }

        public void enterClassBody(Java8Parser.ClassBodyContext ctx) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : kv.entrySet()) {
                sb.append(fuck(entry.getKey(), entry.getValue()));
            }
            rewriter.insertAfter(ctx.start, sb.toString());
        }

        public void enterMethodBody(Java8Parser.MethodBodyContext ctx) {
            DeclarationStatement declarationStatement = new DeclarationStatement(rewriter, kv);
            declarationStatement.visit(ctx);
            MethodInvokeStatement methodInvokeStatement = new MethodInvokeStatement(rewriter, kv);
            methodInvokeStatement.visit(ctx);
        }


        public TokenStreamRewriter getRewriter() {
            return rewriter;
        }


        public Map<String, String> getKv() {
            return kv;
        }

        @Override
        public String fuck(String key, String val) {
            return String.format("\n\tpublic static final %s = %s;", key, val);
        }
    }

    public static void main(String[] args) {
        String info = args[0];
        if (!"fuckAlibaba".equals(info)) {
            System.out.println("输入暗号");
        }
        String fp = args[1];
        File f = new File(fp);
        if (!f.exists()) {
            System.out.println("文件不存在");
        }
        String fout = args[2];
        if ("/".equals(fout)) {
            fout += "./";
        }
        try {
            Lexer lexer = new Java8Lexer(CharStreams.fromFileName(fp));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Java8Parser parser = new Java8Parser(tokens);
            Java8Parser.CompilationUnitContext tree = parser.compilationUnit();
            ScanFuck convert = new ScanFuck(parser.getTokenStream());
            ParseTreeWalker.DEFAULT.walk(convert, tree);
            LineFuck lineFuck = new LineFuck(parser.getTokenStream(), convert.kv);
            ParseTreeWalker.DEFAULT.walk(lineFuck,tree);
            System.out.println(lineFuck.getRewriter().getText());
            try (
                    FileInputStream fis = new FileInputStream(fp);
                    FileOutputStream fos = new FileOutputStream(fout)
            ) {
                byte[] cache = new byte[1024];
                while (fis.read(cache) > -1) {
                    fos.write(cache);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
