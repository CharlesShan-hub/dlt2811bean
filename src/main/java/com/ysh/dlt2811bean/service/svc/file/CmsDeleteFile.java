package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x82 — DeleteFile (delete file service).
 *
 * Corresponds to Table 74 in GB/T 45906.3-2025: DeleteFile service parameters.
 *
 * Service code: 0x82 (130)
 * Service interface: DeleteFile
 * Category: File service
 *
 * The DeleteFile service is used by a client to delete a specified file from a server.
 * The client provides the name of the file to be deleted. This service is typically
 * used for file management and cleanup operations.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to delete a file</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (no data returned)</li>
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
 * DeleteFile-RequestPDU:: = SEQUENCE {
 *   fileName    [0] IMPLICIT VisibleString255
 * }
 *
 * DeleteFile-ResponsePDU:: = NULL
 *
 * DeleteFile-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsDeleteFile extends CmsAsdu<CmsDeleteFile> {

    // ==================== Fields based on Table 74 ====================

    // --- Request parameters ---
    public CmsVisibleString fileName = new CmsVisibleString().max(255);

    // --- Response- parameters ---
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsDeleteFile(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("fileName");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            // no additional fields
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("DeleteFile does not support " + messageType);
        }
    }

    public CmsDeleteFile(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsDeleteFile fileName(String name) {
        this.fileName.set(name);
        return this;
    }

    public CmsDeleteFile serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.DELETE_FILE;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsDeleteFile copy() {
        CmsDeleteFile copy = new CmsDeleteFile(messageType());
        copy.reqId.set(reqId.get());
        copy.fileName = this.fileName.copy();
        copy.serviceError = this.serviceError.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsDeleteFile read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsDeleteFile) new CmsDeleteFile(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsDeleteFile deleteFile) {
        deleteFile.encode(pos);
    }

}
