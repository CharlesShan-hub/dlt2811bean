package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllDataValuesClient extends BaseClientHandler<AllDataValuesDao> {

    @Override
    public void execute(AllDataValuesDao dao) throws Exception {
        CmsGetAllDataValuesRequest req = new CmsGetAllDataValuesRequest().referenceAfter(dao.referenceAfter());

        if (dao.ldName() != null) {
            req.reference.altLdName(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.altLnReference(dao.lnReference());
        }

        if (dao.fc() != null) {
            req.fc(dao.fc());
        }

        send(ServiceName.GET_ALL_DATA_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataValuesError err = decodeErr(frame, new CmsGetAllDataValuesError());
        throw new IOException("GetAllDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetAllDataValuesResponse resp = decodeResp(frame, new CmsGetAllDataValuesResponse());

        List<ContentManager.AllDataEntry> entries = new ArrayList<>();
        for (CmsDataValueEntry e : resp.data) {
            CmsDataValueEntryWrap entry = new CmsDataValueEntryWrap(e);
            if (entry.choiceType == 0)
                continue; // skip error/empty entries
            entries.add(new ContentManager.AllDataEntry(entry.reference, entry.choiceType, entry.valueString));
        }
        node.getContentManager().initAllData(entries);
        log.info("GetAllDataValues succeeded: {} entries", entries.size());
    }

    /** Wraps CmsDataValueEntry to extract readable values after native decode. */
    private static class CmsDataValueEntryWrap {
        final String reference;
        final int choiceType;
        final String valueString;

        CmsDataValueEntryWrap(CmsDataValueEntry e) {
            this.reference = e.reference.value();
            int ct = e.value.choice();
            this.choiceType = ct;
            this.valueString = e.value.toValueString();
        }

        // private static String bytesToHex(byte[] bytes) {
        // StringBuilder sb = new StringBuilder(bytes.length * 2);
        // for (byte b : bytes) sb.append(String.format("%02X", b & 0xFF));
        // return sb.toString();
        // }
    }
}
