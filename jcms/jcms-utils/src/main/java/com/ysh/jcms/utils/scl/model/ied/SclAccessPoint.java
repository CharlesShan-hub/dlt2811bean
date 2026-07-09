package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclAccessPoint {

    private String name;
    private Boolean router = false;
    private Boolean clock = false;
    private Boolean kdc = false;
    private SclServer server;
    private SclServerAt serverAt;
}
