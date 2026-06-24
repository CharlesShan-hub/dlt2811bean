package com.ysh.jcms.svc.file;

import com.ysh.jcms.data.common.CmsFileEntry;
import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsFileTest {
    @Test
    public void get_file_request() {
        CmsGetFileRequest a = new CmsGetFileRequest();
        a.reqId.value(1);
        a.filename.value("test.txt".getBytes());
        a.startPosition.value(0L);
        byte[] encoded = a.encode();

        CmsGetFileRequest b = new CmsGetFileRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_file_response() {
        CmsGetFileResponse a = new CmsGetFileResponse();
        a.reqId.value(2);
        a.fileData.value("file content".getBytes());
        a.endOfFile.value(true);
        byte[] encoded = a.encode();

        CmsGetFileResponse b = new CmsGetFileResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_file_error() {
        CmsGetFileError a = new CmsGetFileError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetFileError b = new CmsGetFileError();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void set_file_request() {
        CmsSetFileRequest a = new CmsSetFileRequest();
        a.reqId.value(10);
        a.filename.value("new.txt".getBytes());
        a.startPosition.value(0L);
        a.fileData.value("new content".getBytes());
        a.endOfFile.value(true);
        byte[] encoded = a.encode();

        CmsSetFileRequest b = new CmsSetFileRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void set_file_error() {
        CmsSetFileError a = new CmsSetFileError();
        a.reqId.value(11);
        a.serviceError.value(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsSetFileError b = new CmsSetFileError();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void delete_file_request() {
        CmsDeleteFileRequest a = new CmsDeleteFileRequest();
        a.reqId.value(20);
        a.filename.value("del.txt".getBytes());
        byte[] encoded = a.encode();

        CmsDeleteFileRequest b = new CmsDeleteFileRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_file_dir_response_with_array() {
        CmsGetFileDirectoryResponse a = new CmsGetFileDirectoryResponse();
        a.reqId.value(30);
        CmsFileEntry e1 = new CmsFileEntry();
        e1.fileName.value("f1.txt".getBytes());
        e1.fileSize.value(100L);
        e1.lastModified.secondsSinceEpoch.value(1000000L);
        e1.lastModified.fractionOfSecond.value(0);
        e1.lastModified.timeQuality.leap_seconds_known.value(true);
        e1.checkSum.value(12345L);
        CmsFileEntry e2 = new CmsFileEntry();
        e2.fileName.value("f2.txt".getBytes());
        e2.fileSize.value(200L);
        e2.lastModified.secondsSinceEpoch.value(2000000L);
        e2.lastModified.fractionOfSecond.value(0);
        e2.lastModified.timeQuality.leap_seconds_known.value(false);
        e2.checkSum.value(67890L);
        a.fileEntry.add(e1).add(e2);
        a.moreFollows.value(false);
        byte[] encoded = a.encode();

        CmsGetFileDirectoryResponse b = new CmsGetFileDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_file_attr_request() {
        CmsGetFileAttributeValuesRequest a = new CmsGetFileAttributeValuesRequest();
        a.reqId.value(40);
        a.filename.value("attr.txt".getBytes());
        byte[] encoded = a.encode();

        CmsGetFileAttributeValuesRequest b = new CmsGetFileAttributeValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_file_attr_response() {
        CmsGetFileAttributeValuesResponse a = new CmsGetFileAttributeValuesResponse();
        a.reqId.value(41);
        a.fileEntry.fileName.value("f.txt".getBytes());
        a.fileEntry.fileSize.value(1024L);
        a.fileEntry.lastModified.secondsSinceEpoch.value(3000000L);
        a.fileEntry.lastModified.fractionOfSecond.value(0);
        a.fileEntry.lastModified.timeQuality.leap_seconds_known.value(true);
        a.fileEntry.checkSum.value(999L);
        byte[] encoded = a.encode();

        CmsGetFileAttributeValuesResponse b = new CmsGetFileAttributeValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
