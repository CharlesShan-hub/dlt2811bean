package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl2.model.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsSetDataSetValues;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SetDataSetValuesHandler extends AbstractCmsServiceHandler<CmsSetDataSetValues> {

    public SetDataSetValuesHandler() {
        super(ServiceName.SET_DATA_SET_VALUES, CmsSetDataSetValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        String dsRef = asdu.datasetReference.get();
        if (dsRef == null || dsRef.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = dsRef.indexOf('/');
        if (slashIdx < 0) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ldName = dsRef.substring(0, slashIdx);
        String rest = dsRef.substring(slashIdx + 1);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        int dotIdx = rest.indexOf('.');
        String lnName = dotIdx < 0 ? "LLN0" : rest.substring(0, dotIdx);
        String dsName = dotIdx < 0 ? rest : rest.substring(dotIdx + 1);
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataSet dataSet = ln.findDataSetByName(dsName);
        if (dataSet == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        int memberCount = asdu.memberValue.size();
        if (memberCount == 0) {
            return new CmsApdu(new CmsSetDataSetValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        String afterRef = asdu.referenceAfter.get();
        int startIndex = 0;
        if (afterRef != null && !afterRef.isEmpty()) {
            startIndex = findStartIndex(dataSet, afterRef);
            if (startIndex < 0) {
                return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }

        int availableMembers = dataSet.getFcDas().size() - startIndex;
        if (memberCount > availableMembers) {
            log.warn("[Server] SetDataSetValues: memberCount={} exceeds available={} after index={}",
                    memberCount, availableMembers, startIndex);
        }

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new);
        boolean hasAnyError = false;

        for (int i = 0; i < memberCount; i++) {
            int fcdaIndex = startIndex + i;
            if (fcdaIndex >= dataSet.getFcDas().size()) {
                results.add(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
                hasAnyError = true;
            } else {
                SclFCDA fcda = dataSet.getFcDas().get(fcdaIndex);
                int error = setValueOnFcda(server, ldName, fcda, asdu.memberValue.get(i));
                results.add(new CmsServiceError(error));
                if (error != CmsServiceError.NO_ERROR) {
                    hasAnyError = true;
                }
            }
        }

        if (hasAnyError) {
            CmsSetDataSetValues response = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get());
            response.result = results;
            return new CmsApdu(response);
        }

        return new CmsApdu(new CmsSetDataSetValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private int setValueOnFcda(SclServer server, String ldName, SclFCDA fcda, CmsData<?> data) {
        String doName = fcda.getDoName();
        if (doName == null || doName.isEmpty()) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        String lnName = fcda.buildLnName();
        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }
        SclDOI doi = ln.findDoiByName(doName);
        if (doi == null) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        String newValue = extractValue(data);
        if (newValue == null) {
            return CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
        }

        String daName = fcda.getDaName();
        if (daName != null && !daName.isEmpty()) {
            // FCDA has explicit daName — find or create DAI
            SclDAI dai = findDaiByName(doi.getDais(), daName);
            if (dai == null) {
                dai = new SclDAI();
                dai.setName(daName);
                doi.getDais().add(dai);
            }
            dai.setVal(newValue);
            log.debug("[Server] SetDataSetValues: {} = {}", fcda.buildFcdaRef(), newValue);
        } else {
            // DO-level FCDA — find first DAI with a value or create a default one
            SclDAI dai = null;
            for (SclDAI d : doi.getDais()) {
                dai = d;
                break;
            }
            if (dai == null) {
                dai = new SclDAI();
                dai.setName("stVal");
                doi.getDais().add(dai);
            }
            dai.setVal(newValue);
            log.debug("[Server] SetDataSetValues: {} = {}", fcda.buildFcdaRef(), newValue);
        }

        return CmsServiceError.NO_ERROR;
    }

    private String extractValue(CmsData<?> data) {
        if (data == null) return null;
        CmsType<?> inner = data.getInnerValue();
        if (inner == null) return null;
        if (inner instanceof com.ysh.dlt2811bean.datatypes.string.CmsUtf8String) {
            return ((com.ysh.dlt2811bean.datatypes.string.CmsUtf8String) inner).get();
        }
        if (inner instanceof com.ysh.dlt2811bean.datatypes.string.CmsVisibleString) {
            return ((com.ysh.dlt2811bean.datatypes.string.CmsVisibleString) inner).get();
        }
        return inner.toString();
    }

    private SclDAI findDaiByName(java.util.List<SclDAI> dais, String name) {
        for (SclDAI dai : dais) {
            if (dai.getName().equals(name)) {
                return dai;
            }
        }
        return null;
    }



    private int findStartIndex(SclDataSet dataSet, String afterRef) {
        for (int i = 0; i < dataSet.getFcDas().size(); i++) {
            SclFCDA fcda = dataSet.getFcDas().get(i);
            if (fcda.buildFcdaRef().equals(afterRef)) {
                return i + 1;
            }
        }
        return -1;
    }



    @Override
    protected CmsApdu buildNegativeResponse(int errorCode) {
        CmsSetDataSetValues response = new CmsSetDataSetValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.getReqId())
                .addResult(errorCode);
        return new CmsApdu(response);
    }
}