package com.lame.detect.vo;


import java.util.ArrayList;
import java.util.List;

public class ApiClassMeta extends ClassMeta{
    private String baseApiPath;

    private List<API> apis = new ArrayList<>();

    public void addApi(String url,String note) {
        API api = new API(url, note);
        apis.add(api);
    }

    public List<API> getApis() {
        return apis;
    }

    public void setApis(List<API> apis) {
        this.apis = apis;
    }

    public String getBaseApiPath() {
        return baseApiPath;
    }

    public void setBaseApiPath(String baseApiPath) {
        this.baseApiPath = baseApiPath;
    }
}
