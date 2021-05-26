${PACKAGE}.constant;

public class ${ENTITY}Constant {
    //命名空间
    <#list fields as field >
    public static final String ${field.upHumpName} = "${field.humpName}"
    </#list>
}
