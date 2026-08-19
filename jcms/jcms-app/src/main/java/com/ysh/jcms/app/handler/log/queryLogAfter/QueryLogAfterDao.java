package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.log.CmsQueryLogAfterRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class QueryLogAfterDao extends BaseDao {

    /** Log reference */
    private String logRef;

    /** Entry ID (8-byte, zero-padded) */
    private String entryId;

    /** Start time as ms since 1970-01-01 */
    private Long startTime;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(logRef, "logRef must not be null");
        return new CmsQueryLogAfterRequest()
            .logReference(logRef)
            .startTime(startTime)
            .entry(entryId);
    }
}
