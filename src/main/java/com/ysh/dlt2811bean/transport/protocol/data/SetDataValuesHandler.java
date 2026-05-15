package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsSetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsSetDataValuesEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SetDataValuesHandler extends AbstractCmsServiceHandler<CmsSetDataValues> {

    public SetDataValuesHandler() {
        super(ServiceName.SET_DATA_VALUES, CmsSetDataValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new).capacity(asdu.data.size());
        boolean allSuccess = true;

        for (CmsSetDataValuesEntry entry : asdu.data) {
            String ref = entry.reference.get();
            String rawValue = entry.value != null ? entry.value.getInnerValue().toString() : null;
            if (rawValue != null) {
                int idx = rawValue.lastIndexOf(") ");
                if (idx >= 0) rawValue = rawValue.substring(idx + 2);
            }
            int error = server.setDataValue(ref, rawValue, sclDocument.getDataTypeTemplates());
            results.add(new CmsServiceError(error));
            if (error != CmsServiceError.NO_ERROR) {
                allSuccess = false;
            }
        }

        log.debug("[Server] SetDataValues: {} entries, allSuccess={}", asdu.data.size(), allSuccess);

        if (allSuccess) {
            CmsSetDataValues response = new CmsSetDataValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get());
            return new CmsApdu(response);
        } else {
            CmsSetDataValues response = new CmsSetDataValues(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get())
                    .result(results);
            return new CmsApdu(response);
        }
    }
}
