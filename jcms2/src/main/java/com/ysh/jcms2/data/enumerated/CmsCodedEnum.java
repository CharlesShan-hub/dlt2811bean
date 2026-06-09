package com.ysh.jcms2.data.enumerated;

import com.ysh.jcms2.data.string.CmsUint8Array;

/**
 * CODEDENUM ::= BIT STRING (SIZE(0..n))  —  7.1.7
 * PER encoding: same as BitString (constrained length + align + bits).
 *
 * Use CmsUint8Array directly. len stores the number of bits (nbits).
 */
public class CmsCodedEnum extends CmsUint8Array {
}
