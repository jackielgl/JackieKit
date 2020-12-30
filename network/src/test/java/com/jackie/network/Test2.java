package com.jackie.network;

public class Test2 {
    public static void main(String[] args) {
        int aInt1 = 5;
        int aInt2 = 3;
        operate(aInt1,aInt2);
        System.out.println(aInt1 +" ," + aInt2);
    }

    public static void operate(int aInt1, int aint2){
        aInt1 = aInt1 + aint2;
        aint2 = aInt1;
    }
}
