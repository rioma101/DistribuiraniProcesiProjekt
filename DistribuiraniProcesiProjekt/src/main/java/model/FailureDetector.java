package model;

import java.util.Arrays;

public class FailureDetector extends Process {
    boolean[] output;
    int[] delta;
    boolean[] aliveThisHeartbeat;
    int[] ticksSinceLastAlive;

    FailureDetector(Linker initComm) {
        super(initComm);
        output = new boolean[N];
        delta = new int[N];
        Arrays.fill(delta, 3);
        aliveThisHeartbeat = new boolean[N];
        ticksSinceLastAlive = new int[N];
    }

    @Override
    public synchronized void handleMsg(Msg m, int src, String tag) {
        if (tag.equals("alive")) {
            int aliveProcess = m.getMessageInt();
            if (!output[aliveProcess]) {
                output[aliveProcess] = true;
                delta[aliveProcess]++;
            }
            ticksSinceLastAlive[aliveProcess] = 0;
            aliveThisHeartbeat[aliveProcess] = true;
            notifyAll();

        }
    }

    void amAlive() {
        sendToNeighbors("alive", myId);
    }

    public void updateList() {
        for (int i = 0; i < N; i++) {
            if (!aliveThisHeartbeat[i] && output[i]) {
                ticksSinceLastAlive[i]++;
                if (ticksSinceLastAlive[i] >= delta[i]) {
                    output[i] = false;
                }
            }
        }
    }

    public void resetTick() {
        aliveThisHeartbeat = new boolean[N];
    }

    public void showList() {
        System.out.println("-------------------------------------");
        for (int i = 0; i < N; i++) {
            if (i != myId) {
                System.out.println("process " + i + " is " + (output[i] ? "alive" : "dead"));
                System.out.println("process " + i + (aliveThisHeartbeat[i] ? " sent " : " didn't send ") + "alive message this tick");
                System.out.println("process " + i + " sent last tick " + ticksSinceLastAlive[i] + " ticks ago");
                System.out.println("process " + i + " has delta " + delta[i]);
            }
        }
        System.out.println("-------------------------------------");
    }
}
