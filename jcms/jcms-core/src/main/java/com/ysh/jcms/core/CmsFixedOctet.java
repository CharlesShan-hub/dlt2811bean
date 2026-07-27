package com.ysh.jcms.core;

import com.ysh.jcms.data.InnerBase;

/**
 * Marker base for types backed by a fixed-size OCTET STRING Inner*.
 *
 * <p>These types store their value as a {@code byte[]} in the Inner*'s public
 * {@code value} field, without structured syncToInner/syncFromInner mappings.
 *
 * <p>Examples: {@link com.ysh.jcms.data.scalar.CmsFloat32} (SIZE(4)),
 * {@link com.ysh.jcms.data.scalar.CmsFloat64} (SIZE(8)).
 */
public abstract class CmsFixedOctet extends CmsScalar {
    protected CmsFixedOctet() {}
    protected CmsFixedOctet(InnerBase inner) { super(inner); }
}
