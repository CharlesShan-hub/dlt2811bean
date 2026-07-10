package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.svc.data.CmsDataRefValueEntry;
import com.ysh.jcms.svc.data.CmsSetDataValuesError;
import com.ysh.jcms.svc.data.CmsSetDataValuesRequest;
import com.ysh.jcms.svc.data.CmsSetDataValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetDataValuesClient extends BaseClientHandler {

    public SetDataValuesClient(CmsNode node) {
        super(node);
    }

    public void execute(SetDataValuesDao dao) throws Exception {
        CmsSetDataValuesRequest req = new CmsSetDataValuesRequest().reqId(nextReqId());

        for (SetDataValuesDao.Entry src : dao.entries()) {
            CmsDataRefValueEntry entry = new CmsDataRefValueEntry().reference(src.reference());

            // Set value in-place (don't replace the CmsData field, as the
            // JNA native pointer is fixed at construction time)
            fillCmsData(entry.value, src.value());

            if (src.fc() != null && src.fc() != 0) {
                entry.fc(src.fc());
            }

            req.data.add(entry);
        }

        send(ServiceName.SET_DATA_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetDataValuesError err = new CmsSetDataValuesError();
        err.decode(frame.asduBytes());
        int errorCount = err.result.count;
        StringBuilder sb = new StringBuilder("SetDataValues rejected:");
        for (int i = 0; i < errorCount; i++) {
            sb.append(" [").append(i).append("] error=").append(err.result.items.get(i).value());
        }
        throw new IOException(sb.toString());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetDataValuesResponse resp = new CmsSetDataValuesResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);
        log.info("SetDataValues succeeded");
    }

    /**
     * Set CmsData fields in-place with a string value. Modifies the existing
     * CmsData rather than creating a new one, because JNA native pointers are fixed
     * at construction time.
     */
    private static void fillCmsData(CmsData data, String value) {
        if (containsNonAscii(value)) {
            data.choice(CmsData.CHOICE_UNICODE_STRING);
            data.alt_unicode_string.value(value);
        } else {
            data.choice(CmsData.CHOICE_VISIBLE_STRING);
            data.alt_visible_string.value(value);
        }
    }

    private static boolean containsNonAscii(String s) {
        if (s == null)
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > 127)
                return true;
        }
        return false;
    }
}
