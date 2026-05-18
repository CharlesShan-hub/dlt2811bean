package com.ysh.dlt2811bean.scl2.model;

import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class SclRCBState {

    private static final String SESSION_KEY = SclRCBState.class.getName();

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

    @SuppressWarnings("unchecked")
    public static Map<String, SclRCBState> getOrCreateSessionState(CmsServerSession session) {
        Map<String, SclRCBState> state = (Map<String, SclRCBState>) session.getAttribute(SESSION_KEY);
        if (state == null) {
            state = new ConcurrentHashMap<>();
            session.setAttribute(SESSION_KEY, state);
        }
        return state;
    }
}