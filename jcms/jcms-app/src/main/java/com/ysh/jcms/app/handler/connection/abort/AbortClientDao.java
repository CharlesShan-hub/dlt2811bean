package com.ysh.jcms.app.handler.connection.abort;

import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class AbortClientDao {
    private int reason;
}
