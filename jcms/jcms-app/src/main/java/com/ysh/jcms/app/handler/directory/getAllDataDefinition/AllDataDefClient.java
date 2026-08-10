package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
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
        send(ServiceName.GET_ALL_DATA_DEFINITION, dao);
    }

    @Override
    protected void beforeAll(AllDataDefDao dao) throws IOException {
        CmsClientOperator.initResult(dao, "data");
        node.contentManager().initDataDef(new ArrayList<>()); // clear before fresh pull
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataDefinitionError err = decodeErr(frame, new CmsGetAllDataDefinitionError());
        throw new IOException("GetAllDataDefinition rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, AllDataDefDao dao) throws IOException {
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
        node.contentManager().addDataDef(entries);
        CmsClientOperator.page(dao).add("data", entries).moreFollows(resp.moreFollows.value()).lastRef(entries, e -> e.reference);
        log.info("GetAllDataDefinition page: {} entries (moreFollows={})", entries.size(), resp.moreFollows.value());
    }

    @Override
    protected void setPaginationCursor(AllDataDefDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
