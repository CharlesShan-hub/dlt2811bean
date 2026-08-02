package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.pdu.sg.CmsGetEditSgValueError;
import com.ysh.jcms.pdu.sg.CmsGetEditSgValueRequest;
import com.ysh.jcms.pdu.sg.CmsGetEditSgValueResponse;
import com.ysh.jcms.data.sequence.sg.CmsSgRefFcEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetEditSgValueClient extends BaseClientHandler {

    public static final class ValueEntry {
        public final int choice;
        public final String text;
        public ValueEntry(int choice, String text) {
            this.choice = choice;
            this.text = text;
        }
    }

    private List<ValueEntry> lastValues = new ArrayList<>();
    public List<ValueEntry> getLastValues() {
        return lastValues;
    }

    public void execute(GetEditSgValueDao dao) throws Exception {
        CmsGetEditSgValueRequest req = new CmsGetEditSgValueRequest();
        for (GetEditSgValueDao.RefFcPair pair : dao.refs()) {
            req.data.add(new CmsSgRefFcEntry().reference(pair.reference()).fc(pair.fc()));
        }
        send(ServiceName.GET_EDIT_SG_VALUE, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetEditSgValueError err = decodeErr(frame, new CmsGetEditSgValueError());
        throw new IOException("GetEditSGValue rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetEditSgValueResponse resp = decodeResp(frame, new CmsGetEditSgValueResponse());

        List<ValueEntry> values = new ArrayList<>();
        for (CmsData data : resp.value) {
            values.add(new ValueEntry(data.choice(), describeValue(data)));
        }
        this.lastValues = values;
        log.info("GetEditSGValue succeeded: {} values", values.size());
    }

    private static String describeValue(CmsData data) {
        int choice = data.choice();
        try {
            switch (choice) {
                case CmsData.CHOICE_BOOLEAN :
                    return String.valueOf(data.alt_boolean.value());
                case CmsData.CHOICE_INT32 :
                    return String.valueOf(data.alt_int32.value());
                case CmsData.CHOICE_FLOAT32 :
                    return String.valueOf(data.alt_float32.value());
                case CmsData.CHOICE_VISIBLE_STRING :
                    return (String) data.alt_visible_string.toJsonValue();
                default :
                    return "choice=" + choice;
            }
        } catch (Exception e) {
            return "choice=" + choice;
        }
    }
}
