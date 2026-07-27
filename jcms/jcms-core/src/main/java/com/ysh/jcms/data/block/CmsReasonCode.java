package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsBits;
import com.ysh.jcms.data.InnerReasonCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ReasonCode ::= BIT STRING (SIZE(7)) — 7.6.3
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsReasonCode extends CmsBits {

    @Bit(1) public boolean data_change;
    @Bit(2) public boolean quality_change;
    @Bit(3) public boolean data_update;
    @Bit(4) public boolean integrity;
    @Bit(5) public boolean general_interrogation;
    @Bit(6) public boolean application_trigger;

    public CmsReasonCode() {
        super(new InnerReasonCode());
    }
}
