package com.jackie.network;

public class Test {
    public  static String output="";

    public   static void foo(int i){
        try {
            if (i==1){
                throw  new Exception();
            }
            output += "1";

        }catch (Exception e){
            output += "2";
            return;
        }finally {
            output += "3";

        }
    }

    public static void main(String[] args) {
        foo(1);
        foo(2);
        System.out.println(output);
    }
}
