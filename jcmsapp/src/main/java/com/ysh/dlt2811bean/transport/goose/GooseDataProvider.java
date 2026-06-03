package com.ysh.dlt2811bean.transport.goose;

import com.ysh.dlt2811bean.datatypes.data.CmsData;

import java.util.List;

@FunctionalInterface
public interface GooseDataProvider {
    CmsData<?> getDataValues(String goCBRef, List<String> dataSetRefs);
}
