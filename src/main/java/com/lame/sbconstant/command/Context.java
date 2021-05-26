package com.lame.sbconstant.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.C;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class Context {
    public boolean aiDetect = true;

    public boolean saveFile = true;

    public String output = "./";

    public static Context Default() {
        return new Context();
    }
}
