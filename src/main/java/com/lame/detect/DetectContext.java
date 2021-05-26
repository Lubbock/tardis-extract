package com.lame.detect;

import com.lame.detect.vo.ClassMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

@Data
@AllArgsConstructor
public class DetectContext {

    private DetectStrategy detectStrategy;

    public ClassMeta detect(ParseTree parseTree, String fp) {
        ClassMeta detect = detectStrategy.detect(parseTree);
        File f = new File(fp);
        detect.setName(StringUtils.substringBeforeLast(f.getName(), "."));
        return detect;
    }
}
