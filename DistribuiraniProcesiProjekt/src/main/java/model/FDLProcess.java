package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FDLProcess extends Process {
    int N, myId;
    Linker comm;
    LamportClock lampCl;
    int[] maxRoundTripDelay;
    ArrayList<ArrayList<Integer>> pendingMessageST;

    public FDLProcess(Linker initComm) {
        super(initComm);
        pendingMessageST = new ArrayList<ArrayList<Integer>>();
        maxRoundTripDelay = new int[N];
        Arrays.fill(maxRoundTripDelay, 0);
    }

    @Override
    public void sendMsg(int destId, String tag, String msg) {
        msg = lampCl.getValue() + " " + msg;
        Util.println("Sending msg to " + destId + ":" + tag + " " + msg);
        pendingMessageST.get(destId).add(lampCl.getValue());
        comm.sendMsg(destId, "appl", msg);
        lampCl.tick();
    }

    public void sendMsg(int destId, String tag, String msg, int time) {
        msg = time + " " + msg;
        Util.println("Sending msg to " + destId + ":" + tag + " " + msg);
        comm.sendMsg(destId, "appl", msg);
    }

    @Override
    public Msg receiveMsg(int fromId) {
        try {
            MsgWithTime m = comm.receiveMsgWithTime(fromId);
            String tag = m.getMessage().getTag();
            if(tag.equals("appl") || tag.equals("ping")) {
                sendMsg(fromId, "ack", "");
            } else if(tag.equals("ack")) {
                this.maxRoundTripDelay[fromId] = Math.max(lampCl.getValue() - m.getTime(), this.maxRoundTripDelay[fromId]);
                this.pendingMessageST.get(fromId).remove(m.getTime());
            }
            return m.getMessage();
        } catch (IOException e){
            System.out.println(e);
            comm.close();
            return null;
        }
    }

    public String query(int fromId) {
        if(pendingMessageST.get(fromId).isEmpty()) {
            sendMsg(fromId, "ping", "", lampCl.getValue());
            pendingMessageST.get(fromId).add(lampCl.getValue());
            return "not_suspect";
        } else {
            int min = Collections.min(pendingMessageST.get(fromId));
            if(lampCl.getValue() - min > maxRoundTripDelay[fromId]) {
                return "suspect";
            } else {
                return "not_suspect";
            }
        }
    }
}
