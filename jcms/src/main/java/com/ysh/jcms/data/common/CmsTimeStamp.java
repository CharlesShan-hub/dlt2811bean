package com.ysh.jcms.data.common;

import com.ysh.jcms.data.time.CmsUtcTime;

/**
 * TimeStamp ::= UtcTime  —  7.3.4
 * PER encoding: same as UtcTime (OCTET STRING (SIZE(8))).
 *
 * Use CmsUtcTime directly — this subclass is kept for
 * type-name documentation only.
 */
public class CmsTimeStamp extends CmsUtcTime {
}
