package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclServices {

    private boolean dynAssociation;
    private boolean getDirectory;
    private boolean getDataObjectDefinition;
    private boolean getDataSetValue;
    private boolean dataSetDirectory;
    private boolean readWrite;
    private boolean fileHandling;
    private boolean getCBValues;
    private boolean gSEDir;
    private boolean timerActivatedControl;

    private Integer confDataSetMax;
    private Integer confDataSetMaxAttributes;
    private Integer confReportControlMax;
    private Integer confLogControlMax;
    private Integer gooseMax;
    private Integer gsseMax;

    private Boolean confLNsFixPrefix;
    private Boolean confLNsFixLnInst;

    private SclReportSettings reportSettings;
    private SclGSESettings gseSettings;

    // ==================== Remaining tServices capability elements (complete
    // tServices coverage) ====================

    private Integer nameLength;

    private Boolean settingGroups;
    private Boolean sgEdit;
    private Boolean confSG;

    private Boolean dataObjectDirectory;
    private Boolean setDataSetValue;

    private Integer dynDataSetMax;
    private Integer dynDataSetMaxAttributes;

    private String logSettingsLogEna;
    private String logSettingsTrgOps;
    private String logSettingsIntgPd;

    private String smvSettingsSmpRate;

    private Boolean gooseFixedOffs;

    private Integer smvscMax;
    private String smvscDelivery;

    private Integer confSigRefMax;

    private Boolean clientServicesGoose;
    private Boolean clientServicesGsse;
    private Boolean clientServicesBufReport;
    private Boolean clientServicesUnbufReport;
    private Boolean clientServicesReadLog;
    private Boolean clientServicesSv;
    private Boolean clientServicesSupportsLdName;

    private Boolean confLdName;

    private Integer supSubscriptionMaxGo;
    private Integer supSubscriptionMaxSv;

    private Boolean valueHandlingSetToRO;

    private Boolean redProtHsr;
    private Boolean redProtPrp;
    private Boolean redProtRstp;

    private Boolean timeSyncProtSntp;
    private Boolean timeSyncProtC37_238;
    private Boolean timeSyncProtOther;

    private Boolean commProtIpv6;
}
