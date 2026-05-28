#ifndef GEN_dlt2811b_datatypes_H
#define GEN_dlt2811b_datatypes_H

#include <stdint.h>
#include "cmsper/cmsper.h"

struct BinaryTime;
struct UtcTime;
struct TimeStamp;
struct Originator;
struct Data;
struct DataDefinition;
struct SGCB;
struct BRCB;
struct URCB;
struct LCB;
struct GoCB;
struct MSVCB;
struct FileEntry;

typedef enum Dbpos {
    Dbpos_intermediate = 0,
    Dbpos_off = 1,
    Dbpos_on = 2,
    Dbpos_badState = 3
} Dbpos;

typedef enum Tcmd {
    Tcmd_reserved = 0,
    Tcmd_select = 1,
    Tcmd_operate = 2,
    Tcmd_cancel = 3
} Tcmd;

typedef enum ServiceError {
    ServiceError_noError = 0,
    ServiceError_accessNotAllowedInCurrentState = 1,
    ServiceError_accessViewNotSupported = 2,
    ServiceError_ambiguousReference = 3,
    ServiceError_classNotSupported = 4,
    ServiceError_instanceNotAvailable = 5,
    ServiceError_instanceInUse = 6,
    ServiceError_parameterValueInappropriate = 7,
    ServiceError_parameterValueInvalid = 8,
    ServiceError_failedDueToServerConstraint = 9,
    ServiceError_failedDueToCommunicationConstraint = 10,
    ServiceError_typeConflict = 11,
    ServiceError_other = 12,
    ServiceError_dataSetNull = 13,
    ServiceError_dataSetLimit = 14,
    ServiceError_objectAlreadyExists = 15,
    ServiceError_objectNotExist = 16
} ServiceError;

typedef enum AddCause {
    AddCause_unknown = 0,
    AddCause_processError = 1,
    AddCause_protocolError = 2,
    AddCause_applicationError = 3,
    AddCause_performanceLimitation = 4,
    AddCause_resourceLimitation = 5,
    AddCause_authenticationFailure = 6,
    AddCause_securityViolation = 7,
    AddCause_communicationFailure = 8,
    AddCause_systemFailure = 9,
    AddCause_hardwareFailure = 10,
    AddCause_softwareFailure = 11,
    AddCause_configurationError = 12,
    AddCause_operationNotSupported = 13,
    AddCause_operationBlocked = 14,
    AddCause_temporaryFailure = 15,
    AddCause_permanentFailure = 16
} AddCause;

typedef enum OrCat {
    OrCat_notSupported = 0,
    OrCat_bayControl = 1,
    OrCat_stationControl = 2,
    OrCat_remoteControl = 3,
    OrCat_automaticBay = 4,
    OrCat_automaticStation = 5,
    OrCat_automaticRemote = 6,
    OrCat_maintenance = 7,
    OrCat_process = 8
} OrCat;

typedef enum SmpMod {
    SmpMod_smpPerPeriod = 0,
    SmpMod_smpPerSecond = 1,
    SmpMod_secPerSmp = 2
} SmpMod;

typedef int64_t Int8;

typedef int64_t Int8U;

typedef int64_t Int16;

typedef int64_t Int16U;

typedef int64_t Int24U;

typedef int64_t Int32;

typedef int64_t Int32U;

typedef int64_t Int64;

typedef int64_t Int64U;

typedef double Float32;

typedef double Float64;

typedef char *VisibleString;

typedef char *UTF8String;

typedef struct { uint8_t *data; int len; } OctetString;

typedef char *ObjectName;

typedef char *ObjectReference;

typedef uint8_t FC[2];

typedef char *SubReference;

typedef uint8_t EntryID[8];

typedef struct { uint8_t *data; int len; } BitString;

typedef struct BinaryTime {
    int64_t msOfDay;
    int64_t daysSince1984;
} BinaryTime;

typedef BinaryTime EntryTime;

typedef struct UtcTime {
    int64_t secondsSinceEpoch;
    int64_t fractional;
    int _has_fractional;
} UtcTime;

typedef struct TimeStamp {
    int64_t secondsSinceEpoch;
    int64_t fractional;
} TimeStamp;

typedef struct Originator {
    OrCat orCat;
    uint8_t * orIdent;
    int orIdent_len;
} Originator;

typedef uint8_t PhyComAddr[6];

typedef uint8_t Check[2];

typedef uint8_t LcbOptFlds[1];

typedef uint8_t MsvcbOptFlds[1];

typedef uint8_t Quality[2];

typedef uint8_t RcbOptFlds[2];

typedef uint8_t ReasonCode[1];

typedef uint8_t TimeQuality[1];

typedef uint8_t TriggerConditions[1];

typedef struct { uint8_t *data; int len; } PackedList;

typedef struct Data {
    int _choice;
    union {
        ServiceError serviceError;
        int boolean;
        int64_t int8;
        int64_t int16;
        int64_t int32;
        int64_t int64;
        int64_t int8u;
        int64_t int16u;
        int64_t int32u;
        int64_t int64u;
        double float32;
        double float64;
        uint8_t * bitString;
        uint8_t * octetString;
        char * visibleString;
        char * utf8String;
        UtcTime utcTime;
        BinaryTime binaryTime;
        uint8_t * quality;
        Dbpos dbpos;
        Tcmd tcmd;
        uint8_t * check;
    } u;
} Data;

typedef struct DataDefinition {
    char * dataName;
    char * dataType;
    uint8_t * fc;
    Data data;
} DataDefinition;

typedef struct SGCB {
    char * sgcbName;
    char * sgcbRef;
    int64_t numOfSG;
    int64_t actSG;
    int64_t editSG;
    int cnfEdit;
    UtcTime lActTm;
    int64_t resvTms;
    int _has_resvTms;
} SGCB;

typedef struct BRCB {
    char * brcbName;
    char * brcbRef;
    char * rptID;
    int rptEna;
    char * datSet;
    int64_t confRev;
    uint8_t * optFlds;
    int64_t bufTm;
    int64_t sqNum;
    uint8_t * trgOps;
    int64_t intgPd;
    int gi;
    int purgeBuf;
    uint8_t * entryID;
    EntryTime timeOfEntry;
    int64_t resvTms;
    int _has_resvTms;
    uint8_t * owner;
    int owner_len;
} BRCB;

typedef struct URCB {
    char * urcbName;
    char * urcbRef;
    char * rptID;
    int rptEna;
    int resv;
    char * datSet;
    int64_t confRev;
    uint8_t * optFlds;
    int64_t bufTm;
    int64_t sqNum;
    uint8_t * trgOps;
    int64_t intgPd;
    int gi;
    uint8_t * owner;
    int owner_len;
} URCB;

typedef struct LCB {
    char * lcbName;
    char * lcbRef;
    char * rptID;
    int logEna;
    char * datSet;
    int64_t confRev;
    uint8_t * optFlds;
    int64_t bufTm;
    uint8_t * trgOps;
    int64_t intgPd;
    char * logRef;
} LCB;

typedef struct GoCB {
    char * gocbRef;
    char * appID;
    char * datSet;
    int64_t confRev;
    int ndcom;
    uint8_t * dstAddress;
    int64_t minTime;
    int64_t maxTime;
    int fixedOffs;
    char * goID;
    int _has_goID;
} GoCB;

typedef struct MSVCB {
    char * msvcbRef;
    char * svID;
    char * datSet;
    int64_t confRev;
    int64_t smpRate;
    int64_t nofASDU;
    uint8_t * optFlds;
    SmpMod smpMod;
    uint8_t * dstAddress;
    int svEna;
    int reserved1;
    char * svCbHealth;
    int _has_svCbHealth;
    char * svCbAlarmName;
    int _has_svCbAlarmName;
} MSVCB;

typedef struct FileEntry {
    char * fileName;
    int64_t fileSize;
    UtcTime lastModified;
    int _has_lastModified;
    char * fileType;
    int _has_fileType;
    char * fileAttr;
    int _has_fileAttr;
} FileEntry;

int encode_BinaryTime(per_stream_t *s, const BinaryTime *v);
int decode_BinaryTime(per_stream_t *s, BinaryTime *v);
int encode_UtcTime(per_stream_t *s, const UtcTime *v);
int decode_UtcTime(per_stream_t *s, UtcTime *v);
int encode_TimeStamp(per_stream_t *s, const TimeStamp *v);
int decode_TimeStamp(per_stream_t *s, TimeStamp *v);
int encode_Originator(per_stream_t *s, const Originator *v);
int decode_Originator(per_stream_t *s, Originator *v);
int encode_Dbpos(per_stream_t *s, int v);
int decode_Dbpos(per_stream_t *s, int *v);
int encode_Tcmd(per_stream_t *s, int v);
int decode_Tcmd(per_stream_t *s, int *v);
int encode_Data(per_stream_t *s, const Data *v);
int decode_Data(per_stream_t *s, Data *v);
int encode_DataDefinition(per_stream_t *s, const DataDefinition *v);
int decode_DataDefinition(per_stream_t *s, DataDefinition *v);
int encode_ServiceError(per_stream_t *s, int v);
int decode_ServiceError(per_stream_t *s, int *v);
int encode_AddCause(per_stream_t *s, int v);
int decode_AddCause(per_stream_t *s, int *v);
int encode_OrCat(per_stream_t *s, int v);
int decode_OrCat(per_stream_t *s, int *v);
int encode_SmpMod(per_stream_t *s, int v);
int decode_SmpMod(per_stream_t *s, int *v);
int encode_SGCB(per_stream_t *s, const SGCB *v);
int decode_SGCB(per_stream_t *s, SGCB *v);
int encode_BRCB(per_stream_t *s, const BRCB *v);
int decode_BRCB(per_stream_t *s, BRCB *v);
int encode_URCB(per_stream_t *s, const URCB *v);
int decode_URCB(per_stream_t *s, URCB *v);
int encode_LCB(per_stream_t *s, const LCB *v);
int decode_LCB(per_stream_t *s, LCB *v);
int encode_GoCB(per_stream_t *s, const GoCB *v);
int decode_GoCB(per_stream_t *s, GoCB *v);
int encode_MSVCB(per_stream_t *s, const MSVCB *v);
int decode_MSVCB(per_stream_t *s, MSVCB *v);
int encode_FileEntry(per_stream_t *s, const FileEntry *v);
int decode_FileEntry(per_stream_t *s, FileEntry *v);
#endif
