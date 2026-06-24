package com.ysh.jcms.utils.scl.model.control;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SclRCBState {

    private boolean rptEna;
    private boolean gi;
    private boolean purgeBuf;
    private byte[] entryID;
    private int resvTms;

    public SclRCBState() {
        this.rptEna = false;
        this.gi = false;
        this.purgeBuf = false;
        this.entryID = null;
        this.resvTms = 0;
    }
}
