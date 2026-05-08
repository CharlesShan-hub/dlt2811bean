package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDirectory;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDirectoryEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GetDataDirectoryHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataDirectoryHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_DATA_DIRECTORY;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetDataDirectory: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetDataDirectory) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsGetDataDirectory asdu = (CmsGetDataDirectory) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ref = asdu.dataReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");

        SclIED.SclLDevice device = findLDevice(accessPoint.getServer(), ldName);
        if (device == null) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (parts.length < 1) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String lnName = parts[0];
        String doName = parts.length > 1 ? parts[1] : null;

        SclIED.SclDOI doi = findDoiInDevice(device, lnName, doName);
        if (doi == null) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String afterRef = asdu.referenceAfter.get();
        boolean skipUntilAfter = afterRef != null && !afterRef.isEmpty();

        List<CmsGetDataDirectoryEntry> allEntries = buildDirectoryEntries(doi, "");
        if (skipUntilAfter) {
            allEntries = filterAfter(allEntries, afterRef);
            if (allEntries == null) {
                return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }

        CmsArray<CmsGetDataDirectoryEntry> responseEntries = new CmsArray<>(CmsGetDataDirectoryEntry::new).capacity(100);
        for (CmsGetDataDirectoryEntry entry : allEntries) {
            responseEntries.add(entry);
        }

        CmsGetDataDirectory response = new CmsGetDataDirectory(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.dataAttribute = responseEntries;
        response.moreFollows.set(false);

        log.debug("[Server] GetDataDirectory: '{}' -> {} entries", ref, responseEntries.size());
        return new CmsApdu(response);
    }

    private List<CmsGetDataDirectoryEntry> buildDirectoryEntries(SclIED.SclDOI doi, String prefix) {
        List<CmsGetDataDirectoryEntry> entries = new ArrayList<>();

        for (SclIED.SclDAI dai : doi.getDais()) {
            CmsGetDataDirectoryEntry entry = new CmsGetDataDirectoryEntry();
            String entryRef = prefix + dai.getName();
            entry.reference.set(entryRef);
            entries.add(entry);
        }

        for (SclIED.SclSDI sdi : doi.getSdis()) {
            String sdiPrefix = prefix + sdi.getName() + ".";
            entries.addAll(buildDirectoryEntriesFromSdi(sdi, sdiPrefix));
        }

        return entries;
    }

    private List<CmsGetDataDirectoryEntry> buildDirectoryEntriesFromSdi(SclIED.SclSDI sdi, String prefix) {
        List<CmsGetDataDirectoryEntry> entries = new ArrayList<>();

        for (SclIED.SclDAI dai : sdi.getDais()) {
            CmsGetDataDirectoryEntry entry = new CmsGetDataDirectoryEntry();
            String entryRef = prefix + dai.getName();
            entry.reference.set(entryRef);
            entries.add(entry);
        }

        return entries;
    }

    private List<CmsGetDataDirectoryEntry> filterAfter(List<CmsGetDataDirectoryEntry> all, String afterRef) {
        boolean found = false;
        List<CmsGetDataDirectoryEntry> result = new ArrayList<>();
        for (CmsGetDataDirectoryEntry entry : all) {
            String ref = entry.reference.get();
            if (!found) {
                if (ref != null && ref.equals(afterRef)) {
                    found = true;
                }
                continue;
            }
            result.add(entry);
        }
        return found ? result : null;
    }

    private SclIED.SclDOI findDoiInDevice(SclIED.SclLDevice device, String lnName, String doName) {
        if (doName == null) return null;
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                for (SclIED.SclDOI doi : device.getLn0().getDois()) {
                    if (doi.getName().equals(doName)) return doi;
                }
                return null;
            }
        }
        for (SclIED.SclLN ln : device.getLns()) {
            String curLnName = ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                for (SclIED.SclDOI doi : ln.getDois()) {
                    if (doi.getName().equals(doName)) return doi;
                }
                return null;
            }
        }
        return null;
    }

    private SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice device : server.getLDevices()) {
            if (device.getInst().equals(ldName)) {
                return device;
            }
        }
        return null;
    }

    private CmsApdu buildNegativeResponse(CmsGetDataDirectory request, int errorCode) {
        CmsGetDataDirectory response = new CmsGetDataDirectory(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
