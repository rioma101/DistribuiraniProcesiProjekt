package model;

import java.util.ArrayList;

public class ConsensusWithS extends FailureDetector {
    int myValue;
    boolean changed = true;
    boolean hasProposed = false;
    int[] V_p, delta_p;
    ArrayList<ArrayList<Msg>> messages;
    ArrayList<Msg> lastMessages;

    public ConsensusWithS(Linker initComm, int myValue) {
        super(initComm);
        this.myValue = myValue;
        this.V_p = new int[N];
        this.V_p[this.myId] = myValue;
        this.delta_p = new int[N];
        this.delta_p[this.myId] = myValue;
        this.messages = new ArrayList<ArrayList<Msg>>();
        this.lastMessages = new ArrayList<Msg>();
    }

    public synchronized void propose(int value) {
        myValue = value;
        hasProposed = true;
        notify();
    }

    public int decide() {
        // faza 1:
        for(int i = 1; i < N; i++) {
            synchronized (this) {
                broadcastMsg("proposal", this.delta_p);
            }
            // sleep enough to receive messages for this round
            Util.mySleep(Symbols.roundTime);
            for(int j = 0; j < N; j++) {
                if(output[j]) {
                    messages.get(i).add(j, receiveMsg(j));
                }
            }
            this.delta_p = new int[N];
            for(int j = 1; j < N; j++) {
                int curr = 0;
                Msg m = messages.get(i).get(j);
                if(m != null) {
                    curr = m.getMessage().charAt(2 * j);
                }
                if(this.V_p[j] == 0 && curr != 0) {
                    this.V_p[j] = curr;
                    this.delta_p[j] = curr;
                }
            }
        }
        // faza 2;
        synchronized (this) {
            broadcastMsg("proposal", delta_p);
        }
        // sleep enough to receive messages for this round
        Util.mySleep(Symbols.roundTime);
        for(int j = 0; j < N; j++) {
            if(output[j]) {
                lastMessages.add(j, receiveMsg(j));
            }
        }
        for(int j = 0; j < N; j++) {
            for(int k = 0; k < N; k++) {
                if(lastMessages.get(k).getMessage().charAt(2 * j) == 0) {
                    this.V_p[j] = 0;
                }
            }
        }
        // faza 3:
        synchronized (this) {
            for (int i = 0; i < N; i++) {
                if (this.V_p[i] != 0) {
                    return this.V_p[i];
                }
            }
            return 0;
        }
    }

    /*public synchronized void handleMsg(Msg m, int src, String tag) {
        while (!hasProposed)
            myWait();
        if (tag.equals("proposal")) {
            int value = m.getMessageInt();
            if (value < myValue) {
                myValue = value;
                changed = true;
            }
        }
    }*/
}
