package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.log.CmsQueryLogByTimeRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class QueryLogByTimeDao extends BaseDao {

    /** Log reference */
    private String logRef;

    /** Start time as ms since 1970-01-01 */
    private Long startTime;

    /** Stop time as ms since 1970-01-01 */
    private Long stopTime;

    /** Return entries after this 8-byte EntryID */
    private String entryAfter;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(logRef, "logRef must not be null");
        return new CmsQueryLogByTimeRequest()
            .logReference(logRef)
            .startTime(startTime)
            .stopTime(stopTime)
            .entryAfter(entryAfter);
    }
}
