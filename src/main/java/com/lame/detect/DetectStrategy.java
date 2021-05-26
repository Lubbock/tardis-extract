package com.lame.detect;

import com.lame.detect.vo.ClassMeta;
import org.antlr.v4.runtime.tree.ParseTree;

public interface DetectStrategy {
    ClassMeta detect(ParseTree parseTree);
}
