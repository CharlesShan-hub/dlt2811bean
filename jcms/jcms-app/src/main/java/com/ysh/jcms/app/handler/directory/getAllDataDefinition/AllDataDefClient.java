package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.data.sequence.directory.CmsDataDefinitionEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllDataDefClient extends BaseClientHandler<AllDataDefDao> {

    @Override
    public void execute(AllDataDefDao dao) throws Exception {
        CmsGetAllDataDefinitionRequest req = new CmsGetAllDataDefinitionRequest().referenceAfter(dao.referenceAfter());

        if (dao.ldName() != null) {
            req.reference.altLdName(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.altLnReference(dao.lnReference());
        }

        if (dao.fc() != null) {
            req.fc(dao.fc());
        }

        send(ServiceName.GET_ALL_DATA_DEFINITION, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataDefinitionError err = decodeErr(frame, new CmsGetAllDataDefinitionError());
        throw new IOException("GetAllDataDefinition rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetAllDataDefinitionResponse resp = decodeResp(frame, new CmsGetAllDataDefinitionResponse());

        List<ContentManager.DataDefEntry> entries = new ArrayList<>();
        for (CmsDataDefinitionEntry src : resp.data) {
            int choice = src.definition.choice();
            if (choice == 0)
                continue; // skip error/empty entries
            String ref = src.reference.value();
            String cdc = src.isPresent("cdcType") ? src.cdcType.value() : "";
            entries.add(new ContentManager.DataDefEntry(ref, cdc, choice));
        }
        node.getContentManager().initDataDef(entries);
        log.info("GetAllDataDefinition succeeded: {} entries", entries.size());
    }
}
