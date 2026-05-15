package com.ysh.dlt2811bean.transport.protocol.dataset;

import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl2.model.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetValues;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetDataSetValuesHandler extends AbstractCmsServiceHandler<CmsGetDataSetValues> {

    public GetDataSetValuesHandler() {
        super(ServiceName.GET_DATA_SET_VALUES, CmsGetDataSetValues::new);
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

        SclDataSet dataSet = findDataSet(device, rest);
        if (dataSet == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String afterRef = asdu.referenceAfter.get();
        boolean skipUntilAfter = afterRef != null && !afterRef.isEmpty();

        CmsStructure values = new CmsStructure();
        boolean foundAfter = false;

        for (SclFCDA fcda : dataSet.getFcDas()) {
            String memberRef = fcda.buildFcdaRef();

            if (skipUntilAfter) {
                if (!foundAfter) {
                    if (memberRef.equals(afterRef)) {
                        foundAfter = true;
                    }
                    continue;
                }
            }

            SclDataValue resolved = server.resolveDataValue(memberRef, sclDocument.getDataTypeTemplates());
            if (resolved != null) {
                CmsType<?> typedValue = SclTypeResolver.createTypedValue(resolved.bType(), resolved.val());
                values.add(typedValue);
            } else {
                values.add(new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE));
            }
        }

        if (skipUntilAfter && !foundAfter) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        CmsGetDataSetValues response = new CmsGetDataSetValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.value = values;
        response.moreFollows.set(false);

        log.debug("[Server] GetDataSetValues: {}, {} members", dsRef, values.size());
        return new CmsApdu(response);
    }



    private SclDataSet findDataSet(SclLDevice device, String ref) {
        int dotIdx = ref.indexOf('.');
        if (dotIdx < 0) {
            if (device.getLn0() == null) return null;
            for (SclDataSet ds : device.getLn0().getDataSets()) {
                if (ds.getName().equals(ref)) {
                    return ds;
                }
            }
            return null;
        }
        String lnName = ref.substring(0, dotIdx);
        String dsName = ref.substring(dotIdx + 1);
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                for (SclDataSet ds : device.getLn0().getDataSets()) {
                    if (ds.getName().equals(dsName)) {
                        return ds;
                    }
                }
                return null;
            }
        }
        for (SclLN ln : device.getLns()) {
            String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                for (SclDataSet ds : ln.getDataSets()) {
                    if (ds.getName().equals(dsName)) {
                        return ds;
                    }
                }
                return null;
            }
        }
        return null;
    }
}