package com.ysh.jcms.utils.scl.model.communication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclGSE {

    private String ldInst;
    private String cbName;
    /** 最小时间 (MinTime), 毫秒 */
    private String minTime;
    /** 最大时间 (MaxTime), 毫秒 */
    private String maxTime;

    private final List<SclAddress> addresses = new ArrayList<>();

    public SclGSE addAddress(SclAddress address) {
        this.addresses.add(address);
        return this;
    }

    public SclAddress findAddressByType(String type) {
        for (SclAddress addr : addresses) {
            if (addr.type().equals(type)) {
                return addr;
            }
        }
        return null;
    }
}
