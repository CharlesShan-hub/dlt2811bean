package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.data.sequence.directory.CmsDataDefinitionEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionResponse;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AllDataDefClient extends BaseClientHandler {

    public void execute(AllDataDefDao dao) throws Exception {
        CmsGetAllDataDefinitionRequest req = new CmsGetAllDataDefinitionRequest().reqId(nextReqId()).refAfter(dao.referenceAfter());

        if (dao.ldName() != null) {
            req.reference.choice(CmsReferenceChoice.LD_NAME);
            req.reference.altLdName.value(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.choice(CmsReferenceChoice.LN_REFERENCE);
            req.reference.altLnReference.value(dao.lnReference());
        }

        if (dao.fc() != null) {
            req.fc(dao.fc());
        }

        send(ServiceName.GET_ALL_DATA_DEFINITION, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataDefinitionError err = decodeErr(frame, new CmsGetAllDataDefinitionError());
        throw new IOException("GetAllDataDefinition rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetAllDataDefinitionResponse resp = decodeResp(frame, new CmsGetAllDataDefinitionResponse());

        List<ContentManager.DataDefEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.data.count; i++) {
            CmsDataDefinitionEntry src = resp.data.items.get(i);
            int choice = src.definition.choice.value();
            if (choice == 0)
                continue; // skip error/empty entries
            String ref = new String(src.reference.value(), StandardCharsets.UTF_8);
            String cdc = src.cdcTypePresent.value() ? new String(src.cdcType.value(), StandardCharsets.UTF_8) : "";
            entries.add(new ContentManager.DataDefEntry(ref, cdc, choice));
        }
        node.getContentManager().initDataDef(entries);
        log.info("GetAllDataDefinition succeeded: {} entries", entries.size());
    }
}
