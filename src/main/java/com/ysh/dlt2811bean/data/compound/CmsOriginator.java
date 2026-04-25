package com.ysh.dlt2811bean.data.compound;

import com.ysh.dlt2811bean.data.enumerated.CmsOrCat;
import com.ysh.dlt2811bean.data.string.CmsOctetString;
import com.ysh.dlt2811bean.data.type.AbstractCmsCompound;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 originator type (§7.5.2).
 *
 * <p>ASN.1 definition from DL/T 2811:
 * <pre>
 * Originator ::= SEQUENCE {
 *     orCat   [0] IMPLICIT INTEGER (0..8),
 *     orIdent [1] IMPLICIT OCTET STRING (SIZE(0..64))
 * }
 * </pre>
 *
 * <pre>
 * ┌──────────┬───────────────────┬────────────────────┬───────────┐
 * │ Field    │ ASN.1 Type        │ Constraints        │ Java type │
 * ├──────────┼───────────────────┼────────────────────┼───────────┤
 * │ orCat    │ INTEGER           │ (0..8)             │ int       │
 * │ orIdent  │ OCTET STRING      │ SIZE(0..64)        │ byte[]    │
 * └──────────┴───────────────────┴────────────────────┴───────────┘
 * </pre>
 *
 * <pre>
 * // Chain usage
 * CmsOriginator orig = new CmsOriginator()
 *     .orCat(new CmsOrCat(CmsOrCat.BAY_CONTROL))
 *     .orIdent(new CmsOctetString(new byte[]{0x01, 0x02}).max(64));
 *
 * // Quick mode
 * CmsOriginator orig = new CmsOriginator(CmsOrCat.BAY_CONTROL, new byte[]{0x01, 0x02});
 *
 * // Encode / Decode
 * orig.encode(pos);
 * CmsOriginator r = new CmsOriginator().decode(pis);
 * </pre>
 */
@Setter
@Accessors(fluent = true)
public class CmsOriginator extends AbstractCmsCompound<CmsOriginator> {

    public CmsOrCat orCat = new CmsOrCat();
    public CmsOctetString orIdent = new CmsOctetString().max(64);

    public CmsOriginator() {
        super("Originator");
        registerField("orCat");
        registerField("orIdent");
    }

    public CmsOriginator(int orCat, byte[] orIdent) {
        this();
        this.orCat.set(orCat);
        this.orIdent.set(orIdent);
    }
}