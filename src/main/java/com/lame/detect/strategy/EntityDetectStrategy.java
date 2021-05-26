package com.lame.detect.strategy;

import com.lame.detect.DetectStrategy;
import com.lame.detect.visit.EntityFieldExtract;
import com.lame.detect.vo.ClassField;
import com.lame.detect.vo.ClassMeta;
import com.lame.sbconstant.utils.TextUtils;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

public class EntityDetectStrategy implements DetectStrategy {
    @Override
    public ClassMeta detect(ParseTree parseTree) {
        EntityFieldExtract extract = new EntityFieldExtract();
        extract.visit(parseTree);
        List<String> detectInfo = extract.detectInfo();
        ClassMeta classMeta = new ClassMeta();
        classMeta.setPackageName(extract.getPackageName());
        for (String s : detectInfo) {
      if (!StringUtils.isBlank(s)){
          String[] typeAndName = s.split(" ");
          String type = typeAndName[0];
          String fieldName = typeAndName[1];
          if (StringUtils.equals(fieldName, "serialVersionUID")) {
              continue;
          }
          ClassField field = new ClassField();
          field.setName(fieldName);
          field.setType(type);
          field.setHumpName(TextUtils.convertLineToHumpTest(fieldName));
          field.setUpHumpName(TextUtils.convertLineToHumpTest(fieldName).toUpperCase(Locale.ROOT));
          classMeta.addFiled(field);
      }

        }
        return classMeta;
    }
}
