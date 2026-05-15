package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
}
