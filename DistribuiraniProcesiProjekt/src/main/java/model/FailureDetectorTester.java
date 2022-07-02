package model;

import java.util.Random;

public class FailureDetectorTester {
    public static void main(String[] args) {
        Linker comm;
        try {
            String baseName = args[0];
            int myId = Integer.parseInt(args[1]);
            int numProc = Integer.parseInt(args[2]);
            comm = new Linker(baseName, myId, numProc);
            FailureDetector failureDetector = new FailureDetector(comm);
            Random random = new Random();
            for (int i = 0; i < numProc; i++)
                if (i != myId)
                    (new ListenerThread(i, failureDetector)).start();

            while(true) {
                Util.mySleep(3000);
                int randomNumber = random.nextInt(7);
                if(randomNumber == 1) failureDetector.amAlive();
                failureDetector.updateList();
                failureDetector.showList();
                failureDetector.resetTick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
