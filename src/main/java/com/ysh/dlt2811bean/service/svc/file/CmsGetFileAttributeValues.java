package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.compound.CmsFileEntry;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x83 — GetFileAttributeValues (read file attribute values service).
 *
 * Corresponds to Table 75 in GB/T 45906.3-2025: GetFileAttributeValues service parameters.
 *
 * Service code: 0x83 (131)
 * Service interface: GetFileAttributeValues
 * Category: File service
 *
 * The GetFileAttributeValues service is used by a client to retrieve the attributes
 * of a specified file from a server. The client provides the file name, and the server
 * responds with a FileEntry structure containing the file's metadata attributes.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to read file attributes</li>
 *   <li>RESPONSE_POSITIVE - Server response containing file attributes (FileEntry)</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ fileName                     VisibleString255                │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ fileEntry                    FileEntry                       │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ serviceError                 ServiceError                    │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetFileAttributeValues-RequestPDU:: = SEQUENCE {
 *   fileName [0] IMPLICIT VisibleString255
 * }
 * GetFileAttributeValues-ResponsePDU:: = FileEntry
 * GetFileAttributeValues-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetFileAttributeValues extends CmsAsdu<CmsGetFileAttributeValues> {

    // ==================== Fields based on Table 75 ====================

    @CmsField(only = {"REQUEST"})
    public CmsVisibleString fileName = new CmsVisibleString().max(255);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsFileEntry fileEntry = new CmsFileEntry();

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetFileAttributeValues() {
    }

    public CmsGetFileAttributeValues(MessageType messageType) {
        super(messageType);
    }

    public CmsGetFileAttributeValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetFileAttributeValues fileName(String name) {
        this.fileName.set(name);
        return this;
    }

    public CmsGetFileAttributeValues serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_FILE_ATTRIBUTEVALUES;
    }
}
