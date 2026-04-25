package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.type.AbstractCmsPackedList;
import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;

/**
 * DL/T 2811 PACKED LIST type (§7.1.8).
 *
 * <p>Variable-length bit string (0..max bits) with length prefix encoded
 * as a constrained integer. Each bit represents a named flag/semantics
 * defined by the data model.
 *
 * <pre>
 * // Construct
 * CmsPackedListImpl list = new CmsPackedListImpl(8);  // max 8 bits
 *
 * // Set individual bits
 * list.setBit(0, true).setBit(2, true);
 *
 * // Encode / Decode
 * list.encode(pos);
 * CmsPackedListImpl r = new CmsPackedListImpl(8).decode(pis);
 *
 * // Access
 * r.get();        // → long value
 * r.testBit(0);   // true
 * r.testBit(1);   // false
 * </pre>
 */
public class CmsPackedListImpl extends AbstractCmsPackedList<CmsPackedListImpl> {

    public CmsPackedListImpl(int max) {
        super("PACKED LIST", 0L, max);
    }

    public CmsPackedListImpl(long value, int bitLength, int max) {
        super("PACKED LIST", value, max);
        setBitLength(bitLength);
    }

    @Override
    public CmsPackedListImpl copy() {
        return new CmsPackedListImpl(get(), getBitLength(), getMax());
    }

    @Override
    public CmsPackedListImpl decode(PerInputStream pis) throws PerDecodeException {
        return super.decode(pis);
    }
}