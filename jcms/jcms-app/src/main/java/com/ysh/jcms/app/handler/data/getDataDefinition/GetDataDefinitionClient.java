package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.sequence.data.CmsDataDefResultEntry;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;
import com.ysh.jcms.pdu.data.CmsGetDataDefinitionError;
import com.ysh.jcms.pdu.data.CmsGetDataDefinitionRequest;
import com.ysh.jcms.pdu.data.CmsGetDataDefinitionResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDataDefinitionClient extends BaseClientHandler<GetDataDefinitionDao> {

    public static final class DefEntry {
        public final String cdcType;
        public final int choiceType;

        public DefEntry(String cdcType, int choiceType) {
            this.cdcType = cdcType;
            this.choiceType = choiceType;
        }
    }

    private List<DefEntry> lastEntries = new ArrayList<>();

    public List<DefEntry> getLastEntries() {
        return lastEntries;
    }

    @Override
    public void execute(GetDataDefinitionDao dao) throws Exception {
        CmsGetDataDefinitionRequest req = new CmsGetDataDefinitionRequest();

        for (GetDataDefinitionDao.DataRef ref : dao.dataRefs()) {
            CmsDataRefEntry entry = new CmsDataRefEntry().reference(ref.reference());
            if (ref.fc() != null) {
                entry.fc(ref.fc());
            }
            req.data.add(entry);
        }

        send(ServiceName.GET_DATA_DEFINITION, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataDefinitionError err = decodeErr(frame, new CmsGetDataDefinitionError());
        throw new IOException("GetDataDefinition rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataDefinitionResponse resp = decodeResp(frame, new CmsGetDataDefinitionResponse());

        List<DefEntry> entries = new ArrayList<>();
        for (CmsDataDefResultEntry src : resp.data) {
            int choice = src.definition.choice();
            if (choice == 0)
                continue;
            String cdc = src.isPresent("cdcType") ? src.cdcType.value() : "";
            entries.add(new DefEntry(cdc, choice));
        }
        this.lastEntries = entries;
        log.info("GetDataDefinition succeeded: {} entries", entries.size());
    }
}
