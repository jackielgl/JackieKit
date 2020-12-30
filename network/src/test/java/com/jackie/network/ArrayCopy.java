package com.jackie.network;

public class ArrayCopy {
    public static void main(String[] args) {
        int[] arr1={1,2,3,4,5};
        int[] arr2 = arr1;
        arr2[0] = 10;

        System.out.printf("%d,%d",arr1[0],arr2[0]);
    }
}
