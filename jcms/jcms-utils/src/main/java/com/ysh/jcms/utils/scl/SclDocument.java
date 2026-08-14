package com.ysh.jcms.utils.scl;

import com.ysh.jcms.utils.scl.model.header.SclHeader;
import com.ysh.jcms.utils.scl.model.substation.SclSubstation;
import com.ysh.jcms.utils.scl.model.communication.SclCommunication;
import com.ysh.jcms.utils.scl.model.ied.*;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import lombok.AccessLevel;
import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclDocument {
    public enum SclFileType {
        SCD, ICD, CID, UNKNOWN
    }

    private String xmlns = "http://www.iec.ch/61850/2006/SCL";
    private String xsiSchemaLocation = "SCL.xsd";
    private SclFileType fileType = SclFileType.UNKNOWN;
    private String originalFilePath;
    private SclHeader header;
    private SclSubstation substation;
    private SclCommunication communication;
    private final List<SclIED> ieds = new ArrayList<>();
    private SclDataTypeTemplates dataTypeTemplates;
    private final List<String> unsupportedElements = new ArrayList<>();

    /**
     * Lazy index: IED name → IED (built on first lookup, invalidated by addIed).
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient Map<String, SclIED> iedIndex;
    /**
     * Lazy cache: all LD instance names (built on first lookup, invalidated by
     * addIed).
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient List<String> ldNamesCache;

    public SclDocument addIed(SclIED ied) {
        ieds.add(ied);
        iedIndex = null;
        ldNamesCache = null;
        return this;
    }
    public SclDocument addUnsupportedElement(String element) {
        unsupportedElements.add(element);
        return this;
    }
    public boolean hasUnsupportedElements() {
        return !unsupportedElements.isEmpty();
    }

    /** Look up an IED by name in O(1) (lazy index). */
    public SclIED ied(String name) {
        if (iedIndex == null) {
            Map<String, SclIED> idx = new HashMap<>();
            for (SclIED ied : ieds) {
                idx.put(ied.name(), ied);
            }
            iedIndex = idx;
        }
        return iedIndex.get(name);
    }

    /** Collect logical device instance names of all IEDs (lazy cache). */
    public List<String> ldNames() {
        if (ldNamesCache == null) {
            ldNamesCache = ieds.stream().flatMap(ied -> ied.lDevices().stream()).map(SclLDevice::inst)
                    .collect(java.util.stream.Collectors.toList());
        }
        return ldNamesCache;
    }
}
