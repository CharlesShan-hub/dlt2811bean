package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.data.sequence.directory.CmsDataDefinitionEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataDefinitionResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllDataDefClient extends BaseClientHandler<AllDataDefDao> {

    @Override
    public void execute(AllDataDefDao dao) throws Exception {
        node.getContentManager().initDataDef(new ArrayList<>()); // clear before fresh pull
        send(ServiceName.GET_ALL_DATA_DEFINITION, dao);
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
        node.getContentManager().addDataDef(entries);
        lastMoreFollows(resp.moreFollows.value());
        if (resp.data.size() > 0) {
            lastReference(resp.data.get(resp.data.size() - 1).reference.value());
        }
        log.info("GetAllDataDefinition page: {} entries (moreFollows={})", entries.size(), lastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(AllDataDefDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
