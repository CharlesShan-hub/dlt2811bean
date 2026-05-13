package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDA;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDOType;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclSDO;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDirectory;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDirectoryEntry;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetDataDirectoryHandler extends AbstractCmsServiceHandler<CmsGetDataDirectory> {

    public GetDataDirectoryHandler() {
        super(ServiceName.GET_DATA_DIRECTORY, CmsGetDataDirectory::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetDataDirectory asdu = (CmsGetDataDirectory) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclIED.SclServer server = accessPoint.getServer();
        SclDataTypeTemplates templates = serverSession.getSclDataTypeTemplates();

        String ref = asdu.dataReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");

        SclIED.SclLDevice device = findLDevice(server, ldName);
        if (device == null) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (parts.length < 1) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String lnName = parts[0];
        String doName = parts.length > 1 ? parts[1] : null;

        String afterRef = asdu.referenceAfter.get();
        boolean skipUntilAfter = afterRef != null && !afterRef.isEmpty();

        List<CmsGetDataDirectoryEntry> allEntries;
        if (doName == null) {
            // LN level: list all DOs (merge instance + type templates)
            allEntries = buildLnDirectoryEntries(device, server, templates, lnName);
            if (allEntries == null) {
                return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        } else {
            // DO level: find specific DOI or use type templates
            SclIED.SclDOI doi = findDoiInDevice(device, lnName, doName);
            if (doi == null && templates != null) {
                // Try to list DAs from type templates
                allEntries = buildDoDirectoryEntriesFromType(server, templates, ldName, lnName, doName);
                if (allEntries == null) {
                    return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
                }
            } else if (doi == null) {
                return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            } else {
                allEntries = buildDirectoryEntries(doi, "", server, templates, ldName, lnName, doName);
            }
        }
        if (skipUntilAfter) {
            allEntries = filterAfter(allEntries, afterRef);
            if (allEntries == null) {
                return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }

        CmsArray<CmsGetDataDirectoryEntry> responseEntries = new CmsArray<>(CmsGetDataDirectoryEntry::new);
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

    private List<CmsGetDataDirectoryEntry> buildDirectoryEntries(SclIED.SclDOI doi, String prefix,
                                                                   SclIED.SclServer server, SclDataTypeTemplates templates,
                                                                   String ldName, String lnName, String doName) {
        List<CmsGetDataDirectoryEntry> entries = new ArrayList<>();

        for (SclIED.SclDAI dai : doi.getDais()) {
            CmsGetDataDirectoryEntry entry = new CmsGetDataDirectoryEntry();
            String entryRef = prefix + dai.getName();
            entry.reference.set(entryRef);
            String fc = SclTypeResolver.resolveFc(server, templates, ldName, lnName, doName, dai.getName());
            if (fc != null && !fc.isEmpty()) {
                entry.fc(fc);
            }
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

    /**
     * Builds directory entries for an LN by merging instance DOs with type template DOs.
     */
    private List<CmsGetDataDirectoryEntry> buildLnDirectoryEntries(SclIED.SclLDevice device,
                                                                     SclIED.SclServer server,
                                                                     SclDataTypeTemplates templates,
                                                                     String lnName) {
        // Get instance DOs
        List<SclIED.SclDOI> dois = findDoisInLn(device, lnName);

        // Get type template DOs
        List<String> typeDoNames = (templates != null)
            ? SclTypeResolver.listDoNamesFromType(server, templates, device.getInst(), lnName)
            : List.of();

        // Merge: instance DOs take priority, add missing type DOs
        Set<String> seen = new HashSet<>();
        List<CmsGetDataDirectoryEntry> entries = new ArrayList<>();

        if (dois != null) {
            for (SclIED.SclDOI doi : dois) {
                String name = doi.getName();
                seen.add(name);
                CmsGetDataDirectoryEntry entry = new CmsGetDataDirectoryEntry();
                entry.reference.set(name);
                entries.add(entry);
            }
        }

        for (String doName : typeDoNames) {
            if (!seen.contains(doName)) {
                seen.add(doName);
                CmsGetDataDirectoryEntry entry = new CmsGetDataDirectoryEntry();
                entry.reference.set(doName);
                entries.add(entry);
            }
        }

        return entries;
    }

    /**
     * Builds directory entries for a DO from type templates (when no instance DOI exists).
     */
    private List<CmsGetDataDirectoryEntry> buildDoDirectoryEntriesFromType(SclIED.SclServer server,
                                                                            SclDataTypeTemplates templates,
                                                                            String ldName, String lnName,
                                                                            String doName) {
        SclDOType doType = resolveDoType(server, templates, ldName, lnName, doName);
        if (doType == null) return null;

        List<CmsGetDataDirectoryEntry> entries = new ArrayList<>();
        buildDoDirectoryEntriesRecursive(templates, doType, "", entries);
        return entries;
    }

    private SclDOType resolveDoType(SclIED.SclServer server, SclDataTypeTemplates templates, String ldName, String lnName, String doName) {
        SclDataTypeTemplates.SclDO doObj = SclTypeResolver.findDoInType(server, templates, ldName, lnName, doName);
        if (doObj == null) return null;
        return templates.findDoTypeById(doObj.getType());
    }

    private void buildDoDirectoryEntriesRecursive(SclDataTypeTemplates templates, SclDOType doType,
                                                   String prefix, List<CmsGetDataDirectoryEntry> entries) {
        for (SclDA da : doType.getDas()) {
            CmsGetDataDirectoryEntry entry = new CmsGetDataDirectoryEntry();
            entry.reference.set(prefix + da.getName());
            if (da.getFc() != null && !da.getFc().isEmpty()) {
                entry.fc(da.getFc());
            }
            entries.add(entry);
        }
        for (SclSDO sdo : doType.getSdos()) {
            SclDOType sdoDoType = templates.findDoTypeById(sdo.getType());
            if (sdoDoType != null) {
                buildDoDirectoryEntriesRecursive(templates, sdoDoType,
                    prefix + sdo.getName() + ".", entries);
            }
        }
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

    private List<SclIED.SclDOI> findDoisInLn(SclIED.SclLDevice device, String lnName) {
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                return device.getLn0().getDois();
            }
        }
        for (SclIED.SclLN ln : device.getLns()) {
            String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                return ln.getDois();
            }
        }
        return null;
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
            String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
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
}
