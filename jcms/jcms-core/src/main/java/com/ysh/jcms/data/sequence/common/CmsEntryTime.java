package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.sequence.time.CmsBinaryTime;

/**
 * EntryTime ::= BinaryTime — 7.3.9 PER encoding: same as BinaryTime (OCTET
 * STRING (SIZE(6))).
 *
 * Use CmsBinaryTime directly — this subclass is kept for type-name
 * documentation only.
 */
public class CmsEntryTime extends CmsBinaryTime {
}
