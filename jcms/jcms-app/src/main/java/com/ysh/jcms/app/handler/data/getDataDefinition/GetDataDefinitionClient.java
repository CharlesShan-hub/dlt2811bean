package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.svc.data.CmsDataDefResultEntry;
import com.ysh.jcms.svc.data.CmsDataRefEntry;
import com.ysh.jcms.svc.data.CmsGetDataDefinitionError;
import com.ysh.jcms.svc.data.CmsGetDataDefinitionRequest;
import com.ysh.jcms.svc.data.CmsGetDataDefinitionResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetDataDefinitionClient extends BaseClientHandler {

    public static final class DefEntry {
        public final String cdcType;
        public final int choiceType;

        public DefEntry(String cdcType, int choiceType) {
            this.cdcType = cdcType;
            this.choiceType = choiceType;
        }
    }

    private List<DefEntry> lastEntries = new ArrayList<>();

    public GetDataDefinitionClient(CmsNode node) {
        super(node);
    }

    public List<DefEntry> getLastEntries() { return lastEntries; }

    public void execute(GetDataDefinitionDao dao) throws Exception {
        CmsGetDataDefinitionRequest req = new CmsGetDataDefinitionRequest()
            .reqId(nextReqId());

        for (GetDataDefinitionDao.DataRef ref : dao.dataRefs()) {
            CmsDataRefEntry entry = new CmsDataRefEntry()
                .reference(ref.reference());
            if (ref.fc() != null) {
                entry.fcPresent(true);
                entry.fc(ref.fc());
            }
            req.data.add(entry);
        }

        send(ServiceName.GET_DATA_DEFINITION, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataDefinitionError err = new CmsGetDataDefinitionError();
        err.decode(frame.asduBytes());
        throw new IOException("GetDataDefinition rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataDefinitionResponse resp = new CmsGetDataDefinitionResponse();
        resp.data.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        resp.decode(frame.asduBytes());
        traceResp(resp);

        List<DefEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.data.count; i++) {
            CmsDataDefResultEntry src = resp.data.items.get(i);
            int choice = src.definition.choice.value();
            if (choice == 0) continue;
            String cdc = src.cdcTypePresent.value()
                ? new String(src.cdcType.value(), StandardCharsets.UTF_8) : "";
            entries.add(new DefEntry(cdc, choice));
        }
        this.lastEntries = entries;
        log.info("GetDataDefinition succeeded: {} entries", entries.size());
    }
}
