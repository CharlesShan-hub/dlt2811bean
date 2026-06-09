package com.ysh.jcms2.data.fc;

import com.ysh.jcms2.data.string.CmsUint8Array;

/**
 * FunctionalConstraint ::= VisibleString (SIZE(2))  —  7.4
 * Fixed-size VisibleString, 2 bytes.
 *
 * Use CmsUint8Array directly. This subclass is kept for
 * type-name documentation only.
 */
public class CmsFunctionalConstraint extends CmsUint8Array {
}
