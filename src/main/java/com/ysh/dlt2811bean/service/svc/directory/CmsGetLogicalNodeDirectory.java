package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CMS Service Code 0x52 — GetLogicalNodeDirectory (read logical node directory).
 *
 * Corresponds to Table 25 in GB/T 45906.3-2025: GetLogicalNodeDirectory service parameters.
 *
 * Service code: 0x52 (82)
 * Service interface: GetLogicalNodeDirectory
 * Category: Directory service
 *
 * The GetLogicalNodeDirectory service is used to retrieve all data objects
 * or control blocks within a logical node. The acsiClass parameter is used
 * to limit the type of objects requested.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get logical node directory request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with subreferences</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                  ObjectName / ObjectReference     │
 * │ acsiClass                  ACSICLass                        │
 * │ referenceAfter[0..1]       ObjectReference (OPTIONAL)       │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference[0..n]           SEQUENCE OF SubReference          │
 * │ moreFollows[0..1]         BOOLEAN (OPTIONAL)                │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError               ServiceError                     │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetLogicalNodeDirectory-RequestPDU::= SEQUENCE {
 *   reference          [0] IMPLICIT CHOICE {
 *                          ldName    [0] IMPLICIT ObjectName,
 *                          lnReference [1] IMPLICIT ObjectReference
 *                       },
 *   acsiClass          [1] IMPLICIT ACSIClass,
 *   referenceAfter     [2] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetLogicalNodeDirectory-ResponsePDU::= SEQUENCE {
 *   reference          [0] IMPLICIT SEQUENCE OF SubReference,
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetLogicalNodeDirectory-ErrorPDU::= ServiceError
 *
 * -- Note: ACSICLass is defined as an INTEGER with the following values:
 * -- reserved (0), data-object (1), data-set (2), brcb (3), urcb (4), lcb (5), log (6), sgcb (7), gocb (8), msvcb (10)
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetLogicalNodeDirectory extends CmsAsdu<CmsGetLogicalNodeDirectory> {

    // ==================== Fields based on Table 25 ====================

    @CmsField(only = {"REQUEST"})
    public CmsReference referenceRequest = new CmsReference();

    @CmsField(only = {"REQUEST"})
    public CmsACSIClass acsiClass = new CmsACSIClass(CmsACSIClass.DATA_OBJECT);

    @CmsField(optional = true, only = {"REQUEST"})
    public CmsObjectReference referenceAfter = new CmsObjectReference();

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsSubReference> referenceResponse = new CmsArray<>(CmsSubReference::new).capacity(100);

    @CmsField(optional = true, only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean();

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetLogicalNodeDirectory() {
        super(ServiceName.GET_LOGIC_NODE_DIRECTORY);
    }

    public CmsGetLogicalNodeDirectory(MessageType messageType) {
        super(ServiceName.GET_LOGIC_NODE_DIRECTORY, messageType);
    }

    public CmsGetLogicalNodeDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ==================== Convenience Setters ====================

    public CmsGetLogicalNodeDirectory ldName(String name) {
        this.referenceRequest.ldName(name);
        return this;
    }

    public CmsGetLogicalNodeDirectory lnReference(String ref) {
        this.referenceRequest.lnReference(ref);
        return this;
    }

    public CmsGetLogicalNodeDirectory referenceAfter(String ref) {
        this.referenceAfter = new CmsObjectReference(ref);
        return this;
    }

    public CmsGetLogicalNodeDirectory serviceError(int errorCode) {
        this.serviceError = new CmsServiceError(errorCode);
        return this;
    }
}
