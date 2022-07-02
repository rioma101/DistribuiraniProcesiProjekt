package model;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class PortAddr {
    String hostname;
    int portnum;

    public PortAddr(String var1, int var2) {
        this.hostname = new String(var1);
        this.portnum = var2;
    }

    public String getHostName() {
        return this.hostname;
    }

    public int getPort() {
        return this.portnum;
    }
}
