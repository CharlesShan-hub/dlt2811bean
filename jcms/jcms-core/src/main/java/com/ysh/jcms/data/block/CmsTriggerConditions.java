package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsBitString;
import com.ysh.jcms.data.InnerTriggerConditions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * TriggerConditions ::= BIT STRING (SIZE(6)) — 7.6.2
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsTriggerConditions extends CmsBitString {

    @Bit(1) public boolean data_change;
    @Bit(2) public boolean quality_change;
    @Bit(3) public boolean data_update;
    @Bit(4) public boolean integrity;
    @Bit(5) public boolean general_interrogation;

    public CmsTriggerConditions() {
        super(new InnerTriggerConditions());
    }
}
