package com.lame.detect.strategy;

import com.lame.detect.DetectStrategy;
import com.lame.detect.vo.ClassMeta;
import com.lame.detect.vo.FileType;
import lombok.Data;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * xDetectStrategy 会从实体常量抽取到控制器
 * *
 **/
@Data
public class XDetectStrategy implements DetectStrategy {

    MethodFieldDetectStrategy methodFieldDetectStrategy = new MethodFieldDetectStrategy();

    EntityDetectStrategy entityDetectStrategy = new EntityDetectStrategy();

    FileType fileType;

    @Override
    public ClassMeta detect(ParseTree parseTree) {
        ClassMeta mfFieldMeta = methodFieldDetectStrategy.detect(parseTree);
        ClassMeta entityMeta = entityDetectStrategy.detect(parseTree);
        //获得实体树
        entityMeta.setLineExtraMetas(mfFieldMeta.getLineExtraMetas());
        return null;
    }
}
