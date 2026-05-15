package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclServer {

    private final List<SclLDevice> lDevices = new ArrayList<>();

    public void addLDevice(SclLDevice ld) { this.lDevices.add(ld); }

    public SclLDevice findLDeviceByInst(String inst) {
        for (SclLDevice ld : lDevices) {
            if (ld.getInst().equals(inst)) return ld;
        }
        return null;
    }

    public List<String> getLDeviceNames() {
        return lDevices.stream().map(SclLDevice::getInst).toList();
    }

    public List<String> getLDeviceNames(String after) {
        List<String> names = getLDeviceNames();
        if (after == null || after.isEmpty()) return names;
        int idx = names.indexOf(after);
        if (idx < 0) return null;
        return names.subList(idx + 1, names.size());
    }

    public List<String> getAllLnNames() {
        List<String> names = new ArrayList<>();
        for (SclLDevice ld : lDevices) {
            names.addAll(ld.getLnNames());
        }
        return names;
    }

    public List<String> getAllLnNames(String after) {
        List<String> names = getAllLnNames();
        if (after == null || after.isEmpty()) return names;
        int idx = names.indexOf(after);
        if (idx < 0) return null;
        return names.subList(idx + 1, names.size());
    }

    /**
     * Finds an LN by reference string in the format "LDinst/LNFullName".
     * <p>For example: {@code findLnByRef("LD0/LLN0")} or {@code findLnByRef("LD0/PIOC1")}.
     *
     * @param lnReference the LN reference string (e.g. "LD0/LLN0")
     * @return the matching SclLN, or null if not found or invalid format
     */
    public SclLN findLnByRef(String lnReference) {
        if (lnReference == null || lnReference.isEmpty()) return null;
        int slashIdx = lnReference.indexOf('/');
        if (slashIdx < 0) return null;
        SclLDevice device = findLDeviceByInst(lnReference.substring(0, slashIdx));
        if (device == null) return null;
        return device.findLnByFullName(lnReference.substring(slashIdx + 1));
    }

    /**
     * Resolves a list of LNs from either an ldName or an lnReference.
     * <p>This is a common pattern used by directory services:
     * <ul>
     *   <li>{@code ldName = "LD0"} → returns all LNs in that logical device</li>
     *   <li>{@code lnReference = "LD0/LLN0"} → returns a single LN</li>
     * </ul>
     *
     * @param ldName      the logical device instance name, or null
     * @param lnReference the LN reference (LDinst/LNFullName), or null
     * @return list of matching LNs, or null if not found
     */
    public List<SclLN> resolveLns(String ldName, String lnReference) {
        if (ldName != null && !ldName.isEmpty()) {
            SclLDevice device = findLDeviceByInst(ldName);
            if (device == null) return null;
            return device.getLns();
        }
        if (lnReference == null || lnReference.isEmpty()) return null;
        SclLN ln = findLnByRef(lnReference);
        if (ln == null) return null;
        return List.of(ln);
    }

    public SclDataValue resolveDataValue(String ref, SclDataTypeTemplates templates) {
        return com.ysh.dlt2811bean.scl2.util.SclDataValueResolver.resolveDataValue(this, ref, templates);
    }

    public int setDataValue(String ref, String value, SclDataTypeTemplates templates) {
        return com.ysh.dlt2811bean.scl2.util.SclSetSetDataValueResolver.setDataValue(this, ref, value, templates);
    }

    public SclDataDefinitionEntry resolveDataDefinition(String ref, String fc, SclDataTypeTemplates templates) {
        return com.ysh.dlt2811bean.scl2.util.SclDataDefinitionResolver.resolveDataDefinition(this, ref, fc, templates);
    }

    public SclFCDA parseRefToFcda(String ref) {
        return com.ysh.dlt2811bean.scl2.util.SclDataSetResolver.parseRefToFcda(this, ref);
    }
}
