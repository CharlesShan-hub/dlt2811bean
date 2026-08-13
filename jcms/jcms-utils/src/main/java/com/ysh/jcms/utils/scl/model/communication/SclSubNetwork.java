package com.ysh.jcms.utils.scl.model.communication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import com.ysh.jcms.utils.scl.model.SclText;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclSubNetwork {

    private String name;
    private String desc;
    private String type;
    /** Text description (Text, type=tText, optional) */
    private SclText text;
    private String bitRate;
    private String bitRateUnit;

    private final List<SclConnectedAP> connectedAPs = new ArrayList<>();

    public SclSubNetwork addConnectedAP(SclConnectedAP connectedAP) {
        this.connectedAPs.add(connectedAP);
        return this;
    }

    public SclConnectedAP findConnectedAPByIedName(String iedName) {
        for (SclConnectedAP cap : connectedAPs) {
            if (cap.iedName().equals(iedName)) {
                return cap;
            }
        }
        return null;
    }
}
