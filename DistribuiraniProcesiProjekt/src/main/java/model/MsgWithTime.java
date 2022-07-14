package model;

public class MsgWithTime {
    Msg message;
    int time;

    public MsgWithTime(Msg message, int time) {
        this.message = message;
        this.time = time;
    }

    public Msg getMessage() {
        return this.message;
    }

    public int getTime() {
        return this.time;
    }
}
