package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsBits;
import com.ysh.jcms.data.InnerRcbOptFlds;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * RcbOptFlds ::= BIT STRING (SIZE(10)) — 7.6.4
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsRcbOptFlds extends CmsBits {

    @Bit(1) public boolean sequence_number;
    @Bit(2) public boolean report_time_stamp;
    @Bit(3) public boolean reason_for_inclusion;
    @Bit(4) public boolean data_set_name;
    @Bit(5) public boolean data_reference;
    @Bit(6) public boolean buffer_overflow;
    @Bit(7) public boolean entry_id;
    @Bit(8) public boolean conf_revision;
    @Bit(9) public boolean segmentation;

    public CmsRcbOptFlds() {
        super(new InnerRcbOptFlds());
    }
}
