package com.ysh.dlt2811bean.scl2.model;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class SclSGCBState {

    private static final String SESSION_KEY = SclSGCBState.class.getName();

    private int numOfSG;
    private int actSG;
    private int editSG;
    private boolean cnfEdit;
    private final Map<String, CmsData<?>> editValues = new ConcurrentHashMap<>();

    public SclSGCBState() {
        this.numOfSG = 1;
        this.actSG = 1;
        this.editSG = 1;
        this.cnfEdit = true;
    }

    public SclSGCBState(int numOfSG) {
        this.numOfSG = numOfSG;
        this.actSG = 1;
        this.editSG = 1;
        this.cnfEdit = true;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, SclSGCBState> getOrCreateSessionState(CmsServerSession session) {
        Map<String, SclSGCBState> state = (Map<String, SclSGCBState>) session.getAttribute(SESSION_KEY);
        if (state == null) {
            state = new ConcurrentHashMap<>();
            session.setAttribute(SESSION_KEY, state);
        }
        return state;
    }
}