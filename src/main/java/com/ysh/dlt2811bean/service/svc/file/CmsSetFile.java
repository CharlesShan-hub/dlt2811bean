package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x81 — SetFile (write file service).
 *
 * Corresponds to Table 73 in GB/T 45906.3-2025: SetFile service parameters.
 *
 * Service code: 0x81 (129)
 * Service interface: SetFile
 * Category: File service
 *
 * The SetFile service is used by a client to write data to a file on a server. The client
 * specifies the file name, the starting position within the file, the data to write, and
 * an indicator for whether this is the final chunk of data (end of file). This allows
 * for both creating new files and appending/updating existing ones.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to write a portion of a file</li>
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
 * │ startPosition                INT32U                          │
 * │ fileData                     OCTET STRING                    │
 * │ endOfFile                    BOOLEAN DEFAULT FALSE           │
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
 * SetFile-RequestPDU:: = SEQUENCE {
 *   fileName       [0] IMPLICIT VisibleString255,
 *   startPosition  [1] IMPLICIT INT32U,
 *   fileData       [2] IMPLICIT OCTET STRING,
 *   endOfFile      [3] IMPLICIT BOOLEAN DEFAULT FALSE
 * }
 *
 * SetFile-ResponsePDU:: = NULL
 *
 * SetFile-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSetFile extends CmsAsdu<CmsSetFile> {

    // ==================== Fields based on Table 73 ====================

    @CmsField(only = {"REQUEST"})
    public CmsVisibleString fileName = new CmsVisibleString().max(255);

    @CmsField(only = {"REQUEST"})
    public CmsInt32U startPosition = new CmsInt32U();

    @CmsField(only = {"REQUEST"})
    public CmsOctetString fileData = new CmsOctetString().max(65535);

    @CmsField(only = {"REQUEST"})
    public CmsBoolean endOfFile = new CmsBoolean();

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsSetFile() {
    }

    public CmsSetFile(MessageType messageType) {
        super(messageType);
    }

    public CmsSetFile(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSetFile fileName(String name) {
        this.fileName.set(name);
        return this;
    }

    public CmsSetFile startPosition(long pos) {
        this.startPosition.set(pos);
        return this;
    }

    public CmsSetFile fileData(byte[] data) {
        this.fileData.set(data);
        return this;
    }

    public CmsSetFile endOfFile(boolean eof) {
        this.endOfFile.set(eof);
        return this;
    }

    public CmsSetFile serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_FILE;
    }
}
