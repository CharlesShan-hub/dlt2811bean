package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsBits;
import com.ysh.jcms.data.InnerMsvcbOptFlds;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * MsvcbOptFlds ::= BIT STRING (SIZE(5)) — 7.6.6
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsMsvcbOptFlds extends CmsBits {

    @Bit(0) public boolean refresh_time;
    @Bit(2) public boolean sample_rate;
    @Bit(3) public boolean data_set_name;
    @Bit(4) public boolean security;

    public CmsMsvcbOptFlds() {
        super(new InnerMsvcbOptFlds());
    }
}
