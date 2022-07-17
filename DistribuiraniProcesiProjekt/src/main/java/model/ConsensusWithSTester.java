package model;

import java.util.Random;

public class ConsensusWithSTester {
    public static void main(String[] args) throws Exception {
        String baseName = args[0];
        int myId = Integer.parseInt(args[1]);
        int numProc = Integer.parseInt(args[2]);
        Linker comm = new Linker(baseName, myId, numProc);
        ConsensusWithS sp = new ConsensusWithS(comm, myId + 1);
        for (int i = 0; i < numProc; i++)
            if (i != myId)
                (new ListenerThread(i, sp)).start();
        sp.propose(myId);
        Random random = new Random();
        int randomNumber = random.nextInt(5) - 1;
        System.out.println("Random: " + randomNumber + ", id: " + myId + ", numProc: " + numProc);
        if(myId == randomNumber || myId == numProc - randomNumber) {
            System.out.println("Mocked error occurred!");
            System.exit(0);
        }
        System.out.println("The value decided:" + sp.decide());
    }
}
