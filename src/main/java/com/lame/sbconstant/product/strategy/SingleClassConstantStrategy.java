package com.lame.sbconstant.product.strategy;

import com.lame.detect.vo.ClassMeta;
import com.lame.sbconstant.product.ProductStrategy;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class SingleClassConstantStrategy implements ProductStrategy {

    private String tpl = "${PACKAGE}.constant;\n" +
            "\n" +
            "public class ${ENTITY}Constant {\n" +
            "    //命名空间\n" +
            "    <#list fields as field >\n" +
            "    public static final String ${field.upHumpName} = \"${field.humpName}\" ;\n" +
            "    </#list>\n" +
            "}\n";

    @Override
    public String product(Parser parser, ParseTree parseTree, ClassMeta classMeta, String fp) {
        Configuration configuration = new Configuration();
        Map<String, Object> root = new HashMap<>(5);
        root.put("ENTITY", classMeta.getName());
        root.put("fields", classMeta.getFields());
        String pkg = StringUtils.substringBeforeLast(classMeta.getPackageName(), ".");
        root.put("PACKAGE", pkg);
        StringWriter stringWriter = new StringWriter();
        try {
            Template template = new Template(classMeta.getName(), tpl, configuration);
            template.process(root, stringWriter);
            System.out.println(stringWriter.toString());
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
