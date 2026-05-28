#ifndef GEN_cms_H
#define GEN_cms_H

#include <stdint.h>
#include "cmsper/cmsper.h"

typedef struct BinaryTime BinaryTime;
typedef struct UtcTime UtcTime;
typedef struct TimeStamp TimeStamp;
typedef struct Originator Originator;
typedef struct Data Data;
typedef struct DataDefinition DataDefinition;
typedef struct SGCB SGCB;
typedef struct BRCB BRCB;
typedef struct URCB URCB;
typedef struct LCB LCB;
typedef struct GoCB GoCB;
typedef struct MSVCB MSVCB;
typedef struct FileEntry FileEntry;
typedef struct Apch Apch;
typedef struct ControlCode ControlCode;
typedef struct Asdu Asdu;
typedef struct Apdu Apdu;
typedef struct AuthenticationParameter AuthenticationParameter;
typedef struct Associate_Request Associate_Request;
typedef struct Associate_ResponsePositive Associate_ResponsePositive;
typedef struct Associate_ResponseNegative Associate_ResponseNegative;
typedef struct Release_Request Release_Request;
typedef struct Release_ResponsePositive Release_ResponsePositive;
typedef struct Release_ResponseNegative Release_ResponseNegative;
typedef struct Abort Abort;
typedef struct NegotiationParameter NegotiationParameter;
typedef struct AssociateNegotiate_Request AssociateNegotiate_Request;
typedef struct AssociateNegotiate_ResponsePositive AssociateNegotiate_ResponsePositive;
typedef struct AssociateNegotiate_ResponseNegative AssociateNegotiate_ResponseNegative;
typedef struct GetDataValues_Entry GetDataValues_Entry;
typedef struct GetDataValues_Request GetDataValues_Request;
typedef struct GetDataValues_ResponsePositive GetDataValues_ResponsePositive;
typedef struct GetDataValues_ResponseNegative GetDataValues_ResponseNegative;
typedef struct SetDataValues_Entry SetDataValues_Entry;
typedef struct SetDataValues_Request SetDataValues_Request;
typedef struct SetDataValues_ResponsePositive SetDataValues_ResponsePositive;
typedef struct SetDataValues_ResponseNegative SetDataValues_ResponseNegative;
typedef struct GetDataDefinition_Entry GetDataDefinition_Entry;
typedef struct GetDataDefinition_Request GetDataDefinition_Request;
typedef struct GetDataDefinition_ResponsePositive GetDataDefinition_ResponsePositive;
typedef struct GetDataDefinition_ResponseNegative GetDataDefinition_ResponseNegative;
typedef struct GetDataDirectory_Entry GetDataDirectory_Entry;
typedef struct GetDataDirectory_Request GetDataDirectory_Request;
typedef struct GetDataDirectory_ResponsePositive GetDataDirectory_ResponsePositive;
typedef struct GetDataDirectory_ResponseNegative GetDataDirectory_ResponseNegative;
typedef struct Select_Request Select_Request;
typedef struct Select_ResponsePositive Select_ResponsePositive;
typedef struct Select_ResponseNegative Select_ResponseNegative;
typedef struct SelectWithValue_Request SelectWithValue_Request;
typedef struct SelectWithValue_ResponsePositive SelectWithValue_ResponsePositive;
typedef struct SelectWithValue_ResponseNegative SelectWithValue_ResponseNegative;
typedef struct Operate_Request Operate_Request;
typedef struct Operate_ResponsePositive Operate_ResponsePositive;
typedef struct Operate_ResponseNegative Operate_ResponseNegative;
typedef struct Cancel_Request Cancel_Request;
typedef struct Cancel_ResponsePositive Cancel_ResponsePositive;
typedef struct Cancel_ResponseNegative Cancel_ResponseNegative;
typedef struct TimeActivatedOperate_Request TimeActivatedOperate_Request;
typedef struct TimeActivatedOperate_ResponsePositive TimeActivatedOperate_ResponsePositive;
typedef struct TimeActivatedOperate_ResponseNegative TimeActivatedOperate_ResponseNegative;
typedef struct CommandTermination CommandTermination;
typedef struct TimeActivatedOperateTermination TimeActivatedOperateTermination;
typedef struct GetBRCBValues_Request GetBRCBValues_Request;
typedef struct GetBRCBValues_ResponsePositive GetBRCBValues_ResponsePositive;
typedef struct GetBRCBValues_ResponseNegative GetBRCBValues_ResponseNegative;
typedef struct SetBRCBValues_Entry SetBRCBValues_Entry;
typedef struct SetBRCBValues_Request SetBRCBValues_Request;
typedef struct SetBRCBValues_ResponsePositive SetBRCBValues_ResponsePositive;
typedef struct SetBRCBValues_ResponseNegative SetBRCBValues_ResponseNegative;
typedef struct GetURCBValues_Request GetURCBValues_Request;
typedef struct GetURCBValues_ResponsePositive GetURCBValues_ResponsePositive;
typedef struct GetURCBValues_ResponseNegative GetURCBValues_ResponseNegative;
typedef struct SetURCBValues_Entry SetURCBValues_Entry;
typedef struct SetURCBValues_Request SetURCBValues_Request;
typedef struct SetURCBValues_ResponsePositive SetURCBValues_ResponsePositive;
typedef struct SetURCBValues_ResponseNegative SetURCBValues_ResponseNegative;
typedef struct ReportEntryData ReportEntryData;
typedef struct ReportEntry ReportEntry;
typedef struct Report Report;
typedef struct CreateDataSet_Entry CreateDataSet_Entry;
typedef struct CreateDataSet_Request CreateDataSet_Request;
typedef struct CreateDataSet_ResponsePositive CreateDataSet_ResponsePositive;
typedef struct CreateDataSet_ResponseNegative CreateDataSet_ResponseNegative;
typedef struct DeleteDataSet_Request DeleteDataSet_Request;
typedef struct DeleteDataSet_ResponsePositive DeleteDataSet_ResponsePositive;
typedef struct DeleteDataSet_ResponseNegative DeleteDataSet_ResponseNegative;
typedef struct GetDataSetDirectory_Request GetDataSetDirectory_Request;
typedef struct GetDataSetDirectory_ResponsePositive GetDataSetDirectory_ResponsePositive;
typedef struct GetDataSetDirectory_ResponseNegative GetDataSetDirectory_ResponseNegative;
typedef struct GetDataSetValues_Request GetDataSetValues_Request;
typedef struct GetDataSetValues_ResponsePositive GetDataSetValues_ResponsePositive;
typedef struct GetDataSetValues_ResponseNegative GetDataSetValues_ResponseNegative;
typedef struct SetDataSetValues_Request SetDataSetValues_Request;
typedef struct SetDataSetValues_ResponsePositive SetDataSetValues_ResponsePositive;
typedef struct SetDataSetValues_ResponseNegative SetDataSetValues_ResponseNegative;
typedef struct GetServerDirectory_Request GetServerDirectory_Request;
typedef struct GetServerDirectory_ResponsePositive GetServerDirectory_ResponsePositive;
typedef struct GetServerDirectory_ResponseNegative GetServerDirectory_ResponseNegative;
typedef struct GetLogicalDeviceDirectory_Request GetLogicalDeviceDirectory_Request;
typedef struct GetLogicalDeviceDirectory_ResponsePositive GetLogicalDeviceDirectory_ResponsePositive;
typedef struct GetLogicalDeviceDirectory_ResponseNegative GetLogicalDeviceDirectory_ResponseNegative;
typedef struct ObjectClass ObjectClass;
typedef struct GetLogicalNodeDirectory_Request GetLogicalNodeDirectory_Request;
typedef struct GetLogicalNodeDirectory_ResponsePositive GetLogicalNodeDirectory_ResponsePositive;
typedef struct CmsReference CmsReference;
typedef struct GetLogicalNodeDirectory_ResponseNegative GetLogicalNodeDirectory_ResponseNegative;
typedef struct GetAllDataValues_Request GetAllDataValues_Request;
typedef struct GetAllDataValues_ResponsePositive GetAllDataValues_ResponsePositive;
typedef struct CmsDataEntry CmsDataEntry;
typedef struct GetAllDataValues_ResponseNegative GetAllDataValues_ResponseNegative;
typedef struct GetAllDataDefinition_Request GetAllDataDefinition_Request;
typedef struct GetAllDataDefinition_ResponsePositive GetAllDataDefinition_ResponsePositive;
typedef struct CmsDataDefinitionEntry CmsDataDefinitionEntry;
typedef struct GetAllDataDefinition_ResponseNegative GetAllDataDefinition_ResponseNegative;
typedef struct CmsCBValue CmsCBValue;
typedef struct CmsCBValueEntry CmsCBValueEntry;
typedef struct GetAllCBValues_Request GetAllCBValues_Request;
typedef struct GetAllCBValues_ResponsePositive GetAllCBValues_ResponsePositive;
typedef struct GetAllCBValues_ResponseNegative GetAllCBValues_ResponseNegative;
typedef struct CmsACSIClass CmsACSIClass;
typedef struct GetACSIClasses_Request GetACSIClasses_Request;
typedef struct GetACSIClasses_ResponsePositive GetACSIClasses_ResponsePositive;
typedef struct GetACSIClasses_ResponseNegative GetACSIClasses_ResponseNegative;
typedef struct GetSGCBValues_Request GetSGCBValues_Request;
typedef struct GetSGCBValues_ResponsePositive GetSGCBValues_ResponsePositive;
typedef struct GetSGCBValues_ResponseNegative GetSGCBValues_ResponseNegative;
typedef struct SelectActiveSG_Request SelectActiveSG_Request;
typedef struct SelectActiveSG_ResponsePositive SelectActiveSG_ResponsePositive;
typedef struct SelectActiveSG_ResponseNegative SelectActiveSG_ResponseNegative;
typedef struct SelectEditSG_Request SelectEditSG_Request;
typedef struct SelectEditSG_ResponsePositive SelectEditSG_ResponsePositive;
typedef struct SelectEditSG_ResponseNegative SelectEditSG_ResponseNegative;
typedef struct ConfirmEditSGValues_Request ConfirmEditSGValues_Request;
typedef struct ConfirmEditSGValues_ResponsePositive ConfirmEditSGValues_ResponsePositive;
typedef struct ConfirmEditSGValues_ResponseNegative ConfirmEditSGValues_ResponseNegative;
typedef struct GetEditSGValue_Request GetEditSGValue_Request;
typedef struct GetEditSGValue_ResponsePositive GetEditSGValue_ResponsePositive;
typedef struct GetEditSGValue_ResponseNegative GetEditSGValue_ResponseNegative;
typedef struct SetEditSGValue_Entry SetEditSGValue_Entry;
typedef struct SetEditSGValue_Request SetEditSGValue_Request;
typedef struct SetEditSGValue_ResponsePositive SetEditSGValue_ResponsePositive;
typedef struct SetEditSGValue_ResponseNegative SetEditSGValue_ResponseNegative;
typedef struct GetLCBValues_Request GetLCBValues_Request;
typedef struct GetLCBValues_ResponsePositive GetLCBValues_ResponsePositive;
typedef struct GetLCBValues_ResponseNegative GetLCBValues_ResponseNegative;
typedef struct SetLCBValues_Entry SetLCBValues_Entry;
typedef struct SetLCBValues_Request SetLCBValues_Request;
typedef struct SetLCBValues_ResponsePositive SetLCBValues_ResponsePositive;
typedef struct SetLCBValues_ResponseNegative SetLCBValues_ResponseNegative;
typedef struct LogStatusValue LogStatusValue;
typedef struct GetLogStatusValues_Request GetLogStatusValues_Request;
typedef struct GetLogStatusValues_ResponsePositive GetLogStatusValues_ResponsePositive;
typedef struct GetLogStatusValues_ResponseNegative GetLogStatusValues_ResponseNegative;
typedef struct LogEntry LogEntry;
typedef struct QueryLogAfter_Request QueryLogAfter_Request;
typedef struct QueryLogAfter_ResponsePositive QueryLogAfter_ResponsePositive;
typedef struct QueryLogAfter_ResponseNegative QueryLogAfter_ResponseNegative;
typedef struct QueryLogByTime_Request QueryLogByTime_Request;
typedef struct QueryLogByTime_ResponsePositive QueryLogByTime_ResponsePositive;
typedef struct QueryLogByTime_ResponseNegative QueryLogByTime_ResponseNegative;
typedef struct GetFile_Request GetFile_Request;
typedef struct GetFile_ResponsePositive GetFile_ResponsePositive;
typedef struct GetFile_ResponseNegative GetFile_ResponseNegative;
typedef struct SetFile_Request SetFile_Request;
typedef struct SetFile_ResponsePositive SetFile_ResponsePositive;
typedef struct SetFile_ResponseNegative SetFile_ResponseNegative;
typedef struct DeleteFile_Request DeleteFile_Request;
typedef struct DeleteFile_ResponsePositive DeleteFile_ResponsePositive;
typedef struct DeleteFile_ResponseNegative DeleteFile_ResponseNegative;
typedef struct FileAttribute FileAttribute;
typedef struct GetFileDirectory_Request GetFileDirectory_Request;
typedef struct GetFileDirectory_ResponsePositive GetFileDirectory_ResponsePositive;
typedef struct GetFileDirectory_ResponseNegative GetFileDirectory_ResponseNegative;
typedef struct GetFileAttributeValues_Request GetFileAttributeValues_Request;
typedef struct GetFileAttributeValues_ResponsePositive GetFileAttributeValues_ResponsePositive;
typedef struct GetFileAttributeValues_ResponseNegative GetFileAttributeValues_ResponseNegative;
typedef struct GetGoReference_Request GetGoReference_Request;
typedef struct GetGoReference_ResponsePositive GetGoReference_ResponsePositive;
typedef struct GetGoReference_ResponseNegative GetGoReference_ResponseNegative;
typedef struct GetGoCBValues_Request GetGoCBValues_Request;
typedef struct GetGoCBValues_ResponsePositive GetGoCBValues_ResponsePositive;
typedef struct GetGoCBValues_ResponseNegative GetGoCBValues_ResponseNegative;
typedef struct SetGoCBValues_Entry SetGoCBValues_Entry;
typedef struct SetGoCBValues_Request SetGoCBValues_Request;
typedef struct SetGoCBValues_ResponsePositive SetGoCBValues_ResponsePositive;
typedef struct SetGoCBValues_ResponseNegative SetGoCBValues_ResponseNegative;
typedef struct GoosePdu GoosePdu;
typedef struct SendGooseMessage SendGooseMessage;
typedef struct GetGooseElementNumber_Request GetGooseElementNumber_Request;
typedef struct GetGooseElementNumber_ResponsePositive GetGooseElementNumber_ResponsePositive;
typedef struct GetGooseElementNumber_ResponseNegative GetGooseElementNumber_ResponseNegative;
typedef struct GetMSVCBValues_Request GetMSVCBValues_Request;
typedef struct GetMSVCBValues_ResponsePositive GetMSVCBValues_ResponsePositive;
typedef struct GetMSVCBValues_ResponseNegative GetMSVCBValues_ResponseNegative;
typedef struct Test_Request Test_Request;
typedef struct Test_ResponsePositive Test_ResponsePositive;
typedef struct Test_ResponseNegative Test_ResponseNegative;

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

typedef enum AbortReason {
    AbortReason_noReason = 0,
    AbortReason_unrecognizedAPDU = 1,
    AbortReason_unexpectedAPDU = 2,
    AbortReason_unexpectedSession = 3,
    AbortReason_protocolError = 4,
    AbortReason_authenticationError = 5,
    AbortReason_userRequest = 6,
    AbortReason_resourceLimit = 7,
    AbortReason_internalError = 8
} AbortReason;

typedef enum CBType {
    CBType_brcb = 0,
    CBType_urcb = 1,
    CBType_lcb = 2,
    CBType_gocb = 3,
    CBType_msvcb = 4,
    CBType_sgcb = 5
} CBType;

typedef struct BinaryTime {
    int64_t msOfDay;
    int64_t daysSince1984;
} BinaryTime;

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

typedef struct ControlCode {
    int next;
    int resp;
    int err;
    int64_t pi;
} ControlCode;

typedef struct Asdu {
    int64_t reqId;
} Asdu;

typedef struct Associate_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} Associate_ResponseNegative;

typedef struct Release_Request {
    int64_t reqId;
} Release_Request;

typedef struct Release_ResponsePositive {
    int64_t reqId;
    int releaseResponse;
} Release_ResponsePositive;

typedef struct Release_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} Release_ResponseNegative;

typedef struct Abort {
    int64_t reqId;
    AbortReason abortReason;
} Abort;

typedef struct NegotiationParameter {
    int64_t maxReqIdSize;
    int64_t maxSegmentSize;
    uint8_t * supportedServices;
    int supportedServices_len;
    char * protocolVersion;
} NegotiationParameter;

typedef struct AssociateNegotiate_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} AssociateNegotiate_ResponseNegative;

typedef struct GetDataValues_Request {
    int64_t reqId;
    char * *dataRefs;
    int dataRefs_count;
} GetDataValues_Request;

typedef struct GetDataValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetDataValues_ResponseNegative;

typedef struct SetDataValues_ResponsePositive {
    int64_t reqId;
} SetDataValues_ResponsePositive;

typedef struct SetDataValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SetDataValues_ResponseNegative;

typedef struct GetDataDefinition_Request {
    int64_t reqId;
    char * dataRef;
} GetDataDefinition_Request;

typedef struct GetDataDefinition_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetDataDefinition_ResponseNegative;

typedef struct GetDataDirectory_Entry {
    char * dataRef;
    uint8_t * fc;
} GetDataDirectory_Entry;

typedef struct GetDataDirectory_Request {
    int64_t reqId;
    char * dataRef;
} GetDataDirectory_Request;

typedef struct GetDataDirectory_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetDataDirectory_ResponseNegative;

typedef struct Select_ResponsePositive {
    int64_t reqId;
    uint8_t * actCnf;
} Select_ResponsePositive;

typedef struct Select_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} Select_ResponseNegative;

typedef struct SelectWithValue_ResponsePositive {
    int64_t reqId;
    uint8_t * actCnf;
} SelectWithValue_ResponsePositive;

typedef struct SelectWithValue_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SelectWithValue_ResponseNegative;

typedef struct Operate_Request {
    int64_t reqId;
    Tcmd actCode;
    char * actRef;
    OrCat orCat;
    uint8_t * orIdent;
    int orIdent_len;
} Operate_Request;

typedef struct Operate_ResponsePositive {
    int64_t reqId;
    uint8_t * actCnf;
} Operate_ResponsePositive;

typedef struct Operate_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} Operate_ResponseNegative;

typedef struct Cancel_Request {
    int64_t reqId;
    char * actRef;
    Tcmd actCode;
} Cancel_Request;

typedef struct Cancel_ResponsePositive {
    int64_t reqId;
    uint8_t * actCnf;
} Cancel_ResponsePositive;

typedef struct Cancel_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} Cancel_ResponseNegative;

typedef struct TimeActivatedOperate_ResponsePositive {
    int64_t reqId;
    uint8_t * actCnf;
} TimeActivatedOperate_ResponsePositive;

typedef struct TimeActivatedOperate_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} TimeActivatedOperate_ResponseNegative;

typedef struct GetBRCBValues_Request {
    int64_t reqId;
    char * brcbRef;
} GetBRCBValues_Request;

typedef struct GetBRCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetBRCBValues_ResponseNegative;

typedef struct SetBRCBValues_ResponsePositive {
    int64_t reqId;
} SetBRCBValues_ResponsePositive;

typedef struct SetBRCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SetBRCBValues_ResponseNegative;

typedef struct GetURCBValues_Request {
    int64_t reqId;
    char * urcbRef;
} GetURCBValues_Request;

typedef struct GetURCBValues_ResponsePositive {
    int64_t reqId;
    char * urcbName;
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
    uint8_t * owner;
    int owner_len;
} GetURCBValues_ResponsePositive;

typedef struct GetURCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetURCBValues_ResponseNegative;

typedef struct SetURCBValues_Entry {
    char * reference;
    int rptEna;
    int _has_rptEna;
    char * rptID;
    int _has_rptID;
    char * datSet;
    int _has_datSet;
    int64_t confRev;
    int _has_confRev;
    uint8_t * optFlds;
    int _has_optFlds;
    int64_t bufTm;
    int _has_bufTm;
    uint8_t * trgOps;
    int _has_trgOps;
    int64_t intgPd;
    int _has_intgPd;
    int gi;
    int _has_gi;
    int reserved;
    int _has_reserved;
} SetURCBValues_Entry;

typedef struct SetURCBValues_ResponsePositive {
    int64_t reqId;
} SetURCBValues_ResponsePositive;

typedef struct SetURCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SetURCBValues_ResponseNegative;

typedef struct CreateDataSet_Entry {
    char * dataRef;
} CreateDataSet_Entry;

typedef struct CreateDataSet_ResponsePositive {
    int64_t reqId;
} CreateDataSet_ResponsePositive;

typedef struct CreateDataSet_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} CreateDataSet_ResponseNegative;

typedef struct DeleteDataSet_Request {
    int64_t reqId;
    char * dataSetRef;
} DeleteDataSet_Request;

typedef struct DeleteDataSet_ResponsePositive {
    int64_t reqId;
} DeleteDataSet_ResponsePositive;

typedef struct DeleteDataSet_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} DeleteDataSet_ResponseNegative;

typedef struct GetDataSetDirectory_Request {
    int64_t reqId;
    char * dataSetRef;
} GetDataSetDirectory_Request;

typedef struct GetDataSetDirectory_ResponsePositive {
    int64_t reqId;
    char * *dataSetEntries;
    int dataSetEntries_count;
} GetDataSetDirectory_ResponsePositive;

typedef struct GetDataSetDirectory_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetDataSetDirectory_ResponseNegative;

typedef struct GetDataSetValues_Request {
    int64_t reqId;
    char * dataSetRef;
} GetDataSetValues_Request;

typedef struct GetDataSetValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetDataSetValues_ResponseNegative;

typedef struct SetDataSetValues_ResponsePositive {
    int64_t reqId;
} SetDataSetValues_ResponsePositive;

typedef struct SetDataSetValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SetDataSetValues_ResponseNegative;

typedef struct GetServerDirectory_Request {
    int64_t reqId;
} GetServerDirectory_Request;

typedef struct GetServerDirectory_ResponsePositive {
    int64_t reqId;
    char * *logicalDevices;
    int logicalDevices_count;
} GetServerDirectory_ResponsePositive;

typedef struct GetServerDirectory_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetServerDirectory_ResponseNegative;

typedef struct GetLogicalDeviceDirectory_Request {
    int64_t reqId;
    char * ldName;
} GetLogicalDeviceDirectory_Request;

typedef struct GetLogicalDeviceDirectory_ResponsePositive {
    int64_t reqId;
    char * *logicalNodes;
    int logicalNodes_count;
} GetLogicalDeviceDirectory_ResponsePositive;

typedef struct GetLogicalDeviceDirectory_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetLogicalDeviceDirectory_ResponseNegative;

typedef struct ObjectClass {
    char * classType;
} ObjectClass;

typedef struct CmsReference {
    char * reference;
    uint8_t * fc;
} CmsReference;

typedef struct GetLogicalNodeDirectory_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetLogicalNodeDirectory_ResponseNegative;

typedef struct GetAllDataValues_Request {
    int64_t reqId;
    char * ldName;
} GetAllDataValues_Request;

typedef struct GetAllDataValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetAllDataValues_ResponseNegative;

typedef struct GetAllDataDefinition_Request {
    int64_t reqId;
    char * ldName;
} GetAllDataDefinition_Request;

typedef struct CmsDataDefinitionEntry {
    char * dataRef;
    uint8_t * fc;
    char * dataName;
    char * dataType;
} CmsDataDefinitionEntry;

typedef struct GetAllDataDefinition_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetAllDataDefinition_ResponseNegative;

typedef struct CmsCBValue {
    char * cbRef;
    CBType cbType;
} CmsCBValue;

typedef struct GetAllCBValues_Request {
    int64_t reqId;
    char * ldName;
} GetAllCBValues_Request;

typedef struct GetAllCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetAllCBValues_ResponseNegative;

typedef struct CmsACSIClass {
    char * className;
    char * classVersion;
} CmsACSIClass;

typedef struct GetACSIClasses_Request {
    int64_t reqId;
} GetACSIClasses_Request;

typedef struct GetACSIClasses_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetACSIClasses_ResponseNegative;

typedef struct GetSGCBValues_Request {
    int64_t reqId;
    char * sgcbRef;
} GetSGCBValues_Request;

typedef struct GetSGCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetSGCBValues_ResponseNegative;

typedef struct SelectActiveSG_Request {
    int64_t reqId;
    char * sgcbRef;
    int64_t actSG;
} SelectActiveSG_Request;

typedef struct SelectActiveSG_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SelectActiveSG_ResponseNegative;

typedef struct SelectEditSG_Request {
    int64_t reqId;
    char * sgcbRef;
    int64_t editSG;
} SelectEditSG_Request;

typedef struct SelectEditSG_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SelectEditSG_ResponseNegative;

typedef struct ConfirmEditSGValues_Request {
    int64_t reqId;
    char * sgcbRef;
} ConfirmEditSGValues_Request;

typedef struct ConfirmEditSGValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} ConfirmEditSGValues_ResponseNegative;

typedef struct GetEditSGValue_Request {
    int64_t reqId;
    char * dataRef;
} GetEditSGValue_Request;

typedef struct GetEditSGValue_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetEditSGValue_ResponseNegative;

typedef struct SetEditSGValue_ResponsePositive {
    int64_t reqId;
} SetEditSGValue_ResponsePositive;

typedef struct SetEditSGValue_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SetEditSGValue_ResponseNegative;

typedef struct GetLCBValues_Request {
    int64_t reqId;
    char * lcbRef;
} GetLCBValues_Request;

typedef struct GetLCBValues_ResponsePositive {
    int64_t reqId;
    char * lcbName;
    char * rptID;
    int logEna;
    char * datSet;
    int64_t confRev;
    uint8_t * optFlds;
    int64_t bufTm;
    uint8_t * trgOps;
    int64_t intgPd;
    char * logRef;
} GetLCBValues_ResponsePositive;

typedef struct GetLCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetLCBValues_ResponseNegative;

typedef struct SetLCBValues_Entry {
    char * reference;
    int logEna;
    int _has_logEna;
    char * rptID;
    int _has_rptID;
    char * datSet;
    int _has_datSet;
    int64_t confRev;
    int _has_confRev;
    uint8_t * optFlds;
    int _has_optFlds;
    int64_t bufTm;
    int _has_bufTm;
    uint8_t * trgOps;
    int _has_trgOps;
    int64_t intgPd;
    int _has_intgPd;
    char * logRef;
    int _has_logRef;
} SetLCBValues_Entry;

typedef struct SetLCBValues_ResponsePositive {
    int64_t reqId;
} SetLCBValues_ResponsePositive;

typedef struct SetLCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SetLCBValues_ResponseNegative;

typedef struct GetLogStatusValues_Request {
    int64_t reqId;
    char * logRef;
} GetLogStatusValues_Request;

typedef struct GetLogStatusValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetLogStatusValues_ResponseNegative;

typedef struct QueryLogAfter_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} QueryLogAfter_ResponseNegative;

typedef struct QueryLogByTime_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} QueryLogByTime_ResponseNegative;

typedef struct GetFile_Request {
    int64_t reqId;
    char * fileName;
} GetFile_Request;

typedef struct GetFile_ResponsePositive {
    int64_t reqId;
    int64_t fileSize;
    uint8_t * fileData;
    int fileData_len;
} GetFile_ResponsePositive;

typedef struct GetFile_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetFile_ResponseNegative;

typedef struct SetFile_Request {
    int64_t reqId;
    char * fileName;
    uint8_t * fileData;
    int fileData_len;
} SetFile_Request;

typedef struct SetFile_ResponsePositive {
    int64_t reqId;
    int64_t fileSize;
} SetFile_ResponsePositive;

typedef struct SetFile_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SetFile_ResponseNegative;

typedef struct DeleteFile_Request {
    int64_t reqId;
    char * fileName;
} DeleteFile_Request;

typedef struct DeleteFile_ResponsePositive {
    int64_t reqId;
} DeleteFile_ResponsePositive;

typedef struct DeleteFile_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} DeleteFile_ResponseNegative;

typedef struct GetFileDirectory_Request {
    int64_t reqId;
    char * directoryName;
} GetFileDirectory_Request;

typedef struct GetFileDirectory_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetFileDirectory_ResponseNegative;

typedef struct GetFileAttributeValues_Request {
    int64_t reqId;
    char * fileName;
} GetFileAttributeValues_Request;

typedef struct GetFileAttributeValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetFileAttributeValues_ResponseNegative;

typedef struct GetGoReference_Request {
    int64_t reqId;
    char * gocbRef;
} GetGoReference_Request;

typedef struct GetGoReference_ResponsePositive {
    int64_t reqId;
    char * goReference;
} GetGoReference_ResponsePositive;

typedef struct GetGoReference_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetGoReference_ResponseNegative;

typedef struct GetGoCBValues_Request {
    int64_t reqId;
    char * gocbRef;
} GetGoCBValues_Request;

typedef struct GetGoCBValues_ResponsePositive {
    int64_t reqId;
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
} GetGoCBValues_ResponsePositive;

typedef struct GetGoCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetGoCBValues_ResponseNegative;

typedef struct SetGoCBValues_Entry {
    char * reference;
    char * appID;
    int _has_appID;
    char * datSet;
    int _has_datSet;
    int64_t confRev;
    int _has_confRev;
    int ndcom;
    int _has_ndcom;
    int64_t minTime;
    int _has_minTime;
    int64_t maxTime;
    int _has_maxTime;
    int fixedOffs;
    int _has_fixedOffs;
    char * goID;
    int _has_goID;
} SetGoCBValues_Entry;

typedef struct SetGoCBValues_ResponsePositive {
    int64_t reqId;
} SetGoCBValues_ResponsePositive;

typedef struct SetGoCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} SetGoCBValues_ResponseNegative;

typedef struct GetGooseElementNumber_ResponsePositive {
    int64_t reqId;
    int64_t elementNum;
} GetGooseElementNumber_ResponsePositive;

typedef struct GetGooseElementNumber_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetGooseElementNumber_ResponseNegative;

typedef struct GetMSVCBValues_Request {
    int64_t reqId;
    char * msvcbRef;
} GetMSVCBValues_Request;

typedef struct GetMSVCBValues_ResponsePositive {
    int64_t reqId;
    char * svID;
    char * datSet;
    int64_t confRev;
    int64_t smpRate;
    int64_t nofASDU;
    uint8_t * optFlds;
    int optFlds_len;
    int smpMod;
    uint8_t * dstAddress;
    int svEna;
} GetMSVCBValues_ResponsePositive;

typedef struct GetMSVCBValues_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} GetMSVCBValues_ResponseNegative;

typedef struct Test_Request {
    int64_t reqId;
} Test_Request;

typedef struct Test_ResponsePositive {
    int64_t reqId;
    int testResult;
} Test_ResponsePositive;

typedef struct Test_ResponseNegative {
    int64_t reqId;
    ServiceError serviceError;
} Test_ResponseNegative;

typedef BinaryTime EntryTime;

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

typedef struct AuthenticationParameter {
    uint8_t * signatureCertificate;
    int signatureCertificate_len;
    UtcTime signedTime;
    uint8_t * signedValue;
    int signedValue_len;
} AuthenticationParameter;

typedef struct GetSGCBValues_ResponsePositive {
    int64_t reqId;
    char * sgcbName;
    int64_t numOfSG;
    int64_t actSG;
    int64_t editSG;
    int cnfEdit;
    UtcTime lActTm;
} GetSGCBValues_ResponsePositive;

typedef struct SelectActiveSG_ResponsePositive {
    int64_t reqId;
    int64_t actSG;
    UtcTime lActTm;
} SelectActiveSG_ResponsePositive;

typedef struct SelectEditSG_ResponsePositive {
    int64_t reqId;
    int64_t editSG;
    UtcTime lActTm;
} SelectEditSG_ResponsePositive;

typedef struct ConfirmEditSGValues_ResponsePositive {
    int64_t reqId;
    UtcTime lActTm;
} ConfirmEditSGValues_ResponsePositive;

typedef struct FileAttribute {
    char * fileName;
    int64_t fileSize;
    UtcTime lastModified;
    int _has_lastModified;
} FileAttribute;

typedef struct Apch {
    ControlCode cc;
    int64_t sc;
    int64_t fl;
} Apch;

typedef struct AssociateNegotiate_Request {
    int64_t reqId;
    NegotiationParameter negotiationParameters;
} AssociateNegotiate_Request;

typedef struct AssociateNegotiate_ResponsePositive {
    int64_t reqId;
    NegotiationParameter negotiationParameters;
} AssociateNegotiate_ResponsePositive;

typedef struct GetDataDirectory_ResponsePositive {
    int64_t reqId;
    GetDataDirectory_Entry *entries;
    int entries_count;
} GetDataDirectory_ResponsePositive;

typedef struct SetURCBValues_Request {
    int64_t reqId;
    SetURCBValues_Entry *urcbValues;
    int urcbValues_count;
} SetURCBValues_Request;

typedef struct CreateDataSet_Request {
    int64_t reqId;
    char * dataSetName;
    CreateDataSet_Entry *dataEntries;
    int dataEntries_count;
} CreateDataSet_Request;

typedef struct GetLogicalNodeDirectory_Request {
    int64_t reqId;
    char * ldName;
    char * lnName;
    ObjectClass objectClass;
    int _has_objectClass;
} GetLogicalNodeDirectory_Request;

typedef struct GetLogicalNodeDirectory_ResponsePositive {
    int64_t reqId;
    CmsReference *objects;
    int objects_count;
} GetLogicalNodeDirectory_ResponsePositive;

typedef struct GetAllDataDefinition_ResponsePositive {
    int64_t reqId;
    CmsDataDefinitionEntry *definitions;
    int definitions_count;
} GetAllDataDefinition_ResponsePositive;

typedef struct CmsCBValueEntry {
    char * ldName;
    CmsCBValue *cbValues;
    int cbValues_count;
} CmsCBValueEntry;

typedef struct GetACSIClasses_ResponsePositive {
    int64_t reqId;
    CmsACSIClass *acsiClasses;
    int acsiClasses_count;
} GetACSIClasses_ResponsePositive;

typedef struct SetLCBValues_Request {
    int64_t reqId;
    SetLCBValues_Entry *lcbValues;
    int lcbValues_count;
} SetLCBValues_Request;

typedef struct SetGoCBValues_Request {
    int64_t reqId;
    SetGoCBValues_Entry *gocbValues;
    int gocbValues_count;
} SetGoCBValues_Request;

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

typedef struct GetBRCBValues_ResponsePositive {
    int64_t reqId;
    char * brcbName;
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
    uint8_t * owner;
    int owner_len;
} GetBRCBValues_ResponsePositive;

typedef struct SetBRCBValues_Entry {
    char * reference;
    int rptEna;
    int _has_rptEna;
    char * rptID;
    int _has_rptID;
    char * datSet;
    int _has_datSet;
    int64_t confRev;
    int _has_confRev;
    uint8_t * optFlds;
    int _has_optFlds;
    int64_t bufTm;
    int _has_bufTm;
    uint8_t * trgOps;
    int _has_trgOps;
    int64_t intgPd;
    int _has_intgPd;
    int gi;
    int _has_gi;
    int purgeBuf;
    int _has_purgeBuf;
    uint8_t * entryID;
    int _has_entryID;
    EntryTime timeOfEntry;
    int _has_timeOfEntry;
    int reserved;
    int _has_reserved;
} SetBRCBValues_Entry;

typedef struct LogStatusValue {
    char * ldName;
    uint8_t * oldEntr;
    uint8_t * newEntr;
    EntryTime oldTm;
    EntryTime newTm;
} LogStatusValue;

typedef struct QueryLogAfter_Request {
    int64_t reqId;
    char * logRef;
    uint8_t * entryID;
    EntryTime timeOfEntry;
} QueryLogAfter_Request;

typedef struct QueryLogByTime_Request {
    int64_t reqId;
    char * logRef;
    EntryTime startTime;
    EntryTime stopTime;
} QueryLogByTime_Request;

typedef struct DataDefinition {
    char * dataName;
    char * dataType;
    uint8_t * fc;
    Data data;
} DataDefinition;

typedef struct GetDataValues_Entry {
    char * dataRef;
    Data data;
} GetDataValues_Entry;

typedef struct SetDataValues_Entry {
    char * dataRef;
    Data data;
} SetDataValues_Entry;

typedef struct Select_Request {
    int64_t reqId;
    Tcmd actCode;
    char * actRef;
    Data actData;
    int _has_actData;
} Select_Request;

typedef struct SelectWithValue_Request {
    int64_t reqId;
    Tcmd actCode;
    char * actRef;
    Data actData;
} SelectWithValue_Request;

typedef struct TimeActivatedOperate_Request {
    int64_t reqId;
    Tcmd actCode;
    char * actRef;
    Data actData;
    int _has_actData;
    OrCat orCat;
    uint8_t * orIdent;
    int orIdent_len;
    UtcTime tActTm;
} TimeActivatedOperate_Request;

typedef struct CommandTermination {
    int64_t reqId;
    char * actRef;
    uint8_t * actCnf;
    Data actData;
    int _has_actData;
} CommandTermination;

typedef struct TimeActivatedOperateTermination {
    int64_t reqId;
    char * actRef;
    uint8_t * actCnf;
    UtcTime tActTm;
    Data actData;
    int _has_actData;
} TimeActivatedOperateTermination;

typedef struct ReportEntryData {
    char * dataRef;
    Data entryData;
    uint8_t * reasonCode;
    uint8_t * dataQuality;
    int _has_dataQuality;
    EntryTime dataTime;
    int _has_dataTime;
} ReportEntryData;

typedef struct GetDataSetValues_ResponsePositive {
    int64_t reqId;
    Data *values;
    int values_count;
} GetDataSetValues_ResponsePositive;

typedef struct SetDataSetValues_Request {
    int64_t reqId;
    char * dataSetRef;
    Data *values;
    int values_count;
} SetDataSetValues_Request;

typedef struct CmsDataEntry {
    char * dataRef;
    Data dataValue;
} CmsDataEntry;

typedef struct GetEditSGValue_ResponsePositive {
    int64_t reqId;
    Data dataValue;
} GetEditSGValue_ResponsePositive;

typedef struct SetEditSGValue_Entry {
    char * dataRef;
    Data dataValue;
} SetEditSGValue_Entry;

typedef struct LogEntry {
    uint8_t * entryID;
    EntryTime entryTime;
    Data entryData;
} LogEntry;

typedef struct GoosePdu {
    char * gocbRef;
    int64_t timeAllowedtoLive;
    char * datSet;
    char * goID;
    UtcTime t;
    int64_t stNum;
    int64_t sqNum;
    int simulation;
    int64_t confRev;
    int ndcom;
    int64_t numDatSetEntries;
    Data *allData;
    int allData_count;
} GoosePdu;

typedef struct Associate_Request {
    int64_t reqId;
    char * serverAccessPointReference;
    AuthenticationParameter authenticationParameter;
    int _has_authenticationParameter;
} Associate_Request;

typedef struct Associate_ResponsePositive {
    int64_t reqId;
    uint8_t * associationId;
    int associationId_len;
    ServiceError serviceError;
    AuthenticationParameter authenticationParameter;
    int _has_authenticationParameter;
} Associate_ResponsePositive;

typedef struct GetFileDirectory_ResponsePositive {
    int64_t reqId;
    FileAttribute *files;
    int files_count;
} GetFileDirectory_ResponsePositive;

typedef struct GetFileAttributeValues_ResponsePositive {
    int64_t reqId;
    FileAttribute fileAttributes;
} GetFileAttributeValues_ResponsePositive;

typedef struct Apdu {
    Apch apch;
    uint8_t * asdu;
    int asdu_len;
} Apdu;

typedef struct GetAllCBValues_ResponsePositive {
    int64_t reqId;
    CmsCBValueEntry *cbEntries;
    int cbEntries_count;
} GetAllCBValues_ResponsePositive;

typedef struct SetBRCBValues_Request {
    int64_t reqId;
    SetBRCBValues_Entry *brcbValues;
    int brcbValues_count;
} SetBRCBValues_Request;

typedef struct GetLogStatusValues_ResponsePositive {
    int64_t reqId;
    LogStatusValue *logs;
    int logs_count;
} GetLogStatusValues_ResponsePositive;

typedef struct GetDataDefinition_Entry {
    char * dataRef;
    DataDefinition dataDefinition;
} GetDataDefinition_Entry;

typedef struct GetDataValues_ResponsePositive {
    int64_t reqId;
    GetDataValues_Entry *values;
    int values_count;
} GetDataValues_ResponsePositive;

typedef struct SetDataValues_Request {
    int64_t reqId;
    SetDataValues_Entry *values;
    int values_count;
} SetDataValues_Request;

typedef struct ReportEntry {
    uint8_t * entryID;
    EntryTime entryTime;
    ReportEntryData *entryData;
    int entryData_count;
} ReportEntry;

typedef struct GetAllDataValues_ResponsePositive {
    int64_t reqId;
    CmsDataEntry *values;
    int values_count;
} GetAllDataValues_ResponsePositive;

typedef struct SetEditSGValue_Request {
    int64_t reqId;
    SetEditSGValue_Entry *values;
    int values_count;
} SetEditSGValue_Request;

typedef struct QueryLogAfter_ResponsePositive {
    int64_t reqId;
    LogEntry *entry;
    int entry_count;
} QueryLogAfter_ResponsePositive;

typedef struct QueryLogByTime_ResponsePositive {
    int64_t reqId;
    LogEntry *entry;
    int entry_count;
} QueryLogByTime_ResponsePositive;

typedef struct SendGooseMessage {
    int64_t reqId;
    GoosePdu goosePdu;
} SendGooseMessage;

typedef struct GetGooseElementNumber_Request {
    int64_t reqId;
    char * gocbRef;
    GoosePdu goosePdu;
} GetGooseElementNumber_Request;

typedef struct GetDataDefinition_ResponsePositive {
    int64_t reqId;
    GetDataDefinition_Entry definition;
} GetDataDefinition_ResponsePositive;

typedef struct Report {
    int64_t reqId;
    char * rptID;
    uint8_t * optFlds;
    int64_t sqNum;
    int64_t subSQNum;
    int moreSegmentsFollow;
    ReportEntry *entry;
    int entry_count;
} Report;

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

typedef char *ServerAccessPointReference;

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
int encode_Apch(per_stream_t *s, const Apch *v);
int decode_Apch(per_stream_t *s, Apch *v);
int encode_ControlCode(per_stream_t *s, const ControlCode *v);
int decode_ControlCode(per_stream_t *s, ControlCode *v);
int encode_Asdu(per_stream_t *s, const Asdu *v);
int decode_Asdu(per_stream_t *s, Asdu *v);
int encode_Apdu(per_stream_t *s, const Apdu *v);
int decode_Apdu(per_stream_t *s, Apdu *v);
int encode_AuthenticationParameter(per_stream_t *s, const AuthenticationParameter *v);
int decode_AuthenticationParameter(per_stream_t *s, AuthenticationParameter *v);
int encode_Associate_Request(per_stream_t *s, const Associate_Request *v);
int decode_Associate_Request(per_stream_t *s, Associate_Request *v);
int encode_Associate_ResponsePositive(per_stream_t *s, const Associate_ResponsePositive *v);
int decode_Associate_ResponsePositive(per_stream_t *s, Associate_ResponsePositive *v);
int encode_Associate_ResponseNegative(per_stream_t *s, const Associate_ResponseNegative *v);
int decode_Associate_ResponseNegative(per_stream_t *s, Associate_ResponseNegative *v);
int encode_Release_Request(per_stream_t *s, const Release_Request *v);
int decode_Release_Request(per_stream_t *s, Release_Request *v);
int encode_Release_ResponsePositive(per_stream_t *s, const Release_ResponsePositive *v);
int decode_Release_ResponsePositive(per_stream_t *s, Release_ResponsePositive *v);
int encode_Release_ResponseNegative(per_stream_t *s, const Release_ResponseNegative *v);
int decode_Release_ResponseNegative(per_stream_t *s, Release_ResponseNegative *v);
int encode_Abort(per_stream_t *s, const Abort *v);
int decode_Abort(per_stream_t *s, Abort *v);
int encode_AbortReason(per_stream_t *s, int v);
int decode_AbortReason(per_stream_t *s, int *v);
int encode_NegotiationParameter(per_stream_t *s, const NegotiationParameter *v);
int decode_NegotiationParameter(per_stream_t *s, NegotiationParameter *v);
int encode_AssociateNegotiate_Request(per_stream_t *s, const AssociateNegotiate_Request *v);
int decode_AssociateNegotiate_Request(per_stream_t *s, AssociateNegotiate_Request *v);
int encode_AssociateNegotiate_ResponsePositive(per_stream_t *s, const AssociateNegotiate_ResponsePositive *v);
int decode_AssociateNegotiate_ResponsePositive(per_stream_t *s, AssociateNegotiate_ResponsePositive *v);
int encode_AssociateNegotiate_ResponseNegative(per_stream_t *s, const AssociateNegotiate_ResponseNegative *v);
int decode_AssociateNegotiate_ResponseNegative(per_stream_t *s, AssociateNegotiate_ResponseNegative *v);
int encode_GetDataValues_Entry(per_stream_t *s, const GetDataValues_Entry *v);
int decode_GetDataValues_Entry(per_stream_t *s, GetDataValues_Entry *v);
int encode_GetDataValues_Request(per_stream_t *s, const GetDataValues_Request *v);
int decode_GetDataValues_Request(per_stream_t *s, GetDataValues_Request *v);
int encode_GetDataValues_ResponsePositive(per_stream_t *s, const GetDataValues_ResponsePositive *v);
int decode_GetDataValues_ResponsePositive(per_stream_t *s, GetDataValues_ResponsePositive *v);
int encode_GetDataValues_ResponseNegative(per_stream_t *s, const GetDataValues_ResponseNegative *v);
int decode_GetDataValues_ResponseNegative(per_stream_t *s, GetDataValues_ResponseNegative *v);
int encode_SetDataValues_Entry(per_stream_t *s, const SetDataValues_Entry *v);
int decode_SetDataValues_Entry(per_stream_t *s, SetDataValues_Entry *v);
int encode_SetDataValues_Request(per_stream_t *s, const SetDataValues_Request *v);
int decode_SetDataValues_Request(per_stream_t *s, SetDataValues_Request *v);
int encode_SetDataValues_ResponsePositive(per_stream_t *s, const SetDataValues_ResponsePositive *v);
int decode_SetDataValues_ResponsePositive(per_stream_t *s, SetDataValues_ResponsePositive *v);
int encode_SetDataValues_ResponseNegative(per_stream_t *s, const SetDataValues_ResponseNegative *v);
int decode_SetDataValues_ResponseNegative(per_stream_t *s, SetDataValues_ResponseNegative *v);
int encode_GetDataDefinition_Entry(per_stream_t *s, const GetDataDefinition_Entry *v);
int decode_GetDataDefinition_Entry(per_stream_t *s, GetDataDefinition_Entry *v);
int encode_GetDataDefinition_Request(per_stream_t *s, const GetDataDefinition_Request *v);
int decode_GetDataDefinition_Request(per_stream_t *s, GetDataDefinition_Request *v);
int encode_GetDataDefinition_ResponsePositive(per_stream_t *s, const GetDataDefinition_ResponsePositive *v);
int decode_GetDataDefinition_ResponsePositive(per_stream_t *s, GetDataDefinition_ResponsePositive *v);
int encode_GetDataDefinition_ResponseNegative(per_stream_t *s, const GetDataDefinition_ResponseNegative *v);
int decode_GetDataDefinition_ResponseNegative(per_stream_t *s, GetDataDefinition_ResponseNegative *v);
int encode_GetDataDirectory_Entry(per_stream_t *s, const GetDataDirectory_Entry *v);
int decode_GetDataDirectory_Entry(per_stream_t *s, GetDataDirectory_Entry *v);
int encode_GetDataDirectory_Request(per_stream_t *s, const GetDataDirectory_Request *v);
int decode_GetDataDirectory_Request(per_stream_t *s, GetDataDirectory_Request *v);
int encode_GetDataDirectory_ResponsePositive(per_stream_t *s, const GetDataDirectory_ResponsePositive *v);
int decode_GetDataDirectory_ResponsePositive(per_stream_t *s, GetDataDirectory_ResponsePositive *v);
int encode_GetDataDirectory_ResponseNegative(per_stream_t *s, const GetDataDirectory_ResponseNegative *v);
int decode_GetDataDirectory_ResponseNegative(per_stream_t *s, GetDataDirectory_ResponseNegative *v);
int encode_Select_Request(per_stream_t *s, const Select_Request *v);
int decode_Select_Request(per_stream_t *s, Select_Request *v);
int encode_Select_ResponsePositive(per_stream_t *s, const Select_ResponsePositive *v);
int decode_Select_ResponsePositive(per_stream_t *s, Select_ResponsePositive *v);
int encode_Select_ResponseNegative(per_stream_t *s, const Select_ResponseNegative *v);
int decode_Select_ResponseNegative(per_stream_t *s, Select_ResponseNegative *v);
int encode_SelectWithValue_Request(per_stream_t *s, const SelectWithValue_Request *v);
int decode_SelectWithValue_Request(per_stream_t *s, SelectWithValue_Request *v);
int encode_SelectWithValue_ResponsePositive(per_stream_t *s, const SelectWithValue_ResponsePositive *v);
int decode_SelectWithValue_ResponsePositive(per_stream_t *s, SelectWithValue_ResponsePositive *v);
int encode_SelectWithValue_ResponseNegative(per_stream_t *s, const SelectWithValue_ResponseNegative *v);
int decode_SelectWithValue_ResponseNegative(per_stream_t *s, SelectWithValue_ResponseNegative *v);
int encode_Operate_Request(per_stream_t *s, const Operate_Request *v);
int decode_Operate_Request(per_stream_t *s, Operate_Request *v);
int encode_Operate_ResponsePositive(per_stream_t *s, const Operate_ResponsePositive *v);
int decode_Operate_ResponsePositive(per_stream_t *s, Operate_ResponsePositive *v);
int encode_Operate_ResponseNegative(per_stream_t *s, const Operate_ResponseNegative *v);
int decode_Operate_ResponseNegative(per_stream_t *s, Operate_ResponseNegative *v);
int encode_Cancel_Request(per_stream_t *s, const Cancel_Request *v);
int decode_Cancel_Request(per_stream_t *s, Cancel_Request *v);
int encode_Cancel_ResponsePositive(per_stream_t *s, const Cancel_ResponsePositive *v);
int decode_Cancel_ResponsePositive(per_stream_t *s, Cancel_ResponsePositive *v);
int encode_Cancel_ResponseNegative(per_stream_t *s, const Cancel_ResponseNegative *v);
int decode_Cancel_ResponseNegative(per_stream_t *s, Cancel_ResponseNegative *v);
int encode_TimeActivatedOperate_Request(per_stream_t *s, const TimeActivatedOperate_Request *v);
int decode_TimeActivatedOperate_Request(per_stream_t *s, TimeActivatedOperate_Request *v);
int encode_TimeActivatedOperate_ResponsePositive(per_stream_t *s, const TimeActivatedOperate_ResponsePositive *v);
int decode_TimeActivatedOperate_ResponsePositive(per_stream_t *s, TimeActivatedOperate_ResponsePositive *v);
int encode_TimeActivatedOperate_ResponseNegative(per_stream_t *s, const TimeActivatedOperate_ResponseNegative *v);
int decode_TimeActivatedOperate_ResponseNegative(per_stream_t *s, TimeActivatedOperate_ResponseNegative *v);
int encode_CommandTermination(per_stream_t *s, const CommandTermination *v);
int decode_CommandTermination(per_stream_t *s, CommandTermination *v);
int encode_TimeActivatedOperateTermination(per_stream_t *s, const TimeActivatedOperateTermination *v);
int decode_TimeActivatedOperateTermination(per_stream_t *s, TimeActivatedOperateTermination *v);
int encode_GetBRCBValues_Request(per_stream_t *s, const GetBRCBValues_Request *v);
int decode_GetBRCBValues_Request(per_stream_t *s, GetBRCBValues_Request *v);
int encode_GetBRCBValues_ResponsePositive(per_stream_t *s, const GetBRCBValues_ResponsePositive *v);
int decode_GetBRCBValues_ResponsePositive(per_stream_t *s, GetBRCBValues_ResponsePositive *v);
int encode_GetBRCBValues_ResponseNegative(per_stream_t *s, const GetBRCBValues_ResponseNegative *v);
int decode_GetBRCBValues_ResponseNegative(per_stream_t *s, GetBRCBValues_ResponseNegative *v);
int encode_SetBRCBValues_Entry(per_stream_t *s, const SetBRCBValues_Entry *v);
int decode_SetBRCBValues_Entry(per_stream_t *s, SetBRCBValues_Entry *v);
int encode_SetBRCBValues_Request(per_stream_t *s, const SetBRCBValues_Request *v);
int decode_SetBRCBValues_Request(per_stream_t *s, SetBRCBValues_Request *v);
int encode_SetBRCBValues_ResponsePositive(per_stream_t *s, const SetBRCBValues_ResponsePositive *v);
int decode_SetBRCBValues_ResponsePositive(per_stream_t *s, SetBRCBValues_ResponsePositive *v);
int encode_SetBRCBValues_ResponseNegative(per_stream_t *s, const SetBRCBValues_ResponseNegative *v);
int decode_SetBRCBValues_ResponseNegative(per_stream_t *s, SetBRCBValues_ResponseNegative *v);
int encode_GetURCBValues_Request(per_stream_t *s, const GetURCBValues_Request *v);
int decode_GetURCBValues_Request(per_stream_t *s, GetURCBValues_Request *v);
int encode_GetURCBValues_ResponsePositive(per_stream_t *s, const GetURCBValues_ResponsePositive *v);
int decode_GetURCBValues_ResponsePositive(per_stream_t *s, GetURCBValues_ResponsePositive *v);
int encode_GetURCBValues_ResponseNegative(per_stream_t *s, const GetURCBValues_ResponseNegative *v);
int decode_GetURCBValues_ResponseNegative(per_stream_t *s, GetURCBValues_ResponseNegative *v);
int encode_SetURCBValues_Entry(per_stream_t *s, const SetURCBValues_Entry *v);
int decode_SetURCBValues_Entry(per_stream_t *s, SetURCBValues_Entry *v);
int encode_SetURCBValues_Request(per_stream_t *s, const SetURCBValues_Request *v);
int decode_SetURCBValues_Request(per_stream_t *s, SetURCBValues_Request *v);
int encode_SetURCBValues_ResponsePositive(per_stream_t *s, const SetURCBValues_ResponsePositive *v);
int decode_SetURCBValues_ResponsePositive(per_stream_t *s, SetURCBValues_ResponsePositive *v);
int encode_SetURCBValues_ResponseNegative(per_stream_t *s, const SetURCBValues_ResponseNegative *v);
int decode_SetURCBValues_ResponseNegative(per_stream_t *s, SetURCBValues_ResponseNegative *v);
int encode_ReportEntryData(per_stream_t *s, const ReportEntryData *v);
int decode_ReportEntryData(per_stream_t *s, ReportEntryData *v);
int encode_ReportEntry(per_stream_t *s, const ReportEntry *v);
int decode_ReportEntry(per_stream_t *s, ReportEntry *v);
int encode_Report(per_stream_t *s, const Report *v);
int decode_Report(per_stream_t *s, Report *v);
int encode_CreateDataSet_Entry(per_stream_t *s, const CreateDataSet_Entry *v);
int decode_CreateDataSet_Entry(per_stream_t *s, CreateDataSet_Entry *v);
int encode_CreateDataSet_Request(per_stream_t *s, const CreateDataSet_Request *v);
int decode_CreateDataSet_Request(per_stream_t *s, CreateDataSet_Request *v);
int encode_CreateDataSet_ResponsePositive(per_stream_t *s, const CreateDataSet_ResponsePositive *v);
int decode_CreateDataSet_ResponsePositive(per_stream_t *s, CreateDataSet_ResponsePositive *v);
int encode_CreateDataSet_ResponseNegative(per_stream_t *s, const CreateDataSet_ResponseNegative *v);
int decode_CreateDataSet_ResponseNegative(per_stream_t *s, CreateDataSet_ResponseNegative *v);
int encode_DeleteDataSet_Request(per_stream_t *s, const DeleteDataSet_Request *v);
int decode_DeleteDataSet_Request(per_stream_t *s, DeleteDataSet_Request *v);
int encode_DeleteDataSet_ResponsePositive(per_stream_t *s, const DeleteDataSet_ResponsePositive *v);
int decode_DeleteDataSet_ResponsePositive(per_stream_t *s, DeleteDataSet_ResponsePositive *v);
int encode_DeleteDataSet_ResponseNegative(per_stream_t *s, const DeleteDataSet_ResponseNegative *v);
int decode_DeleteDataSet_ResponseNegative(per_stream_t *s, DeleteDataSet_ResponseNegative *v);
int encode_GetDataSetDirectory_Request(per_stream_t *s, const GetDataSetDirectory_Request *v);
int decode_GetDataSetDirectory_Request(per_stream_t *s, GetDataSetDirectory_Request *v);
int encode_GetDataSetDirectory_ResponsePositive(per_stream_t *s, const GetDataSetDirectory_ResponsePositive *v);
int decode_GetDataSetDirectory_ResponsePositive(per_stream_t *s, GetDataSetDirectory_ResponsePositive *v);
int encode_GetDataSetDirectory_ResponseNegative(per_stream_t *s, const GetDataSetDirectory_ResponseNegative *v);
int decode_GetDataSetDirectory_ResponseNegative(per_stream_t *s, GetDataSetDirectory_ResponseNegative *v);
int encode_GetDataSetValues_Request(per_stream_t *s, const GetDataSetValues_Request *v);
int decode_GetDataSetValues_Request(per_stream_t *s, GetDataSetValues_Request *v);
int encode_GetDataSetValues_ResponsePositive(per_stream_t *s, const GetDataSetValues_ResponsePositive *v);
int decode_GetDataSetValues_ResponsePositive(per_stream_t *s, GetDataSetValues_ResponsePositive *v);
int encode_GetDataSetValues_ResponseNegative(per_stream_t *s, const GetDataSetValues_ResponseNegative *v);
int decode_GetDataSetValues_ResponseNegative(per_stream_t *s, GetDataSetValues_ResponseNegative *v);
int encode_SetDataSetValues_Request(per_stream_t *s, const SetDataSetValues_Request *v);
int decode_SetDataSetValues_Request(per_stream_t *s, SetDataSetValues_Request *v);
int encode_SetDataSetValues_ResponsePositive(per_stream_t *s, const SetDataSetValues_ResponsePositive *v);
int decode_SetDataSetValues_ResponsePositive(per_stream_t *s, SetDataSetValues_ResponsePositive *v);
int encode_SetDataSetValues_ResponseNegative(per_stream_t *s, const SetDataSetValues_ResponseNegative *v);
int decode_SetDataSetValues_ResponseNegative(per_stream_t *s, SetDataSetValues_ResponseNegative *v);
int encode_GetServerDirectory_Request(per_stream_t *s, const GetServerDirectory_Request *v);
int decode_GetServerDirectory_Request(per_stream_t *s, GetServerDirectory_Request *v);
int encode_GetServerDirectory_ResponsePositive(per_stream_t *s, const GetServerDirectory_ResponsePositive *v);
int decode_GetServerDirectory_ResponsePositive(per_stream_t *s, GetServerDirectory_ResponsePositive *v);
int encode_GetServerDirectory_ResponseNegative(per_stream_t *s, const GetServerDirectory_ResponseNegative *v);
int decode_GetServerDirectory_ResponseNegative(per_stream_t *s, GetServerDirectory_ResponseNegative *v);
int encode_GetLogicalDeviceDirectory_Request(per_stream_t *s, const GetLogicalDeviceDirectory_Request *v);
int decode_GetLogicalDeviceDirectory_Request(per_stream_t *s, GetLogicalDeviceDirectory_Request *v);
int encode_GetLogicalDeviceDirectory_ResponsePositive(per_stream_t *s, const GetLogicalDeviceDirectory_ResponsePositive *v);
int decode_GetLogicalDeviceDirectory_ResponsePositive(per_stream_t *s, GetLogicalDeviceDirectory_ResponsePositive *v);
int encode_GetLogicalDeviceDirectory_ResponseNegative(per_stream_t *s, const GetLogicalDeviceDirectory_ResponseNegative *v);
int decode_GetLogicalDeviceDirectory_ResponseNegative(per_stream_t *s, GetLogicalDeviceDirectory_ResponseNegative *v);
int encode_ObjectClass(per_stream_t *s, const ObjectClass *v);
int decode_ObjectClass(per_stream_t *s, ObjectClass *v);
int encode_GetLogicalNodeDirectory_Request(per_stream_t *s, const GetLogicalNodeDirectory_Request *v);
int decode_GetLogicalNodeDirectory_Request(per_stream_t *s, GetLogicalNodeDirectory_Request *v);
int encode_GetLogicalNodeDirectory_ResponsePositive(per_stream_t *s, const GetLogicalNodeDirectory_ResponsePositive *v);
int decode_GetLogicalNodeDirectory_ResponsePositive(per_stream_t *s, GetLogicalNodeDirectory_ResponsePositive *v);
int encode_CmsReference(per_stream_t *s, const CmsReference *v);
int decode_CmsReference(per_stream_t *s, CmsReference *v);
int encode_GetLogicalNodeDirectory_ResponseNegative(per_stream_t *s, const GetLogicalNodeDirectory_ResponseNegative *v);
int decode_GetLogicalNodeDirectory_ResponseNegative(per_stream_t *s, GetLogicalNodeDirectory_ResponseNegative *v);
int encode_GetAllDataValues_Request(per_stream_t *s, const GetAllDataValues_Request *v);
int decode_GetAllDataValues_Request(per_stream_t *s, GetAllDataValues_Request *v);
int encode_GetAllDataValues_ResponsePositive(per_stream_t *s, const GetAllDataValues_ResponsePositive *v);
int decode_GetAllDataValues_ResponsePositive(per_stream_t *s, GetAllDataValues_ResponsePositive *v);
int encode_CmsDataEntry(per_stream_t *s, const CmsDataEntry *v);
int decode_CmsDataEntry(per_stream_t *s, CmsDataEntry *v);
int encode_GetAllDataValues_ResponseNegative(per_stream_t *s, const GetAllDataValues_ResponseNegative *v);
int decode_GetAllDataValues_ResponseNegative(per_stream_t *s, GetAllDataValues_ResponseNegative *v);
int encode_GetAllDataDefinition_Request(per_stream_t *s, const GetAllDataDefinition_Request *v);
int decode_GetAllDataDefinition_Request(per_stream_t *s, GetAllDataDefinition_Request *v);
int encode_GetAllDataDefinition_ResponsePositive(per_stream_t *s, const GetAllDataDefinition_ResponsePositive *v);
int decode_GetAllDataDefinition_ResponsePositive(per_stream_t *s, GetAllDataDefinition_ResponsePositive *v);
int encode_CmsDataDefinitionEntry(per_stream_t *s, const CmsDataDefinitionEntry *v);
int decode_CmsDataDefinitionEntry(per_stream_t *s, CmsDataDefinitionEntry *v);
int encode_GetAllDataDefinition_ResponseNegative(per_stream_t *s, const GetAllDataDefinition_ResponseNegative *v);
int decode_GetAllDataDefinition_ResponseNegative(per_stream_t *s, GetAllDataDefinition_ResponseNegative *v);
int encode_CBType(per_stream_t *s, int v);
int decode_CBType(per_stream_t *s, int *v);
int encode_CmsCBValue(per_stream_t *s, const CmsCBValue *v);
int decode_CmsCBValue(per_stream_t *s, CmsCBValue *v);
int encode_CmsCBValueEntry(per_stream_t *s, const CmsCBValueEntry *v);
int decode_CmsCBValueEntry(per_stream_t *s, CmsCBValueEntry *v);
int encode_GetAllCBValues_Request(per_stream_t *s, const GetAllCBValues_Request *v);
int decode_GetAllCBValues_Request(per_stream_t *s, GetAllCBValues_Request *v);
int encode_GetAllCBValues_ResponsePositive(per_stream_t *s, const GetAllCBValues_ResponsePositive *v);
int decode_GetAllCBValues_ResponsePositive(per_stream_t *s, GetAllCBValues_ResponsePositive *v);
int encode_GetAllCBValues_ResponseNegative(per_stream_t *s, const GetAllCBValues_ResponseNegative *v);
int decode_GetAllCBValues_ResponseNegative(per_stream_t *s, GetAllCBValues_ResponseNegative *v);
int encode_CmsACSIClass(per_stream_t *s, const CmsACSIClass *v);
int decode_CmsACSIClass(per_stream_t *s, CmsACSIClass *v);
int encode_GetACSIClasses_Request(per_stream_t *s, const GetACSIClasses_Request *v);
int decode_GetACSIClasses_Request(per_stream_t *s, GetACSIClasses_Request *v);
int encode_GetACSIClasses_ResponsePositive(per_stream_t *s, const GetACSIClasses_ResponsePositive *v);
int decode_GetACSIClasses_ResponsePositive(per_stream_t *s, GetACSIClasses_ResponsePositive *v);
int encode_GetACSIClasses_ResponseNegative(per_stream_t *s, const GetACSIClasses_ResponseNegative *v);
int decode_GetACSIClasses_ResponseNegative(per_stream_t *s, GetACSIClasses_ResponseNegative *v);
int encode_GetSGCBValues_Request(per_stream_t *s, const GetSGCBValues_Request *v);
int decode_GetSGCBValues_Request(per_stream_t *s, GetSGCBValues_Request *v);
int encode_GetSGCBValues_ResponsePositive(per_stream_t *s, const GetSGCBValues_ResponsePositive *v);
int decode_GetSGCBValues_ResponsePositive(per_stream_t *s, GetSGCBValues_ResponsePositive *v);
int encode_GetSGCBValues_ResponseNegative(per_stream_t *s, const GetSGCBValues_ResponseNegative *v);
int decode_GetSGCBValues_ResponseNegative(per_stream_t *s, GetSGCBValues_ResponseNegative *v);
int encode_SelectActiveSG_Request(per_stream_t *s, const SelectActiveSG_Request *v);
int decode_SelectActiveSG_Request(per_stream_t *s, SelectActiveSG_Request *v);
int encode_SelectActiveSG_ResponsePositive(per_stream_t *s, const SelectActiveSG_ResponsePositive *v);
int decode_SelectActiveSG_ResponsePositive(per_stream_t *s, SelectActiveSG_ResponsePositive *v);
int encode_SelectActiveSG_ResponseNegative(per_stream_t *s, const SelectActiveSG_ResponseNegative *v);
int decode_SelectActiveSG_ResponseNegative(per_stream_t *s, SelectActiveSG_ResponseNegative *v);
int encode_SelectEditSG_Request(per_stream_t *s, const SelectEditSG_Request *v);
int decode_SelectEditSG_Request(per_stream_t *s, SelectEditSG_Request *v);
int encode_SelectEditSG_ResponsePositive(per_stream_t *s, const SelectEditSG_ResponsePositive *v);
int decode_SelectEditSG_ResponsePositive(per_stream_t *s, SelectEditSG_ResponsePositive *v);
int encode_SelectEditSG_ResponseNegative(per_stream_t *s, const SelectEditSG_ResponseNegative *v);
int decode_SelectEditSG_ResponseNegative(per_stream_t *s, SelectEditSG_ResponseNegative *v);
int encode_ConfirmEditSGValues_Request(per_stream_t *s, const ConfirmEditSGValues_Request *v);
int decode_ConfirmEditSGValues_Request(per_stream_t *s, ConfirmEditSGValues_Request *v);
int encode_ConfirmEditSGValues_ResponsePositive(per_stream_t *s, const ConfirmEditSGValues_ResponsePositive *v);
int decode_ConfirmEditSGValues_ResponsePositive(per_stream_t *s, ConfirmEditSGValues_ResponsePositive *v);
int encode_ConfirmEditSGValues_ResponseNegative(per_stream_t *s, const ConfirmEditSGValues_ResponseNegative *v);
int decode_ConfirmEditSGValues_ResponseNegative(per_stream_t *s, ConfirmEditSGValues_ResponseNegative *v);
int encode_GetEditSGValue_Request(per_stream_t *s, const GetEditSGValue_Request *v);
int decode_GetEditSGValue_Request(per_stream_t *s, GetEditSGValue_Request *v);
int encode_GetEditSGValue_ResponsePositive(per_stream_t *s, const GetEditSGValue_ResponsePositive *v);
int decode_GetEditSGValue_ResponsePositive(per_stream_t *s, GetEditSGValue_ResponsePositive *v);
int encode_GetEditSGValue_ResponseNegative(per_stream_t *s, const GetEditSGValue_ResponseNegative *v);
int decode_GetEditSGValue_ResponseNegative(per_stream_t *s, GetEditSGValue_ResponseNegative *v);
int encode_SetEditSGValue_Entry(per_stream_t *s, const SetEditSGValue_Entry *v);
int decode_SetEditSGValue_Entry(per_stream_t *s, SetEditSGValue_Entry *v);
int encode_SetEditSGValue_Request(per_stream_t *s, const SetEditSGValue_Request *v);
int decode_SetEditSGValue_Request(per_stream_t *s, SetEditSGValue_Request *v);
int encode_SetEditSGValue_ResponsePositive(per_stream_t *s, const SetEditSGValue_ResponsePositive *v);
int decode_SetEditSGValue_ResponsePositive(per_stream_t *s, SetEditSGValue_ResponsePositive *v);
int encode_SetEditSGValue_ResponseNegative(per_stream_t *s, const SetEditSGValue_ResponseNegative *v);
int decode_SetEditSGValue_ResponseNegative(per_stream_t *s, SetEditSGValue_ResponseNegative *v);
int encode_GetLCBValues_Request(per_stream_t *s, const GetLCBValues_Request *v);
int decode_GetLCBValues_Request(per_stream_t *s, GetLCBValues_Request *v);
int encode_GetLCBValues_ResponsePositive(per_stream_t *s, const GetLCBValues_ResponsePositive *v);
int decode_GetLCBValues_ResponsePositive(per_stream_t *s, GetLCBValues_ResponsePositive *v);
int encode_GetLCBValues_ResponseNegative(per_stream_t *s, const GetLCBValues_ResponseNegative *v);
int decode_GetLCBValues_ResponseNegative(per_stream_t *s, GetLCBValues_ResponseNegative *v);
int encode_SetLCBValues_Entry(per_stream_t *s, const SetLCBValues_Entry *v);
int decode_SetLCBValues_Entry(per_stream_t *s, SetLCBValues_Entry *v);
int encode_SetLCBValues_Request(per_stream_t *s, const SetLCBValues_Request *v);
int decode_SetLCBValues_Request(per_stream_t *s, SetLCBValues_Request *v);
int encode_SetLCBValues_ResponsePositive(per_stream_t *s, const SetLCBValues_ResponsePositive *v);
int decode_SetLCBValues_ResponsePositive(per_stream_t *s, SetLCBValues_ResponsePositive *v);
int encode_SetLCBValues_ResponseNegative(per_stream_t *s, const SetLCBValues_ResponseNegative *v);
int decode_SetLCBValues_ResponseNegative(per_stream_t *s, SetLCBValues_ResponseNegative *v);
int encode_LogStatusValue(per_stream_t *s, const LogStatusValue *v);
int decode_LogStatusValue(per_stream_t *s, LogStatusValue *v);
int encode_GetLogStatusValues_Request(per_stream_t *s, const GetLogStatusValues_Request *v);
int decode_GetLogStatusValues_Request(per_stream_t *s, GetLogStatusValues_Request *v);
int encode_GetLogStatusValues_ResponsePositive(per_stream_t *s, const GetLogStatusValues_ResponsePositive *v);
int decode_GetLogStatusValues_ResponsePositive(per_stream_t *s, GetLogStatusValues_ResponsePositive *v);
int encode_GetLogStatusValues_ResponseNegative(per_stream_t *s, const GetLogStatusValues_ResponseNegative *v);
int decode_GetLogStatusValues_ResponseNegative(per_stream_t *s, GetLogStatusValues_ResponseNegative *v);
int encode_LogEntry(per_stream_t *s, const LogEntry *v);
int decode_LogEntry(per_stream_t *s, LogEntry *v);
int encode_QueryLogAfter_Request(per_stream_t *s, const QueryLogAfter_Request *v);
int decode_QueryLogAfter_Request(per_stream_t *s, QueryLogAfter_Request *v);
int encode_QueryLogAfter_ResponsePositive(per_stream_t *s, const QueryLogAfter_ResponsePositive *v);
int decode_QueryLogAfter_ResponsePositive(per_stream_t *s, QueryLogAfter_ResponsePositive *v);
int encode_QueryLogAfter_ResponseNegative(per_stream_t *s, const QueryLogAfter_ResponseNegative *v);
int decode_QueryLogAfter_ResponseNegative(per_stream_t *s, QueryLogAfter_ResponseNegative *v);
int encode_QueryLogByTime_Request(per_stream_t *s, const QueryLogByTime_Request *v);
int decode_QueryLogByTime_Request(per_stream_t *s, QueryLogByTime_Request *v);
int encode_QueryLogByTime_ResponsePositive(per_stream_t *s, const QueryLogByTime_ResponsePositive *v);
int decode_QueryLogByTime_ResponsePositive(per_stream_t *s, QueryLogByTime_ResponsePositive *v);
int encode_QueryLogByTime_ResponseNegative(per_stream_t *s, const QueryLogByTime_ResponseNegative *v);
int decode_QueryLogByTime_ResponseNegative(per_stream_t *s, QueryLogByTime_ResponseNegative *v);
int encode_GetFile_Request(per_stream_t *s, const GetFile_Request *v);
int decode_GetFile_Request(per_stream_t *s, GetFile_Request *v);
int encode_GetFile_ResponsePositive(per_stream_t *s, const GetFile_ResponsePositive *v);
int decode_GetFile_ResponsePositive(per_stream_t *s, GetFile_ResponsePositive *v);
int encode_GetFile_ResponseNegative(per_stream_t *s, const GetFile_ResponseNegative *v);
int decode_GetFile_ResponseNegative(per_stream_t *s, GetFile_ResponseNegative *v);
int encode_SetFile_Request(per_stream_t *s, const SetFile_Request *v);
int decode_SetFile_Request(per_stream_t *s, SetFile_Request *v);
int encode_SetFile_ResponsePositive(per_stream_t *s, const SetFile_ResponsePositive *v);
int decode_SetFile_ResponsePositive(per_stream_t *s, SetFile_ResponsePositive *v);
int encode_SetFile_ResponseNegative(per_stream_t *s, const SetFile_ResponseNegative *v);
int decode_SetFile_ResponseNegative(per_stream_t *s, SetFile_ResponseNegative *v);
int encode_DeleteFile_Request(per_stream_t *s, const DeleteFile_Request *v);
int decode_DeleteFile_Request(per_stream_t *s, DeleteFile_Request *v);
int encode_DeleteFile_ResponsePositive(per_stream_t *s, const DeleteFile_ResponsePositive *v);
int decode_DeleteFile_ResponsePositive(per_stream_t *s, DeleteFile_ResponsePositive *v);
int encode_DeleteFile_ResponseNegative(per_stream_t *s, const DeleteFile_ResponseNegative *v);
int decode_DeleteFile_ResponseNegative(per_stream_t *s, DeleteFile_ResponseNegative *v);
int encode_FileAttribute(per_stream_t *s, const FileAttribute *v);
int decode_FileAttribute(per_stream_t *s, FileAttribute *v);
int encode_GetFileDirectory_Request(per_stream_t *s, const GetFileDirectory_Request *v);
int decode_GetFileDirectory_Request(per_stream_t *s, GetFileDirectory_Request *v);
int encode_GetFileDirectory_ResponsePositive(per_stream_t *s, const GetFileDirectory_ResponsePositive *v);
int decode_GetFileDirectory_ResponsePositive(per_stream_t *s, GetFileDirectory_ResponsePositive *v);
int encode_GetFileDirectory_ResponseNegative(per_stream_t *s, const GetFileDirectory_ResponseNegative *v);
int decode_GetFileDirectory_ResponseNegative(per_stream_t *s, GetFileDirectory_ResponseNegative *v);
int encode_GetFileAttributeValues_Request(per_stream_t *s, const GetFileAttributeValues_Request *v);
int decode_GetFileAttributeValues_Request(per_stream_t *s, GetFileAttributeValues_Request *v);
int encode_GetFileAttributeValues_ResponsePositive(per_stream_t *s, const GetFileAttributeValues_ResponsePositive *v);
int decode_GetFileAttributeValues_ResponsePositive(per_stream_t *s, GetFileAttributeValues_ResponsePositive *v);
int encode_GetFileAttributeValues_ResponseNegative(per_stream_t *s, const GetFileAttributeValues_ResponseNegative *v);
int decode_GetFileAttributeValues_ResponseNegative(per_stream_t *s, GetFileAttributeValues_ResponseNegative *v);
int encode_GetGoReference_Request(per_stream_t *s, const GetGoReference_Request *v);
int decode_GetGoReference_Request(per_stream_t *s, GetGoReference_Request *v);
int encode_GetGoReference_ResponsePositive(per_stream_t *s, const GetGoReference_ResponsePositive *v);
int decode_GetGoReference_ResponsePositive(per_stream_t *s, GetGoReference_ResponsePositive *v);
int encode_GetGoReference_ResponseNegative(per_stream_t *s, const GetGoReference_ResponseNegative *v);
int decode_GetGoReference_ResponseNegative(per_stream_t *s, GetGoReference_ResponseNegative *v);
int encode_GetGoCBValues_Request(per_stream_t *s, const GetGoCBValues_Request *v);
int decode_GetGoCBValues_Request(per_stream_t *s, GetGoCBValues_Request *v);
int encode_GetGoCBValues_ResponsePositive(per_stream_t *s, const GetGoCBValues_ResponsePositive *v);
int decode_GetGoCBValues_ResponsePositive(per_stream_t *s, GetGoCBValues_ResponsePositive *v);
int encode_GetGoCBValues_ResponseNegative(per_stream_t *s, const GetGoCBValues_ResponseNegative *v);
int decode_GetGoCBValues_ResponseNegative(per_stream_t *s, GetGoCBValues_ResponseNegative *v);
int encode_SetGoCBValues_Entry(per_stream_t *s, const SetGoCBValues_Entry *v);
int decode_SetGoCBValues_Entry(per_stream_t *s, SetGoCBValues_Entry *v);
int encode_SetGoCBValues_Request(per_stream_t *s, const SetGoCBValues_Request *v);
int decode_SetGoCBValues_Request(per_stream_t *s, SetGoCBValues_Request *v);
int encode_SetGoCBValues_ResponsePositive(per_stream_t *s, const SetGoCBValues_ResponsePositive *v);
int decode_SetGoCBValues_ResponsePositive(per_stream_t *s, SetGoCBValues_ResponsePositive *v);
int encode_SetGoCBValues_ResponseNegative(per_stream_t *s, const SetGoCBValues_ResponseNegative *v);
int decode_SetGoCBValues_ResponseNegative(per_stream_t *s, SetGoCBValues_ResponseNegative *v);
int encode_GoosePdu(per_stream_t *s, const GoosePdu *v);
int decode_GoosePdu(per_stream_t *s, GoosePdu *v);
int encode_SendGooseMessage(per_stream_t *s, const SendGooseMessage *v);
int decode_SendGooseMessage(per_stream_t *s, SendGooseMessage *v);
int encode_GetGooseElementNumber_Request(per_stream_t *s, const GetGooseElementNumber_Request *v);
int decode_GetGooseElementNumber_Request(per_stream_t *s, GetGooseElementNumber_Request *v);
int encode_GetGooseElementNumber_ResponsePositive(per_stream_t *s, const GetGooseElementNumber_ResponsePositive *v);
int decode_GetGooseElementNumber_ResponsePositive(per_stream_t *s, GetGooseElementNumber_ResponsePositive *v);
int encode_GetGooseElementNumber_ResponseNegative(per_stream_t *s, const GetGooseElementNumber_ResponseNegative *v);
int decode_GetGooseElementNumber_ResponseNegative(per_stream_t *s, GetGooseElementNumber_ResponseNegative *v);
int encode_GetMSVCBValues_Request(per_stream_t *s, const GetMSVCBValues_Request *v);
int decode_GetMSVCBValues_Request(per_stream_t *s, GetMSVCBValues_Request *v);
int encode_GetMSVCBValues_ResponsePositive(per_stream_t *s, const GetMSVCBValues_ResponsePositive *v);
int decode_GetMSVCBValues_ResponsePositive(per_stream_t *s, GetMSVCBValues_ResponsePositive *v);
int encode_GetMSVCBValues_ResponseNegative(per_stream_t *s, const GetMSVCBValues_ResponseNegative *v);
int decode_GetMSVCBValues_ResponseNegative(per_stream_t *s, GetMSVCBValues_ResponseNegative *v);
int encode_Test_Request(per_stream_t *s, const Test_Request *v);
int decode_Test_Request(per_stream_t *s, Test_Request *v);
int encode_Test_ResponsePositive(per_stream_t *s, const Test_ResponsePositive *v);
int decode_Test_ResponsePositive(per_stream_t *s, Test_ResponsePositive *v);
int encode_Test_ResponseNegative(per_stream_t *s, const Test_ResponseNegative *v);
int decode_Test_ResponseNegative(per_stream_t *s, Test_ResponseNegative *v);
#endif
