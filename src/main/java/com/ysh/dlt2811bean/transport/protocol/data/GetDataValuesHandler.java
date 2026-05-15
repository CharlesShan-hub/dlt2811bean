package com.ysh.dlt2811bean.transport.protocol.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl2.model.SclDataValue;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetDataValuesHandler extends AbstractCmsServiceHandler<CmsGetDataValues> {

    public GetDataValuesHandler() {
        super(ServiceName.GET_DATA_VALUES, CmsGetDataValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        CmsStructure values = new CmsStructure();
        for (CmsGetDataValuesEntry entry : asdu.data) {
            String ref = entry.reference.get();
            SclDataValue resolved = server.resolveDataValue(ref, sclDocument.getDataTypeTemplates());
            if (resolved != null) {
                CmsType<?> typedValue = SclTypeResolver.createTypedValue(resolved.bType(), resolved.val());
                values.add(typedValue);
            } else {
                values.add(new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE));
            }
        }

        CmsGetDataValues response = new CmsGetDataValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .value(values);
        response.moreFollows.set(false);

        log.debug("[Server] GetDataValues: {} entries", values.size());
        return new CmsApdu(response);
    }
}
