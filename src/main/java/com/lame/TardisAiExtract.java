package com.lame;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.lame.sbconstant.command.Context;
import com.lame.sbconstant.command.KoalExtractEntityCommand;

public class TardisAiExtract extends AnAction {
    KoalExtractEntityCommand command = new KoalExtractEntityCommand();

    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取当前在操作的工程上下文
        Project project = e.getData(PlatformDataKeys.PROJECT);

        //获取当前操作的类文件
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        //获取当前类文件的路径
        String classPath = psiFile.getVirtualFile().getPath();
        try {
            Context ctx = new Context();
            ctx.aiDetect = true;
            command.execute(ctx, classPath);
            Messages.showMessageDialog(project, "文件生成成功请刷新项目", "常量文件生成成功", Messages.getInformationIcon());
        } catch (Exception exception) {
            exception.printStackTrace();
            Messages.showMessageDialog(project, "文件解析失败", "文件解析失败", Messages.getInformationIcon());
        }

//        String title = "Hello World!";
//
//        //显示对话框
//        Messages.showMessageDialog(project, classPath, title, Messages.getInformationIcon());
    }
}
