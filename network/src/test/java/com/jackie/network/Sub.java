package com.jackie.network;

class Super{
    public float getNum(){
        return 3.2f;
    }
}
public class Sub extends Super{
    public float getNum() {
        return 4.0f;
    }
    /*
        public void getNum(){

        }
     */
    public void getNum(double d) {

    }

    public double getNum(float d ){
        return 4.0d;
    }

}
