package com.lame.detect.strategy;

import com.lame.detect.DetectStrategy;
import com.lame.detect.visit.APIExtract;
import com.lame.detect.visit.EntityFieldExtract;
import com.lame.detect.vo.ApiClassMeta;
import com.lame.detect.vo.ClassMeta;
import core.analy.Java8Lexer;
import core.analy.Java8Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * 控制器扫描
 * **/
public class APIDetectStrategy implements DetectStrategy {

    @Override
    public ClassMeta detect(ParseTree parseTree) {
        APIExtract extract = new APIExtract();
        extract.visit(parseTree);
        ApiClassMeta apiClassMeta = extract.getApiClassMeta();
        return apiClassMeta;
    }

    public static void main(String[] args) {
        try {
            String f = "/media/lame/0DD80F300DD80F30/koal/kcsp-admin/kcsp-boot/kcsp-boot-module-system/src/main/java/kl/kcsp/modules/customer/controller/CustomerAppController.java";
            Lexer lexer = new Java8Lexer(CharStreams.fromFileName(f));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Java8Parser parser = new Java8Parser(tokens);
            Java8Parser.CompilationUnitContext tree = parser.compilationUnit();
            APIDetectStrategy detect = new APIDetectStrategy();
            ApiClassMeta apiClassMeta = (ApiClassMeta)detect.detect(tree);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
