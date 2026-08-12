package com.ysh.jcms.core.data.bitarray;

import com.ysh.jcms.data.core.CmsBits;
import com.ysh.jcms.data.InnerMsvcbOptFlds;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <pre>
 * {@code
 * MsvcbOptFlds ::= BIT STRING {
 *     refresh-time  (0),
 *     reserved      (1),
 *     sample-rate   (2),
 *     data-set-name (3),
 *     security      (4)
 * } (SIZE(5)) — 7.6.6
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class CmsMsvcbOptFlds extends CmsBits {

    @Bit(0)
    public boolean refresh_time;
    @Bit(2)
    public boolean sample_rate;
    @Bit(3)
    public boolean data_set_name;
    @Bit(4)
    public boolean security;

    public CmsMsvcbOptFlds() {
        super(new InnerMsvcbOptFlds());
    }
}
