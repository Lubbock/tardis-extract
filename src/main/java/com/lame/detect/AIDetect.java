package com.lame.detect;

import com.lame.detect.visit.PackageImportExtract;
import com.lame.detect.vo.FileType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(fluent = true)
public class AIDetect {

    private static String entityFlag = "entity";
    private static String serviceFlag = "service";
    private static String controllerFlag = "controller";
    private static String daoFlag = "mapper";

    PackageImportExtract extract = new PackageImportExtract();

    public Map<String, FileType> generateSiteMap() {
        Map<String, FileType> siteMap = new HashMap<>(7);
        siteMap.put(entityFlag, FileType.ENTITY);
        siteMap.put(serviceFlag, FileType.SERVICE);
        siteMap.put(controllerFlag, FileType.CONTROLLER);
        siteMap.put(daoFlag, FileType.DAO);
        return siteMap;
    }

    public FileType visit(ParseTree tree) {
        extract.visit(tree);
        String detectPackage = extract.getDetectPackage();
        FileType detectType = FileType.COMMON;
        for (Map.Entry<String, FileType> entry : generateSiteMap().entrySet()) {
            if (StringUtils.endsWith(detectPackage, entry.getKey())) {
                detectType = entry.getValue();
                break;
            }
        }
        return detectType;
    }
}
