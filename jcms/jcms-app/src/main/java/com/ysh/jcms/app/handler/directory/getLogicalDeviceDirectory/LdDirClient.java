package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LdDirClient extends BaseClientHandler<LdDirDao> {

    /**
     * 获取逻辑设备目录。省略 ldName 时服务器可能分页（moreFollows=true）， 这里循环带上 referenceAfter
     * 续拉直到最后一页，合并全部结果。
     */
    @Override
    public void execute(LdDirDao dao) throws Exception {
        List<String> all = new ArrayList<>();
        String after = dao.referenceAfter();

        while (true) {
            dao.referenceAfter(after);
            Frame frame = send(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, dao.toRequest());
            CmsGetLogicalDeviceDirectoryResponse resp = decodeFrame(frame, new CmsGetLogicalDeviceDirectoryResponse());

            for (CmsSubReference ref : resp.lnReference) {
                all.add(ref.value());
            }
            if (!resp.moreFollows.value())
                break;
            if (resp.lnReference.isEmpty())
                break; // 防死循环：服务器说还有但没给数据
            after = all.get(all.size() - 1); // 从上一页最后一个引用续拉
        }

        node.getContentManager().initLdDir(all);
        log.info("GetLogicalDeviceDirectory succeeded: {} logical nodes", all.size());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalDeviceDirectoryError err = decodeErr(frame, new CmsGetLogicalDeviceDirectoryError());
        throw new IOException("GetLogicalDeviceDirectory rejected: " + err.value());
    }
}
