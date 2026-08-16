package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.info.CmsServiceInfo;

public class TestClient extends BaseClientHandler<TestDao> {

    @Override
    public void execute(TestDao dao) throws Exception {
        send(CmsServiceInfo.TEST, dao);
    }
}
