package com.lame.sbconstant.command;

import com.lame.detect.AIDetect;
import com.lame.detect.ConstantDetectFactory;
import com.lame.detect.DetectContext;
import com.lame.detect.vo.ClassMeta;
import com.lame.detect.vo.FileType;
import com.lame.sbconstant.product.ProductContext;
import com.lame.sbconstant.product.ProductFactory;
import core.analy.Java8Lexer;
import core.analy.Java8Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 1. 查找类，实体变量实现QueryWrapper替换,如果一般类，自动生成实体常量，然后对应替换
 **/
public class SimCommand implements Command{

    @Override
    public  void execute(Context ctx,String f) throws Exception {
        try {
            Lexer lexer = new Java8Lexer(CharStreams.fromFileName(f));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Java8Parser parser = new Java8Parser(tokens);
            Java8Parser.CompilationUnitContext tree = parser.compilationUnit();
            FileType fileType = FileType.COMMON;
            if (ctx.aiDetect) {
                AIDetect aiDetect = new AIDetect();
                fileType = aiDetect.visit(tree);
            }
            DetectContext detectContext = ConstantDetectFactory.getDetectContext(fileType);
            ClassMeta detectMeta = detectContext.detect(tree, f);
            ProductContext productContext = ProductFactory.getProductContext(fileType);
            String product = productContext.product(parser, tree, detectMeta, f);
            if (ctx.saveFile) {
                File detectFile = new File(f);
                File file = new File(ctx.output, detectFile.getName());
                FileUtils.writeStringToFile(file, product, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        SimCommand simCommand = new SimCommand();
        simCommand.execute(Context.Default(),"/media/lame/0DD80F300DD80F30/koal/kcsp-admin/kcsp-boot/kcsp-boot-module-system/src/main/java/kl/kcsp/modules/customer/controller/CustomerAppController.java");
    }

}
