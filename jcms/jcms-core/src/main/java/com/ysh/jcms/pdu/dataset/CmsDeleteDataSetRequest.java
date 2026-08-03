package com.ysh.jcms.pdu.dataset;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerDeleteDataSetRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * DeleteDataSet-RequestPDU ::= SEQUENCE {
 *     datasetReference    [0] IMPLICIT ObjectReference
 * } — 8.5.4
 * }
 * </pre>
 */
public class CmsDeleteDataSetRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference datasetReference;

    public CmsDeleteDataSetRequest() {
        super(new InnerDeleteDataSetRequestPDU());
        this.datasetReference = new CmsObjectReference();
    }

    public CmsDeleteDataSetRequest datasetReference(String v) {
        this.datasetReference.value(v);
        return this;
    }
    public CmsDeleteDataSetRequest datasetReference(byte[] v) {
        return datasetReference(new String(v, StandardCharsets.UTF_8));
    }
}
