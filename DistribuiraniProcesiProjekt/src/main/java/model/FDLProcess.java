package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FDLProcess extends Process {
    boolean isAlive;
    LamportClock lampCl;
    int[] maxRoundTripDelay;
    ArrayList<ArrayList<Integer>> pendingMessageST;

    public FDLProcess(Linker initComm) {
        super(initComm);
        pendingMessageST = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < N; i++) {
            this.pendingMessageST.add(new ArrayList<Integer>());
        }
        maxRoundTripDelay = new int[N];
        Arrays.fill(maxRoundTripDelay, 0);
        lampCl = new LamportClock();
        isAlive = true;
    }

    @Override
    public void sendMsg(int destId, String tag, String msg) {
        if(isAlive) {
            if(!tag.equals("ack")) {
                msg = lampCl.getValue() + " " + msg;
                pendingMessageST.get(destId).add(lampCl.getValue());
                lampCl.tick();
            }
            Util.println("Sending msg to " + destId + ": " + tag + " " + msg);
            comm.sendMsg(destId, tag, msg);
        }
    }

    @Override
    public Msg receiveMsg(int fromId) {
        try {
            MsgWithTime m = comm.receiveMsgWithTime(fromId);
            if(isAlive) {
                String tag = m.getMessage().getTag();
                if (tag.equals("appl")) {
                    this.sendMsg(fromId, "ack", m.getTime() + " ");
                } else if(tag.equals("ping")) {
                    this.sendMsg(fromId, "ack", lampCl.getValue() + " ");
                } else if (tag.equals("ack")) {
                    this.maxRoundTripDelay[fromId] = Math.max(lampCl.getValue() - m.getTime(), this.maxRoundTripDelay[fromId]);
                    this.pendingMessageST.get(fromId).remove(Integer.valueOf(m.getTime()));
                }
            }
            return m.getMessage();
        } catch (IOException e){
            System.out.println(e);
            comm.close();
            return null;
        }
    }

    public String query(int fromId) {
        if(isAlive) {
            if (pendingMessageST.get(fromId).isEmpty()) {
                this.sendMsg(fromId, "ping", "S");
                return "not_suspect";
            } else {
                int min = Collections.min(pendingMessageST.get(fromId));
                if (lampCl.getValue() - min > maxRoundTripDelay[fromId]) {
                    return "suspect";
                } else {
                    return "not_suspect";
                }
            }
        }
        return "";
    }
}
