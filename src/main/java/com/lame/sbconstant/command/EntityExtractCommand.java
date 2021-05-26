package com.lame.sbconstant.command;

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

public class EntityExtractCommand implements Command{
    @Override
    public void execute(Context ctx, String f) throws Exception {
        Lexer lexer = new Java8Lexer(CharStreams.fromFileName(f));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        Java8Parser.CompilationUnitContext tree = parser.compilationUnit();
        DetectContext detectContext = ConstantDetectFactory.getDetectContext(FileType.ENTITY);
        ClassMeta detectMeta = detectContext.detect(tree, f);
        ProductContext productContext = ProductFactory.getProductContext(FileType.ENTITY);
        String product = productContext.product(parser, tree, detectMeta, f);
    if (ctx.saveFile()) {
      FileUtils.write(new File(ctx.output()), product, "utf-8");
    }
    }
}
