package model;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public interface Lock extends MsgHandler {
    void requestCS();

    void releaseCS();
}
