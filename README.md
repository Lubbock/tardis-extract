# tardis-extract

#### 介绍
idea 的插件，识别实体类，自动生成对应的常量代码。
后续考虑自动完成实体替换功能


#### 软件架构
软件架构说明

在web项目的实体类中，右键点击generate 开始生成对应的常量代码

#### 安装教程
idea 搜索插件 tardis-extract

<em>在web项目的实体类中，右键点击generate 开始生成对应的常量代码</em>
   自动的把实体的字段，按照驼峰命名法的规则，进行修改，生成对应的常量如
   public class TestEntityConstant {
    //命名空间
    public static final String ID = "id" ;
    public static final String USER_NAME = "user_name" ;
}

