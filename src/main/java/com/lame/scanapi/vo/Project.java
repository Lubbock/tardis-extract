package com.lame.scanapi.vo;


import lombok.Data;

import java.util.List;

@Data
public class Project {
    private String scanBasename;

    List<Module> modules;
}
