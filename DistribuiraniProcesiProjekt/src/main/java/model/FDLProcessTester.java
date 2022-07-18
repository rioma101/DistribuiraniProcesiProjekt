package model;

import java.util.Random;

public class FDLProcessTester {
    public static void main(String[] args) throws Exception {
        String baseName = args[0];
        int myId = Integer.parseInt(args[1]);
        int numProc = Integer.parseInt(args[2]);
        Linker comm = new Linker(baseName, myId, numProc);
        FDLProcess sp = new FDLProcess(comm);
        for (int i = 0; i < numProc; i++)
            if (i != myId)
                (new ListenerThread(i, sp)).start();
        System.out.println("Base: " + baseName + ", process: " + myId + ", numProc: " + numProc);
        for (int i = 0; i < numProc; i++) {
            if(i != myId) {
                sp.sendMsg(i, "appl", "Message from " + myId);
            }
        }
        Util.mySleep(Symbols.roundTime);
        Random random = new Random();
        int randomNumber = random.nextInt(5) - 1;
        System.out.println("Random: " + randomNumber + ", id: " + myId + ", numProc: " + numProc);
        if(myId == randomNumber || myId == numProc - randomNumber) {
            System.out.println("Mocked error occurred!");
            sp.isAlive = false;
        }
        for(int j = 0; j < numProc; j++) {
            for (int i = 0; i < numProc; i++) {
                if (i != myId) {
                    sp.sendMsg(i, "appl", "Message from " + myId);
                }
            }
            Util.mySleep(Symbols.roundTime);
        }
        Util.mySleep(Symbols.roundTime);
        for (int i = 0; i < numProc; i++) {
            if(i != myId) {
                System.out.println("Process " + i + ": " + sp.query(i));
            }
        }
    }
}
