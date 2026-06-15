package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * SetGoCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     goEna       [1] IMPLICIT BOOLEAN OPTIONAL,
 *     goID        [2] IMPLICIT VisibleString129 OPTIONAL,
 *     datSet      [3] IMPLICIT ObjectReference OPTIONAL
 * }  —  8.9.5
 *
 * Used by SetGoCBValues request.
 */
public class CmsSetGoCbEntry extends CmsType {

    public CmsObjectReference reference;
    public CmsBoolean         goEnaPresent;
    public CmsBoolean         goEna;              /* OPTIONAL */
    public CmsBoolean         goIdPresent;
    public CmsUint8Array      goId;               /* VisibleString129 OPTIONAL */
    public CmsBoolean         datSetPresent;
    public CmsObjectReference datSet;             /* OPTIONAL */

    public CmsSetGoCbEntry() {
        this.reference      = new CmsObjectReference();
        this.goEnaPresent   = new CmsBoolean();
        this.goEna          = new CmsBoolean();
        this.goIdPresent    = new CmsBoolean();
        this.goId           = new CmsUint8Array();
        this.datSetPresent  = new CmsBoolean();
        this.datSet         = new CmsObjectReference();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference,
            goEnaPresent, goEna,
            goIdPresent, goId,
            datSetPresent, datSet);
    }
}
