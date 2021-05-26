package com.lame.sbconstant.command;

public interface Command {
    void execute(Context ctx, String f) throws Exception;
}
