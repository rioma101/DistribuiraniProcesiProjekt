package model;

import java.io.IOException;
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
        java.util.Arrays.fill(this.V_p, 0);
        this.V_p[this.myId] = myValue;
        this.delta_p = new int[N + 1];
        java.util.Arrays.fill(this.delta_p, 0);
        this.delta_p[this.myId] = myValue;
        this.messages = new ArrayList<ArrayList<Msg>>();
        for(int i = 0; i < N; i++) {
            ArrayList<Msg> a = new ArrayList<Msg>();
            for(int j = 0; j < N; j++) {
                a.add(null);
            }
            this.messages.add(a);
        }
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
                this.delta_p[N] = i;
                broadcastMsg("proposal", this.delta_p);
            }
            // sleep enough to receive messages for this round
            Util.mySleep(Symbols.roundTime);
            synchronized (this) {
                this.delta_p = new int[N + 1];
                for (int j = 0; j < N; j++) {
                    if(this.V_p[j] == 0) {
                        int curr = 0;
                        Msg m = messages.get(i).get(j);
                        if (m != null) {
                            curr = Character.getNumericValue(m.getMessage().charAt(2 * j + 1));
                        }
                        if (curr != 0) {
                            this.V_p[j] = curr;
                            this.delta_p[j] = curr;
                        }
                    } else {
                        this.delta_p[j] = 0;
                    }
                }
            }
        }
        // faza 2:
        synchronized (this) {
            broadcastMsg("proposal", V_p);
        }
        // sleep enough to receive messages for this round
        Util.mySleep(Symbols.roundTime);
        synchronized (this) {
            for (int j = 0; j < N; j++) {
                if (output[j]) {
                    lastMessages.add(j, receiveMsg(j));
                } else {
                    lastMessages.add(j, null);
                }
            }
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    if (lastMessages.get(k) != null && lastMessages.get(k).getMessage().charAt(2 * j) == '0') {
                        this.V_p[j] = 0;
                    }
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

    public Msg receiveMsg(int fromId) {
        try {
            Msg m = comm.receiveMsg(fromId);
            String mes = m.getMessage();
            if(mes.length() == 2 * N + 3) {
                messages.get(Character.getNumericValue(mes.charAt(mes.length() - 2))).add(fromId, m);
            }
            return m;
        } catch (IOException e){
            System.out.println(e);
            comm.close();
            return null;
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
