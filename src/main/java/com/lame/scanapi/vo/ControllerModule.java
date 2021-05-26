package com.lame.scanapi.vo;

import com.lame.detect.vo.ApiClassMeta;
import lombok.Data;

import java.io.File;

@Data
public class ControllerModule {
    private String name;

    private File file;

    private ApiClassMeta apiClassMeta;
}
