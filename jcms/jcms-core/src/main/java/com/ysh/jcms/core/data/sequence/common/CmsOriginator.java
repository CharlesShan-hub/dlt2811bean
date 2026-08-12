package com.ysh.jcms.core.data.sequence.common;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerOriginator;
import com.ysh.jcms.data.enumerate.CmsOrCat;
import com.ysh.jcms.data.scalar.CmsOctetString;

/**
 * <pre>
 * {@code
 * Originator ::= SEQUENCE {
 *     orCat        [0] IMPLICIT INTEGER {
 *         notSupported      (0),
 *         bayControl        (1),
 *         stationControl    (2),
 *         remoteControl     (3),
 *         automaticBay      (4),
 *         automaticStation  (5),
 *         automaticRemote   (6),
 *         maintenance       (7),
 *         process           (8)
 *     } (0..8),
 *     orIdent      [1] IMPLICIT OCTET STRING (SIZE(0..64))
 * } — 7.5.2
 * }
 * </pre>
 */
public class CmsOriginator extends CmsSequence {

    @CmsField
    public CmsOrCat orCat;
    @CmsField
    public CmsOctetString orIdent;

    public CmsOriginator() {
        super(new InnerOriginator());
    }

    public CmsOriginator orCat(int v) {
        this.orCat.value(v);
        return this;
    }
    public CmsOriginator orIdent(byte[] v) {
        this.orIdent.value(v);
        return this;
    }

    /** Copy all field values from another CmsOriginator (fluent). */
    public CmsOriginator value(CmsOriginator v) {
        return orCat(v.orCat.value()).orIdent(v.orIdent.value());
    }
}
