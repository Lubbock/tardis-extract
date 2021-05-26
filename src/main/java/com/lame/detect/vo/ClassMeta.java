package com.lame.detect.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassMeta {

    /**
     * 被导入的时候包名称
     * **/
    private String importName;

    private FileType fileType;

    private String name;

    private String packageName;

    private List<ClassField> fields = new ArrayList<>();

    private List<LineExtraMeta> lineExtraMetas = new ArrayList<>();

    private ClassMeta correlation;

    public void addFiled(ClassField field) {
        fields.add(field);
    }
}
