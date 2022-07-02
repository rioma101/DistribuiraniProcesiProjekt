package model;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.StringTokenizer;

public class Msg {
    int srcId;
    int destId;
    String tag;
    String msgBuf;

    public Msg(int var1, int var2, String var3, String var4) {
        this.srcId = var1;
        this.destId = var2;
        this.tag = var3;
        this.msgBuf = var4;
    }

    public int getSrcId() {
        return this.srcId;
    }

    public int getDestId() {
        return this.destId;
    }

    public String getTag() {
        return this.tag;
    }

    public String getMessage() {
        return this.msgBuf;
    }

    public int getMessageInt() {
        StringTokenizer var1 = new StringTokenizer(this.msgBuf);
        return Integer.parseInt(var1.nextToken());
    }

    public static Msg parseMsg(StringTokenizer var0) {
        int var1 = Integer.parseInt(var0.nextToken());
        int var2 = Integer.parseInt(var0.nextToken());
        String var3 = var0.nextToken();
        String var4 = var0.nextToken("#");
        return new Msg(var1, var2, var3, var4);
    }

    public String toString() {
        String var1 = this.srcId + " " + this.destId + " " + this.tag + " " + this.msgBuf + "#";
        return var1;
    }
}
