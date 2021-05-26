package com.lame.scanapi.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class Module {

    private String baseScan;

    private String name;

    private List<ControllerModule> children;
}
