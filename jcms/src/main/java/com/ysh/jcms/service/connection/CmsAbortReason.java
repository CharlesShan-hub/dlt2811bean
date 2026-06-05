package com.ysh.jcms.service.connection;
import com.sun.jna.Structure;

import com.ysh.jcms.ffi.CmsScalar;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsAbortReason extends CmsScalar {
    public static final int OTHER                             = 0;
    public static final int UNRECOGNIZED_SERVICE              = 1;
    public static final int INVALID_REQ_ID                    = 2;
    public static final int INVALID_ARGUMENT                  = 3;
    public static final int INVALID_RESULT                    = 4;
    public static final int MAX_SERV_OUTSTANDING_EXCEEDED     = 5;

    public int value;

    public static class ByValue extends CmsAbortReason implements Structure.ByValue {}
}