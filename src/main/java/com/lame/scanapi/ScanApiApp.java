package com.lame.scanapi;



import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.lame.detect.strategy.APIDetectStrategy;
import com.lame.detect.vo.API;
import com.lame.detect.vo.ApiClassMeta;
import com.lame.detect.vo.ClassMeta;
import com.lame.scanapi.vo.ControllerModule;
import com.lame.scanapi.vo.Module;
import com.lame.scanapi.vo.Project;
import core.analy.Java8Lexer;
import core.analy.Java8Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScanApiApp {

    public static Project scanProject(String baseScan) {
        List<Module> modules = new ArrayList<>();
        File f = new File(baseScan);
        Project project = new Project();
        project.setScanBasename(baseScan);
        project.setModules(modules);
        for (File file : f.listFiles()) {
            if (file.isDirectory()) {
                Module module = new Module();
                module.name(file.getName());
                List<ControllerModule> controllerModules = new ArrayList<>();
                File conDir = new File(file.getAbsolutePath(), "controller");
                if (conDir.exists()) {
                    modules.add(module);
                    for (File tf : conDir.listFiles()) {
                        if (tf.isFile() && tf.getName().endsWith("Controller.java")) {
                            ControllerModule controllerModule = new ControllerModule();
                            controllerModule.setName(controllerModule.getName());
                            controllerModule.setFile(tf);
                            controllerModules.add(controllerModule);
                        }
                    }
                    module.children(controllerModules);
                }
            }
        }
        return project;
    }

    public static void moduleScan(ControllerModule cm) {
        String f = cm.getFile().getAbsolutePath();
        try {
            Lexer lexer = new Java8Lexer(CharStreams.fromFileName(f));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Java8Parser parser = new Java8Parser(tokens);
            Java8Parser.CompilationUnitContext tree = parser.compilationUnit();
            APIDetectStrategy detect = new APIDetectStrategy();
            ApiClassMeta apiClassMeta = (ApiClassMeta)detect.detect(tree);
            cm.setApiClassMeta(apiClassMeta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String baseScan = "/media/lame/0DD80F300DD80F30/koal/kcsp-admin/kcsp-boot/kcsp-boot-module-system/src/main/java/kl/kcsp/modules";
        Project project = scanProject(baseScan);
        List<Module> modules = project.getModules();
        for (Module module : modules) {
            for (ControllerModule cm : module.children()) {
                moduleScan(cm);
            }
        }

//        ????????????	08e6b9dc3c04489c8e1ff2ce6f105aa4	monitor
//        ??????????????????	1336941192918274049	srvtpl
//        ????????????	1338426581305049090	serviceStore
//        ????????????	1338726488880545794	api,application,authkey,customer,report,srvinse,vsm
//        ????????????	1344158334211272706	chsm,vsm
//        ????????????	1356841470050222081	operationAudit
//        ????????????	1384029851890806786	feedback
//        ????????????	d7d6e2e4e2934f2c9385a623fd98c6f3	bakrec,cert,helpCenter,message,quartz,system

        //permission_id // module
        Map<String, String> siteMap = new HashMap<>();
        siteMap.put("08e6b9dc3c04489c8e1ff2ce6f105aa4", "monitor");
        siteMap.put("1336941192918274049", "srvtpl");
        siteMap.put("1338426581305049090", "serviceStore");
        siteMap.put("1338726488880545794", "api,application,authkey,customer,report,srvinse,vsm");
        siteMap.put("1344158334211272706", "chsm,vsm");
        siteMap.put("1356841470050222081", "operationAudit");
        siteMap.put("1384029851890806786", "feedback");
        siteMap.put("d7d6e2e4e2934f2c9385a623fd98c6f3", "bakrec,cert,helpCenter,message,quartz,system");

        HashMultimap<String, String> reverse = HashMultimap.create();
        for (Map.Entry<String, String> entry : siteMap.entrySet()) {
            String value = entry.getValue();
            for (String newKey : value.split(",")) {
                reverse.put(newKey, entry.getKey());
            }
        }

        List<Module> unbindModule = new ArrayList<>();
        long i = 1387663170767937537L;
        int step = 1;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Module module : modules) {
            Set<String> perms = reverse.get(module.name());
            if (perms != null && perms.size() > 0) {
                List<ControllerModule> children = module.children();
                for (ControllerModule child : children) {
                    ApiClassMeta apiClassMeta = child.getApiClassMeta();
                    for (API api : apiClassMeta.getApis()) {
                        String url = api.getUrl();
                        if (!url.startsWith("/")) {
                            url = "/" + url;
                        }
                        url = apiClassMeta.getBaseApiPath() + url;
                        String note = module.name();
                        if (StringUtils.isNotBlank(api.getNote())) {
                            String temp = api.getNote();
                            note = temp.replaceAll("\'", "\"");
                        }
                        for (String perm : perms) {
                            String sql = String.format("insert into sys_api_permission values('%s','%s','%s','%s','%s');", i + step, note, url, timeFormatter.format(LocalDateTime.now()), perm);
                            step++;
                            System.out.println(sql);
                        }
                    }
                }
            }else {
                unbindModule.add(module);
            }
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (Module module : unbindModule) {
            System.out.println("????????????????????????-module " + module.name());
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("++++++++++++++++++++++++++++++++++++");
        for (Module module : modules) {
            Set<String> perms = reverse.get(module.name());
            if (perms != null && perms.size() > 0) {
                List<ControllerModule> children = module.children();
                for (ControllerModule child : children) {
                    ApiClassMeta apiClassMeta = child.getApiClassMeta();
                    List<API> apis = apiClassMeta.getApis();
                    System.out.println("....????????????:" + module.name());
                    System.out.println("....????????????url:" + apiClassMeta.getBaseApiPath());
                    System.out.println("*******************************");
                    for (API api : apis) {
                        if (StringUtils.isBlank(api.getNote())) {
                            System.out.println("??????????????????" + api.getUrl());
                        }
                    }
                    System.out.println("****************************");
                }
            }
        }
        System.out.println("++++++++++++++++++++++++++++++++++++");
        //language=JSON
        String s = "";
    }
}
