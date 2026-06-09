package com.ysh.jcms2.data.choice;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.data.scalar.CmsInt32;

import java.util.Arrays;
import java.util.List;

/**
 * DataDefinitionArray ::= SEQUENCE {
 *     numberOfElement  Int32,
 *     elementType      DataDefinition
 * }  —  7.7
 *
 * All-pointer container:
 *   [0] numberOfElement → CmsInt32*
 *   [8] elementType     → CmsDataDefinition*
 */
public class CmsDataDefinitionArray extends CmsType {

    public CmsInt32          numberOfElement;
    public CmsDataDefinition elementType;

    public CmsDataDefinitionArray() {
        this.numberOfElement = new CmsInt32();
        this.elementType     = new CmsDataDefinition();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(numberOfElement, elementType);
    }
}
