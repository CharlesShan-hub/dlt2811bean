package com.ysh.jcms.data.choice;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt32;

import java.util.Arrays;
import java.util.List;

/**
 * DataDefinitionArray ::= SEQUENCE { numberOfElement Int32, elementType
 * DataDefinition } — 7.7
 *
 * All-pointer container: [0] numberOfElement → CmsInt32* [8] elementType →
 * CmsDataDefinition*
 */
public class CmsDataDefinitionArray extends CmsType {

    public CmsInt32 numberOfElement;
    public CmsDataDefinition elementType;

    public CmsDataDefinitionArray() {
        super();
        this.numberOfElement = new CmsInt32();
        // elementType set by caller via elementType() setter.
        // It is included in children() so write() properly NULLs slot 1
        // when not set (no circular reference risk since CmsDataDefinitionArray
        // no longer creates CmsDataDefinition in its constructor).
    }

    @Override
    protected int calcNativeSize() {
        return 16;
    } // 2 pointers × 8 bytes

    public CmsDataDefinitionArray numberOfElement(int v) {
        this.numberOfElement.value(v);
        return this;
    }
    public CmsDataDefinitionArray elementType(CmsDataDefinition v) {
        this.elementType = v;
        return this;
    }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(numberOfElement, elementType);
    }
}
