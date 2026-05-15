package com.ysh.dlt2811bean.scl2.query;

import com.ysh.dlt2811bean.scl2.model.*;
import com.ysh.dlt2811bean.scl2.ref.SclRef;
import com.ysh.dlt2811bean.scl2.ref.SclRefParser;
import com.ysh.dlt2811bean.scl2.ref.SclRefResult;
import com.ysh.dlt2811bean.scl2.ref.SclRefValidator;

import java.util.*;

public class SclQuery {

    private final SclDocument document;
    private final SclRefValidator validator;

    public SclQuery(SclDocument document) {
        this.document = document;
        this.validator = new SclRefValidator(document);
    }

    public SclDocument document() { return document; }

    public SclRefValidator validator() { return validator; }

    public SclRefResult validate(String ref) { return validator.validate(ref); }

    public boolean isValid(String ref) { return validator.isValid(ref); }

    public SclRef parse(String ref) { return SclRefParser.parse(ref); }

    public List<SclIED> ieds() { return document.getIeds(); }

    public Optional<SclIED> ied(String name) {
        return document.getIeds().stream()
            .filter(i -> i.getName().equals(name))
            .findFirst();
    }

    public IedQuery iedQuery(String name) {
        return ied(name).map(IedQuery::new)
            .orElseThrow(() -> new IllegalArgumentException("IED not found: " + name));
    }

    public List<String> iedNames() {
        return document.getIeds().stream()
            .map(SclIED::getName)
            .toList();
    }

    public List<SclIED> iedsByFilter(String namePattern) {
        String regex = namePattern.replace("*", ".*");
        return document.getIeds().stream()
            .filter(i -> i.getName().matches(regex))
            .toList();
    }

    public Optional<SclLDevice> lDevice(String ldInst) {
        return document.getIeds().stream()
            .flatMap(ied -> ied.getAccessPoints().stream())
            .map(SclAccessPoint::getServer)
            .filter(Objects::nonNull)
            .flatMap(server -> server.getLDevices().stream())
            .filter(ld -> ld.getInst().equals(ldInst))
            .findFirst();
    }

    public List<String> lDeviceNames() {
        return lDeviceNames(null);
    }

    public List<String> lDeviceNames(String after) {
        List<String> names = document.getIeds().stream()
            .flatMap(ied -> ied.getAccessPoints().stream())
            .map(SclAccessPoint::getServer)
            .filter(Objects::nonNull)
            .flatMap(server -> server.getLDevices().stream())
            .map(SclLDevice::getInst)
            .toList();
        if (after == null || after.isEmpty()) return names;
        int idx = names.indexOf(after);
        if (idx < 0) return List.of();
        return names.subList(idx + 1, names.size());
    }

    public Optional<SclLN> ln(String ldInst, String lnFullName) {
        return lDevice(ldInst)
            .flatMap(ld -> Optional.ofNullable(ld.findLnByFullName(lnFullName)));
    }

    public List<String> lnNames(String ldInst) {
        return lDevice(ldInst)
            .map(ld -> ld.getLns().stream()
                .map(SclLN::getFullName)
                .toList())
            .orElse(List.of());
    }

    public List<String> lnNamesByClass(String ldInst, String lnClass) {
        return lDevice(ldInst)
            .map(ld -> ld.findLnsByClass(lnClass).stream()
                .map(SclLN::getFullName)
                .toList())
            .orElse(List.of());
    }

    public List<String> doNames(String ldInst, String lnFullName) {
        return ln(ldInst, lnFullName)
            .map(ln -> ln.getDois().stream()
                .map(SclDOI::getName)
                .toList())
            .orElse(List.of());
    }

    public List<String> daNames(String ldInst, String lnFullName, String doName) {
        return ln(ldInst, lnFullName)
            .map(ln -> {
                SclDOI doi = ln.findDoiByName(doName);
                if (doi == null) return List.<String>of();
                return doi.getDais().stream()
                    .map(SclDAI::getName)
                    .toList();
            })
            .orElse(List.of());
    }

    public Optional<String> resolveBType(String refStr) {
        SclRef ref = SclRefParser.parse(refStr);
        return resolveBType(ref);
    }

    public Optional<String> resolveBType(SclRef ref) {
        SclDataTypeTemplates templates = document.getDataTypeTemplates();
        if (templates == null) return Optional.empty();

        Optional<SclLN> lnOpt = ln(ref.getLdName(), ref.getLnName());
        if (lnOpt.isEmpty()) return Optional.empty();

        SclLN ln = lnOpt.get();
        SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
        if (lnt == null) return Optional.empty();

        SclDO doDef = lnt.findDoByName(ref.getDoName());
        if (doDef == null) return Optional.empty();

        SclDOType doType = templates.findDoTypeById(doDef.getType());
        if (doType == null) return Optional.empty();

        if (!ref.hasDa()) {
            return Optional.of(doType.getCdc());
        }

        SclDA da = doType.findDaByName(ref.getDaName());
        if (da == null) return Optional.empty();

        if ("Struct".equals(da.getBType()) && da.getType() != null) {
            SclDAType daType = templates.findDaTypeById(da.getType());
            if (daType != null && !daType.getBdas().isEmpty()) {
                return Optional.of(daType.getBdas().get(0).getBType());
            }
        }

        return Optional.ofNullable(da.getBType());
    }

    public Optional<String> resolveCdc(String ldInst, String lnFullName, String doName) {
        SclDataTypeTemplates templates = document.getDataTypeTemplates();
        if (templates == null) return Optional.empty();

        return ln(ldInst, lnFullName)
            .map(ln -> templates.findLNodeTypeById(ln.getLnType()))
            .filter(Objects::nonNull)
            .map(lnt -> lnt.findDoByName(doName))
            .filter(Objects::nonNull)
            .map(SclDO::getType)
            .map(templates::findDoTypeById)
            .filter(Objects::nonNull)
            .map(SclDOType::getCdc);
    }

    public List<SclDataSet> dataSets(String ldInst, String lnFullName) {
        return ln(ldInst, lnFullName)
            .map(SclLN::getDataSets)
            .orElse(List.of());
    }

    public Optional<SclDataSet> dataSet(String ldInst, String lnFullName, String dsName) {
        return ln(ldInst, lnFullName)
            .map(ln -> ln.findDataSetByName(dsName));
    }

    public List<SclReportControl> reportControls(String ldInst, String lnFullName) {
        return ln(ldInst, lnFullName)
            .map(SclLN::getReportControls)
            .orElse(List.of());
    }

    public List<SclGSEControl> gseControls(String ldInst, String lnFullName) {
        return ln(ldInst, lnFullName)
            .map(SclLN::getGseControls)
            .orElse(List.of());
    }

    public DataTypeQuery dataTypes() {
        return new DataTypeQuery(document.getDataTypeTemplates());
    }

    public SclCommunication communication() { return document.getCommunication(); }

    public Optional<SclConnectedAP> connectedAP(String iedName, String apName) {
        SclCommunication comm = document.getCommunication();
        if (comm == null) return Optional.empty();
        return comm.getSubNetworks().stream()
            .map(sn -> sn.findConnectedAPByIedName(iedName))
            .filter(cap -> cap != null && cap.getApName().equals(apName))
            .findFirst();
    }

    public Map<String, List<String>> allLDevicesByIed() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (SclIED ied : document.getIeds()) {
            List<String> ldInsts = ied.getAccessPoints().stream()
                .map(SclAccessPoint::getServer)
                .filter(Objects::nonNull)
                .flatMap(server -> server.getLDevices().stream())
                .map(SclLDevice::getInst)
                .toList();
            result.put(ied.getName(), ldInsts);
        }
        return result;
    }
}
