package com.lame.sbconstant.command;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class KoalExtractEntityCommand implements Command{

  private Command command = new EntityExtractCommand();

    @Override
    public void execute(Context ctx, String f) throws Exception {
    File file = new File(f);
      final String name = file.getParentFile().getName();
      if (StringUtils.equalsIgnoreCase(name, "entity")) {
        final String parent = file.getParentFile().getParent();
      File f1 = new File(parent, "constant");
      if (!f1.exists()) {
        f1.mkdirs();
      }
      File op =
          new File(
              f1.getAbsolutePath(),
              StringUtils.substringBeforeLast(file.getName(),".java")+ "Constant.java");
      if (op.exists()) {
        op = new File(f1.getAbsolutePath(), file.getName() + System.currentTimeMillis());
        }
      ctx.output(op.getAbsolutePath());
        command.execute(ctx, f);

      }else {
        //ignore
      }
    }
}
