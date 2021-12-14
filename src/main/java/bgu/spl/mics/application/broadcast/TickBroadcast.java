package bgu.spl.mics.application.broadcast;

import bgu.spl.mics.Broadcast;

public class TickBroadcast  implements Broadcast {
    private  int tick;

    public TickBroadcast(int tick){this.tick = tick;}

    public Integer getTick(){return tick;}
    public void setTick(int tick){this.tick = tick;}
}
