package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclDataDirectoryEntry;
import com.ysh.dlt2811bean.scl2.model.SclDOI;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.util.SclFilters;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDirectory;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDirectoryEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.List;

public class GetDataDirectoryHandler extends AbstractCmsServiceHandler<CmsGetDataDirectory> {

    public GetDataDirectoryHandler() {
        super(ServiceName.GET_DATA_DIRECTORY, CmsGetDataDirectory::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        String ref = asdu.dataReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        // parse reference: "LD0/LLN0" or "LD0/LLN0.Pos"
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");
        if (parts.length < 1) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String lnName = parts[0];
        String doName = parts.length > 1 ? parts[1] : null;

        // resolve LN
        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // collect directory entries
        List<SclDataDirectoryEntry> allEntries;
        if (doName == null) {
            // LN level: list DOs
            allEntries = ln.collectDataDirectory(sclDocument.getDataTypeTemplates());
        } else {
            // DO level: list DAs and SDIs
            SclDOI doi = ln.findDoiByName(doName);
            if (doi == null) {
                return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            allEntries = doi.collectDataDirectory(sclDocument.getDataTypeTemplates(), ln);
        }

        // referenceAfter pagination
        String afterRef = asdu.referenceAfter.get();
        List<SclDataDirectoryEntry> filtered = SclFilters.filterAfter(allEntries, afterRef, SclDataDirectoryEntry::ref);
        if (filtered == null && afterRef != null && !afterRef.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }
        if (filtered != null) allEntries = filtered;

        // build response
        CmsArray<CmsGetDataDirectoryEntry> responseEntries = new CmsArray<>(CmsGetDataDirectoryEntry::new);
        for (SclDataDirectoryEntry e : allEntries) {
            CmsGetDataDirectoryEntry entry = new CmsGetDataDirectoryEntry().reference(e.ref());
            if (e.fc() != null && !e.fc().isEmpty()) {
                entry.fc(e.fc());
            }
            responseEntries.add(entry);
        }

        CmsGetDataDirectory response = new CmsGetDataDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.dataAttribute = responseEntries;
        response.moreFollows.set(false);

        log.debug("[Server] GetDataDirectory: '{}' -> {} entries", ref, responseEntries.size());
        return new CmsApdu(response);
    }
}
