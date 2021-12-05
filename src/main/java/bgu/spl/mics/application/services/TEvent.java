package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;

public class TEvent implements Event<Integer> {
    public int num;

    public  TEvent(int n){this.num = n;}
}
