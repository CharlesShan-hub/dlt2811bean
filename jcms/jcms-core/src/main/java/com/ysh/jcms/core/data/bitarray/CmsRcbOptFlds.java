package com.ysh.jcms.core.data.bitarray;

import com.ysh.jcms.data.core.CmsBits;
import com.ysh.jcms.data.InnerRcbOptFlds;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <pre>
 * {@code
 * RcbOptFlds ::= BIT STRING {
 *     reserved            (0),
 *     sequence-number     (1),
 *     report-time-stamp   (2),
 *     reason-for-inclusion(3),
 *     data-set-name       (4),
 *     data-reference      (5),
 *     buffer-overflow     (6),
 *     entryID             (7),
 *     conf-revision       (8),
 *     segmentation        (9)
 * } (SIZE(10)) — 7.6.4
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class CmsRcbOptFlds extends CmsBits {

    @Bit(1)
    public boolean sequence_number;
    @Bit(2)
    public boolean report_time_stamp;
    @Bit(3)
    public boolean reason_for_inclusion;
    @Bit(4)
    public boolean data_set_name;
    @Bit(5)
    public boolean data_reference;
    @Bit(6)
    public boolean buffer_overflow;
    @Bit(7)
    public boolean entry_id;
    @Bit(8)
    public boolean conf_revision;
    @Bit(9)
    public boolean segmentation;

    public CmsRcbOptFlds() {
        super(new InnerRcbOptFlds());
    }
}
