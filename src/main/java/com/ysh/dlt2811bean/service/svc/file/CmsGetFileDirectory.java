package com.ysh.dlt2811bean.service.svc.file;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x84 — GetFileDirectory (list file directory service).
 *
 * Corresponds to Table 76 in GB/T 45906.3-2025: GetFileDirectory service parameters.
 *
 * Service code: 0x84 (132)
 * Service interface: GetFileDirectory
 * Category: File service
 *
 * The GetFileDirectory service is used by a client to list files and their attributes
 * within a specified directory on a server. The client can provide filtering criteria
 * including a path name, a time range, and a starting file name. The server responds
 * with a sequence of FileEntry structures and indicates if more data is available.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to list directory contents</li>
 *   <li>RESPONSE_POSITIVE - Server response containing file entries and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ pathName             [0] IMPLICIT VisibleString255 OPTIONAL  │
 * │ startTime            [1] IMPLICIT TimeStamp OPTIONAL         │
 * │ stopTime             [2] IMPLICIT TimeStamp OPTIONAL         │
 * │ fileAfter            [3] IMPLICIT VisibleString255 OPTIONAL  │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ fileEntry           [0] IMPLICIT SEQUENCE OF FileEntry       │
 * │ moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE        │
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
 * GetFileDirectory-RequestPDU:: = SEQUENCE {
 *   pathName                     [0] IMPLICIT VisibleString255 OPTIONAL,
 *   startTime                    [1] IMPLICIT TimeStamp OPTIONAL,
 *   stopTime                     [2] IMPLICIT TimeStamp OPTIONAL,
 *   fileAfter                    [3] IMPLICIT VisibleString255 OPTIONAL
 * }
 * GetFileDirectory-ResponsePDU:: = SEQUENCE {
 *   fileEntry                    [0] IMPLICIT SEQUENCE OF FileEntry,
 *   moreFollows                  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * GetFileDirectory-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetFileDirectory extends CmsAsdu<CmsGetFileDirectory> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetFileDirectory(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetFileDirectory does not support " + messageType);
        }
    }

    public CmsGetFileDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_FILE_DIRECTORY;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetFileDirectory copy() {
        CmsGetFileDirectory copy = new CmsGetFileDirectory(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetFileDirectory read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetFileDirectory) new CmsGetFileDirectory(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetFileDirectory getFileDirectory) {
        getFileDirectory.encode(pos);
    }

}
