package com.ysh.dlt2811bean.transport.goose;

import java.util.concurrent.atomic.AtomicLong;

public class GooseStateMachine {

    static final long[] FAST_RETRANSMIT_DELAYS_MS = {0, 1, 2, 4, 8};

    private final AtomicLong stNum = new AtomicLong(0);
    private final AtomicLong sqNum = new AtomicLong(0);
    private int fastRetransmitIndex = 0;

    public void onEvent() {
        stNum.incrementAndGet();
        sqNum.set(0);
        fastRetransmitIndex = 0;
    }

    public long onTick() {
        if (fastRetransmitIndex < FAST_RETRANSMIT_DELAYS_MS.length) {
            fastRetransmitIndex++;
        }
        return sqNum.getAndIncrement();
    }

    public boolean isInFastRetransmit() {
        return fastRetransmitIndex < FAST_RETRANSMIT_DELAYS_MS.length;
    }

    public long getCurrentStNum() {
        return stNum.get();
    }

    public long getCurrentSqNum() {
        return sqNum.get();
    }

    public int getFastRetransmitIndex() {
        return fastRetransmitIndex;
    }

    public void reset() {
        stNum.set(0);
        sqNum.set(0);
        fastRetransmitIndex = 0;
    }
}
