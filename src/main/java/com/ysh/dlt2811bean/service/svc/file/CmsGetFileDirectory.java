package com.ysh.dlt2811bean.service.svc.file;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsFileEntry;
import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
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

    // ==================== Fields based on Table 76 ====================

    // --- Request parameters ---
    public CmsVisibleString pathName = new CmsVisibleString().max(255);
    public CmsUtcTime startTime = new CmsUtcTime();
    public CmsUtcTime stopTime = new CmsUtcTime();
    public CmsVisibleString fileAfter = new CmsVisibleString().max(255);

    // --- Response+ parameters ---
    public CmsArray<CmsFileEntry> fileEntry = new CmsArray<>(CmsFileEntry::new).capacity(100);
    public CmsBoolean moreFollows = new CmsBoolean(true);

    // --- Response- parameters ---
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetFileDirectory(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerOptionalField("pathName");
            registerOptionalField("startTime");
            registerOptionalField("stopTime");
            registerOptionalField("fileAfter");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("fileEntry");
            registerField("moreFollows");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("GetFileDirectory does not support " + messageType);
        }
    }

    public CmsGetFileDirectory(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetFileDirectory pathName(String name) {
        this.pathName.set(name);
        return this;
    }

    public CmsGetFileDirectory fileAfter(String name) {
        this.fileAfter.set(name);
        return this;
    }

    public CmsGetFileDirectory serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_FILE_DIRECTORY;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetFileDirectory copy() {
        CmsGetFileDirectory copy = new CmsGetFileDirectory(messageType());
        copy.reqId.set(reqId.get());
        copy.pathName = this.pathName.copy();
        copy.startTime = this.startTime.copy();
        copy.stopTime = this.stopTime.copy();
        copy.fileAfter = this.fileAfter.copy();
        copy.fileEntry = this.fileEntry.copy();
        copy.moreFollows = this.moreFollows.copy();
        copy.serviceError = this.serviceError.copy();
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
