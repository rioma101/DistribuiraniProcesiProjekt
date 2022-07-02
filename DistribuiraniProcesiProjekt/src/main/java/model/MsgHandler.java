package model;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.IOException;

public interface MsgHandler {
    void handleMsg(Msg var1, int var2, String var3);

    Msg receiveMsg(int var1) throws IOException;
}
