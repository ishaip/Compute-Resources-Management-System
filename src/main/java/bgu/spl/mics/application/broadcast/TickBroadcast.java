package bgu.spl.mics.application.broadcast;

import bgu.spl.mics.Broadcast;

public class TickBroadcast  implements Broadcast {
    private final int tick;

    TickBroadcast(int tick){this.tick = tick;}

    public Integer getTick(){return tick;}

}
