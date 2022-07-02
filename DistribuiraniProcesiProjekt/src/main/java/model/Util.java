package model;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.StringTokenizer;

public class Util {
    public Util() {
    }

    public static int max(int var0, int var1) {
        return var0 > var1 ? var0 : var1;
    }

    public static void mySleep(int var0) {
        try {
            Thread.sleep((long)var0);
        } catch (InterruptedException var2) {
        }

    }

    public static void myWait(Object var0) {
        println("waiting");

        try {
            var0.wait();
        } catch (InterruptedException var2) {
        }

    }

    public static boolean lessThan(int[] var0, int[] var1) {
        for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] > var1[var2]) {
                return false;
            }
        }

        for(int var3 = 0; var3 < var0.length; ++var3) {
            if (var0[var3] < var1[var3]) {
                return true;
            }
        }

        return false;
    }

    public static int maxArray(int[] var0) {
        int var1 = var0[0];

        for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] > var1) {
                var1 = var0[var2];
            }
        }

        return var1;
    }

    public static String writeArray(int[] var0) {
        StringBuffer var1 = new StringBuffer();

        for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.append(var0[var2] + " ");
        }

        return new String(var1.toString());
    }

    public static void readArray(String var0, int[] var1) {
        StringTokenizer var2 = new StringTokenizer(var0);

        for(int var3 = 0; var3 < var1.length; ++var3) {
            var1[var3] = Integer.parseInt(var2.nextToken());
        }

    }

    public static int searchArray(int[] var0, int var1) {
        for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] == var1) {
                return var2;
            }
        }

        return -1;
    }

    public static void println(String var0) {
        System.out.println(var0);
        System.out.flush();
    }
}
