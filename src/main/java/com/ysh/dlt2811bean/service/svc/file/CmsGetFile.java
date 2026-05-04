package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
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
 * CMS Service Code 0x50 — GetFile (read file service).
 *
 * Corresponds to Table 72 in GB/T 45906.3-2025: GetFile service parameters.
 *
 * Service code: 0x80 (128)
 * Service interface: GetFile
 * Category: File service
 *
 * The GetFile service is used by a client to read a file from a server. The client
 * specifies the file name and the starting position within the file. The server
 * responds with a chunk of file data and indicates whether the end of the file
 * has been reached. The start position is 1-based.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to read a portion of a file</li>
 *   <li>RESPONSE_POSITIVE - Server response containing file data and EOF flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ fileName                     VisibleString255                │
 * │ startPosition                INT32U                          │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ fileData                     OCTET STRING (variable length)  │
 * │ endOfFile                    BOOLEAN (default FALSE)         │
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
 * GetFile-RequestPDU:: = SEQUENCE {
 *   fileName       [0] IMPLICIT VisibleString255,
 *   startPosition  [1] IMPLICIT INT32U
 * }
 *
 * GetFile-ResponsePDU:: = SEQUENCE {
 *   fileData       [0] IMPLICIT OCTET STRING,
 *   endOfFile      [1] IMPLICIT BOOLEAN DEFAULT FALSE
 * }
 *
 * GetFile-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetFile extends CmsAsdu<CmsGetFile> {

    // ==================== Fields based on Table 72 ====================

    // --- Request parameters ---
    public CmsVisibleString fileName = new CmsVisibleString().max(255);
    public CmsInt32U startPosition = new CmsInt32U();

    // --- Response+ parameters ---
    public CmsOctetString fileData = new CmsOctetString().max(65535);
    public CmsBoolean endOfFile = new CmsBoolean();

    // --- Response- parameters ---
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetFile(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("fileName");
            registerField("startPosition");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("fileData");
            registerField("endOfFile");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("GetFile does not support " + messageType);
        }
    }

    public CmsGetFile(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetFile fileName(String name) {
        this.fileName.set(name);
        return this;
    }

    public CmsGetFile startPosition(long pos) {
        this.startPosition.set(pos);
        return this;
    }

    public CmsGetFile fileData(byte[] data) {
        this.fileData.set(data);
        return this;
    }

    public CmsGetFile endOfFile(boolean eof) {
        this.endOfFile.set(eof);
        return this;
    }

    public CmsGetFile serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_FILE;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetFile copy() {
        CmsGetFile copy = new CmsGetFile(messageType());
        copy.reqId.set(reqId.get());
        copy.fileName = this.fileName.copy();
        copy.startPosition = this.startPosition.copy();
        copy.fileData = this.fileData.copy();
        copy.endOfFile = this.endOfFile.copy();
        copy.serviceError = this.serviceError.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetFile read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetFile) new CmsGetFile(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetFile getFile) {
        getFile.encode(pos);
    }

}
