package model;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.LinkedList;

public class IntLinkedList extends LinkedList {
    public IntLinkedList() {
    }

    public void add(int var1) {
        super.add(new Integer(var1));
    }

    public boolean contains(int var1) {
        return super.contains(new Integer(var1));
    }

    public int removeHead() {
        Integer var1 = (Integer)super.removeFirst();
        return var1;
    }

    public boolean removeObject(int var1) {
        return super.remove(new Integer(var1));
    }

    public int getEntry(int var1) {
        Integer var2 = (Integer)super.get(var1);
        return var2;
    }
}
