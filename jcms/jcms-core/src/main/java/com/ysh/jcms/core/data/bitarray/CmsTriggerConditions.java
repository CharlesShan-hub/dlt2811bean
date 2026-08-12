package com.ysh.jcms.core.data.bitarray;

import com.ysh.jcms.core.data.core.CmsBits;
import com.ysh.jcms.data.InnerTriggerConditions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <pre>
 * {@code
 * TriggerConditions ::= BIT STRING {
 *     reserved              (0),
 *     data-change           (1),
 *     quality-change        (2),
 *     data-update           (3),
 *     integrity             (4),
 *     general-interrogation (5)
 * } (SIZE(6)) — 7.6.2
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class CmsTriggerConditions extends CmsBits {

    @Bit(1)
    public boolean data_change;
    @Bit(2)
    public boolean quality_change;
    @Bit(3)
    public boolean data_update;
    @Bit(4)
    public boolean integrity;
    @Bit(5)
    public boolean general_interrogation;

    public CmsTriggerConditions() {
        super(new InnerTriggerConditions());
    }
}
