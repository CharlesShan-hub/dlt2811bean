// package com.ysh.dlt2811bean.transport.protocol.file;

// import com.ysh.dlt2811bean.datatypes.compound.CmsFileEntry;
// import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
// import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
// import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
// import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
// import com.ysh.dlt2811bean.service.svc.file.CmsGetFileDirectory;
// import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.zip.CRC32;

// public class GetFileDirectoryHandler extends AbstractCmsServiceHandler<CmsGetFileDirectory> {
    
//     private final List<CmsFileEntry> builtinFiles = new ArrayList<>();

//     public GetFileDirectoryHandler() {
//         super(ServiceName.GET_FILE_DIRECTORY, CmsGetFileDirectory::new);
//         long now = System.currentTimeMillis() / 1000;
//         builtinFiles.add(buildEntry("/README.txt", 76, now - 86400, buildCrc32("README")));
//         builtinFiles.add(buildEntry("/config.yaml", 719, now - 43200, buildCrc32("config")));
//         builtinFiles.add(buildEntry("/data/log.txt", 9357, now - 3600, buildCrc32("log")));
//     }

//     private static long buildCrc32(String input) {
//         CRC32 crc = new CRC32();
//         crc.update(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
//         return crc.getValue();
//     }

//     private static CmsFileEntry buildEntry(String name, long size, long epochSeconds, long crc) {
//         return new CmsFileEntry()
//                 .fileName(name)
//                 .fileSize(size)
//                 .lastModified(new CmsUtcTime(epochSeconds, 0, 0L))
//                 .checkSum(crc);
//     }

//     @Override
//     protected CmsApdu doServerHandle() {
//         String pathName = asdu.pathName.get();
//         String fileAfter = asdu.fileAfter.get();
//         long startTime = asdu.startTime.secondsSinceEpoch.get();
//         long stopTime = asdu.stopTime.secondsSinceEpoch.get();
//         boolean hasStart = asdu.startTime != null && startTime > 0;
//         boolean hasStop = asdu.stopTime != null && stopTime > 0;

//         CmsGetFileDirectory response = new CmsGetFileDirectory(MessageType.RESPONSE_POSITIVE)
//                 .reqId(asdu.reqId().get());

//         boolean passedAfter = (fileAfter == null || fileAfter.isEmpty());

//         for (CmsFileEntry entry : builtinFiles) {
//             String name = entry.fileName.get();

//             if (pathName != null && !pathName.isEmpty() && !pathName.equals("/")) {
//                 String prefix = pathName.endsWith("/") ? pathName : pathName + "/";
//                 if (!name.startsWith(prefix) && !name.equals(pathName)) {
//                     continue;
//                 }
//             }

//             if (!passedAfter) {
//                 if (name.equals(fileAfter)) {
//                     passedAfter = true;
//                 }
//                 continue;
//             }

//             long modified = entry.lastModified.secondsSinceEpoch.get();
//             if (hasStart && modified < startTime) continue;
//             if (hasStop && modified > stopTime) continue;

//             response.fileEntry.add(entry.copy());
//         }

//         log.debug("[Server] GetFileDirectory: path={}, {} entries", pathName, response.fileEntry.size());
//         return new CmsApdu(response);
//     }
// }
