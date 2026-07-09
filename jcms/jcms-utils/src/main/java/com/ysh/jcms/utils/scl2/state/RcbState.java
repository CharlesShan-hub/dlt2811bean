package com.ysh.jcms.utils.scl2.state;

import lombok.Getter;
import lombok.Setter;

/**
 * RCB 运行时状态（BRCB/URCB）。
 */
@Getter
@Setter
public class RcbState {

    private boolean rptEna;
    private boolean gi;
    private boolean purgeBuf;
    private byte[] entryID;
    private int resvTms;

    public RcbState() {
        this.rptEna = false;
        this.gi = false;
        this.purgeBuf = false;
        this.entryID = null;
        this.resvTms = 0;
    }
}
