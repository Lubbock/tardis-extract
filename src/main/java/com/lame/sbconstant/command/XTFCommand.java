package com.lame.sbconstant.command;

import com.lame.detect.ConstantDetectFactory;
import com.lame.detect.DetectContext;
import com.lame.detect.strategy.MethodFieldDetectStrategy;
import com.lame.detect.vo.ClassMeta;
import com.lame.detect.vo.FileType;
import com.lame.sbconstant.product.ProductContext;
import com.lame.sbconstant.product.ProductFactory;
import com.lame.sbconstant.product.strategy.ClassInnerConstantStrategy;
import core.analy.Java8Lexer;
import core.analy.Java8Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 1. 查找类，实体变量实现QueryWrapper替换,如果一般类，自动生成实体常量，然后对应替换
 **/
public class XTFCommand implements Command{

    private Context ctx;



    public ClassMeta extractEntity(String entityPath) throws Exception {
        Lexer lexer = new Java8Lexer(CharStreams.fromFileName(entityPath));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        Java8Parser.CompilationUnitContext tree = parser.compilationUnit();
        DetectContext detectContext = ConstantDetectFactory.getDetectContext(FileType.ENTITY);
        ClassMeta detectMeta = detectContext.detect(tree, entityPath);
        ProductContext productContext = ProductFactory.getProductContext(FileType.ENTITY);
        String product = productContext.product(parser, tree, detectMeta, entityPath);
        System.out.println(product);
        return detectMeta;
    }

    public void execute(Context ctx,String f) throws Exception {
        File claz = new File(f);
        String name = claz.getName();
        String baseScan = claz.getParentFile().getParentFile().getAbsolutePath();
        String ft = claz.getParentFile().getName();
        FileType fileType = FileType.COMMON;
        String entityName = "";
        switch (ft) {
            case "controller":
                fileType = FileType.CONTROLLER;
                entityName = StringUtils.substringBeforeLast(name, "Controller");
                break;
            case "service":
                fileType = FileType.SERVICE;
                entityName = StringUtils.substringBeforeLast(name, "Service");
                break;
            default:
                throw new IllegalAccessException("不支持文件类型");
        }
        String entityFile = baseScan + File.separator + "entity" + File.separator + entityName + ".java";
        ClassMeta correlation = extractEntity(entityFile);
        correlation.setName(entityName + "Constant");
//        correlation.setPackageName(baseScan + File.separator + "entity." + entityName + ";");
        Lexer lexer = new Java8Lexer(CharStreams.fromFileName(f));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        Java8Parser.CompilationUnitContext tree = parser.compilationUnit();
        DetectContext detectContext = new DetectContext(new MethodFieldDetectStrategy());
        ClassMeta detect = detectContext.detect(tree, f);
        detect.setCorrelation(correlation);
        ClassInnerConstantStrategy classInnerConstantStrategy = new ClassInnerConstantStrategy();
        classInnerConstantStrategy.product(parser, tree, detect, f);

    }

    public static void main(String[] args) throws Exception {
        XTFCommand xtfCommand = new XTFCommand();
        xtfCommand.execute(Context.Default(),"/media/lame/0DD80F300DD80F30/koal/kcsp-admin/kcsp-boot/kcsp-boot-module-system/src/main/java/kl/kcsp/modules/customer/controller/CustomerAppController.java");
    }

}
