package com.jackie.network;

public class FooBar {
    public static void main(String[] args) {
        int i=0, j=5;
        tp:for(;;i++){
            for(;;j--){
                if(i>j ) break tp;
            }
        }

        System.out.println("i="+i +",j="+j);

    }
}
