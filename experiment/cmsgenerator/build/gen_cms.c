#include "gen_cms.h"
#include <stdlib.h>
#include <string.h>

int encode_BinaryTime(per_stream_t *s, const BinaryTime *v) {
    per_encode_constrained_int(s, v->msOfDay, 0, 4294967295);
    per_encode_constrained_int(s, v->daysSince1984, 0, 65535);
    return 0;
}

int decode_BinaryTime(per_stream_t *s, BinaryTime *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->msOfDay = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->daysSince1984 = (int)_tmp;
    }
    return 0;
}

int encode_UtcTime(per_stream_t *s, const UtcTime *v) {
    per_encode_constrained_int(s, v->secondsSinceEpoch, -2147483648, 2147483647);
    if (v->_has_fractional) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->fractional, 0, 16777215);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_UtcTime(per_stream_t *s, UtcTime *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, -2147483648, 2147483647);
        v->secondsSinceEpoch = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_fractional = _b ? 1 : 0;
    }
    if (v->_has_fractional) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 16777215);
            v->fractional = (int)_tmp;
        }
    }
    return 0;
}

int encode_TimeStamp(per_stream_t *s, const TimeStamp *v) {
    per_encode_constrained_int(s, v->secondsSinceEpoch, -2147483648, 2147483647);
    per_encode_constrained_int(s, v->fractional, 0, 16777215);
    return 0;
}

int decode_TimeStamp(per_stream_t *s, TimeStamp *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, -2147483648, 2147483647);
        v->secondsSinceEpoch = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16777215);
        v->fractional = (int)_tmp;
    }
    return 0;
}

int encode_Originator(per_stream_t *s, const Originator *v) {
    per_encode_constrained_int(s, v->orCat, 0, 8);
    per_encode_octet_string(s, v->orIdent, v->orIdent_len, 65535);
    return 0;
}

int decode_Originator(per_stream_t *s, Originator *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 8);
        v->orCat = (int)_tmp;
    }
        return 0;
}

int encode_Dbpos(per_stream_t *s, int v) {
    return per_encode_constrained_int(s, v, 0, 3);
}

int decode_Dbpos(per_stream_t *s, int *v) {
    int64_t _tmp;
    int err = per_decode_constrained_int(s, &_tmp, 0, 3);
    if (err) return err;
    *v = (int)_tmp;
    return 0;
}

int encode_Tcmd(per_stream_t *s, int v) {
    return per_encode_constrained_int(s, v, 0, 3);
}

int decode_Tcmd(per_stream_t *s, int *v) {
    int64_t _tmp;
    int err = per_decode_constrained_int(s, &_tmp, 0, 3);
    if (err) return err;
    *v = (int)_tmp;
    return 0;
}

int encode_Data(per_stream_t *s, const Data *v) {
    per_encode_small_non_negative(s, v->_choice);
    switch (v->_choice) {
        case 0: break; /* serviceError */
        case 1: break; /* array */
        case 2: break; /* structure */
        case 3: break; /* boolean */
        case 4: break; /* int8 */
        case 5: break; /* int16 */
        case 6: break; /* int32 */
        case 7: break; /* int64 */
        case 8: break; /* int8u */
        case 9: break; /* int16u */
        case 10: break; /* int32u */
        case 11: break; /* int64u */
        case 12: break; /* float32 */
        case 13: break; /* float64 */
        case 14: break; /* bitString */
        case 15: break; /* octetString */
        case 16: break; /* visibleString */
        case 17: break; /* utf8String */
        case 18: break; /* utcTime */
        case 19: break; /* binaryTime */
        case 20: break; /* quality */
        case 21: break; /* dbpos */
        case 22: break; /* tcmd */
        case 23: break; /* check */
    }
    return 0;
}

int decode_Data(per_stream_t *s, Data *v) {
    uint32_t _idx;
    per_decode_small_non_negative(s, &_idx);
    v->_choice = (int)_idx;
    switch (v->_choice) {
        case 0: break; /* serviceError */
        case 1: break; /* array */
        case 2: break; /* structure */
        case 3: break; /* boolean */
        case 4: break; /* int8 */
        case 5: break; /* int16 */
        case 6: break; /* int32 */
        case 7: break; /* int64 */
        case 8: break; /* int8u */
        case 9: break; /* int16u */
        case 10: break; /* int32u */
        case 11: break; /* int64u */
        case 12: break; /* float32 */
        case 13: break; /* float64 */
        case 14: break; /* bitString */
        case 15: break; /* octetString */
        case 16: break; /* visibleString */
        case 17: break; /* utf8String */
        case 18: break; /* utcTime */
        case 19: break; /* binaryTime */
        case 20: break; /* quality */
        case 21: break; /* dbpos */
        case 22: break; /* tcmd */
        case 23: break; /* check */
    }
    return 0;
}

int encode_DataDefinition(per_stream_t *s, const DataDefinition *v) {
    per_encode_visible_string(s, v->dataName, 255);
    per_encode_visible_string(s, v->dataType, 255);
    per_encode_bit_string_fixed(s, v->fc, 16);
    per_encode_small_non_negative(s, v->data._choice);
    return 0;
}

int decode_DataDefinition(per_stream_t *s, DataDefinition *v) {
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->dataName = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->dataType = strdup(_buf);
    }
            return 0;
}

int encode_ServiceError(per_stream_t *s, int v) {
    return per_encode_constrained_int(s, v, 0, 16);
}

int decode_ServiceError(per_stream_t *s, int *v) {
    int64_t _tmp;
    int err = per_decode_constrained_int(s, &_tmp, 0, 16);
    if (err) return err;
    *v = (int)_tmp;
    return 0;
}

int encode_AddCause(per_stream_t *s, int v) {
    return per_encode_constrained_int(s, v, 0, 16);
}

int decode_AddCause(per_stream_t *s, int *v) {
    int64_t _tmp;
    int err = per_decode_constrained_int(s, &_tmp, 0, 16);
    if (err) return err;
    *v = (int)_tmp;
    return 0;
}

int encode_OrCat(per_stream_t *s, int v) {
    return per_encode_constrained_int(s, v, 0, 8);
}

int decode_OrCat(per_stream_t *s, int *v) {
    int64_t _tmp;
    int err = per_decode_constrained_int(s, &_tmp, 0, 8);
    if (err) return err;
    *v = (int)_tmp;
    return 0;
}

int encode_SmpMod(per_stream_t *s, int v) {
    return per_encode_constrained_int(s, v, 0, 2);
}

int decode_SmpMod(per_stream_t *s, int *v) {
    int64_t _tmp;
    int err = per_decode_constrained_int(s, &_tmp, 0, 2);
    if (err) return err;
    *v = (int)_tmp;
    return 0;
}

int encode_SGCB(per_stream_t *s, const SGCB *v) {
    per_encode_visible_string(s, v->sgcbName, 64);
    per_encode_visible_string(s, v->sgcbRef, 129);
    per_encode_constrained_int(s, v->numOfSG, 0, 255);
    per_encode_constrained_int(s, v->actSG, 0, 255);
    per_encode_constrained_int(s, v->editSG, 0, 255);
    per_encode_boolean(s, v->cnfEdit);
    encode_UtcTime(s, &v->lActTm);
    if (v->_has_resvTms) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->resvTms, 0, 65535);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_SGCB(per_stream_t *s, SGCB *v) {
    {
        char _buf[65];
        per_decode_visible_string(s, _buf, 64);
        v->sgcbName = strdup(_buf);
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->sgcbRef = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->numOfSG = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->actSG = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->editSG = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->cnfEdit = _b ? 1 : 0;
    }
    decode_UtcTime(s, &v->lActTm);
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_resvTms = _b ? 1 : 0;
    }
    if (v->_has_resvTms) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 65535);
            v->resvTms = (int)_tmp;
        }
    }
    return 0;
}

int encode_BRCB(per_stream_t *s, const BRCB *v) {
    per_encode_visible_string(s, v->brcbName, 64);
    per_encode_visible_string(s, v->brcbRef, 129);
    per_encode_visible_string(s, v->rptID, 255);
    per_encode_boolean(s, v->rptEna);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_bit_string_fixed(s, v->optFlds, 10);
    per_encode_constrained_int(s, v->bufTm, 0, 4294967295);
    per_encode_constrained_int(s, v->sqNum, 0, 65535);
    per_encode_bit_string_fixed(s, v->trgOps, 6);
    per_encode_constrained_int(s, v->intgPd, 0, 4294967295);
    per_encode_boolean(s, v->gi);
    per_encode_boolean(s, v->purgeBuf);
    per_encode_octet_string_fixed(s, v->entryID, 8);
    encode_BinaryTime(s, &v->timeOfEntry);
    if (v->_has_resvTms) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->resvTms, -32768, 32767);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    per_encode_octet_string(s, v->owner, v->owner_len, 65535);
    return 0;
}

int decode_BRCB(per_stream_t *s, BRCB *v) {
    {
        char _buf[65];
        per_decode_visible_string(s, _buf, 64);
        v->brcbName = strdup(_buf);
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->brcbRef = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->rptID = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->rptEna = _b ? 1 : 0;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->bufTm = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->sqNum = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->intgPd = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->gi = _b ? 1 : 0;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->purgeBuf = _b ? 1 : 0;
    }
        decode_BinaryTime(s, &v->timeOfEntry);
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_resvTms = _b ? 1 : 0;
    }
    if (v->_has_resvTms) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, -32768, 32767);
            v->resvTms = (int)_tmp;
        }
    }
    /* TODO: decode owner */
    return 0;
}

int encode_URCB(per_stream_t *s, const URCB *v) {
    per_encode_visible_string(s, v->urcbName, 64);
    per_encode_visible_string(s, v->urcbRef, 129);
    per_encode_visible_string(s, v->rptID, 255);
    per_encode_boolean(s, v->rptEna);
    per_encode_boolean(s, v->resv);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_bit_string_fixed(s, v->optFlds, 10);
    per_encode_constrained_int(s, v->bufTm, 0, 4294967295);
    per_encode_constrained_int(s, v->sqNum, 0, 255);
    per_encode_bit_string_fixed(s, v->trgOps, 6);
    per_encode_constrained_int(s, v->intgPd, 0, 4294967295);
    per_encode_boolean(s, v->gi);
    per_encode_octet_string(s, v->owner, v->owner_len, 65535);
    return 0;
}

int decode_URCB(per_stream_t *s, URCB *v) {
    {
        char _buf[65];
        per_decode_visible_string(s, _buf, 64);
        v->urcbName = strdup(_buf);
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->urcbRef = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->rptID = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->rptEna = _b ? 1 : 0;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->resv = _b ? 1 : 0;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->bufTm = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->sqNum = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->intgPd = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->gi = _b ? 1 : 0;
    }
    /* TODO: decode owner */
    return 0;
}

int encode_LCB(per_stream_t *s, const LCB *v) {
    per_encode_visible_string(s, v->lcbName, 64);
    per_encode_visible_string(s, v->lcbRef, 129);
    per_encode_visible_string(s, v->rptID, 255);
    per_encode_boolean(s, v->logEna);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_bit_string_fixed(s, v->optFlds, 6);
    per_encode_constrained_int(s, v->bufTm, 0, 4294967295);
    per_encode_bit_string_fixed(s, v->trgOps, 6);
    per_encode_constrained_int(s, v->intgPd, 0, 4294967295);
    per_encode_visible_string(s, v->logRef, 129);
    return 0;
}

int decode_LCB(per_stream_t *s, LCB *v) {
    {
        char _buf[65];
        per_decode_visible_string(s, _buf, 64);
        v->lcbName = strdup(_buf);
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->lcbRef = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->rptID = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->logEna = _b ? 1 : 0;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->bufTm = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->intgPd = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->logRef = strdup(_buf);
    }
    return 0;
}

int encode_GoCB(per_stream_t *s, const GoCB *v) {
    per_encode_visible_string(s, v->gocbRef, 129);
    per_encode_visible_string(s, v->appID, 255);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_boolean(s, v->ndcom);
    per_encode_octet_string_fixed(s, v->dstAddress, 6);
    per_encode_constrained_int(s, v->minTime, 0, 4294967295);
    per_encode_constrained_int(s, v->maxTime, 0, 4294967295);
    per_encode_boolean(s, v->fixedOffs);
    if (v->_has_goID) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->goID, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_GoCB(per_stream_t *s, GoCB *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->gocbRef = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->appID = strdup(_buf);
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->ndcom = _b ? 1 : 0;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->minTime = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->maxTime = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->fixedOffs = _b ? 1 : 0;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_goID = _b ? 1 : 0;
    }
    if (v->_has_goID) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->goID = strdup(_buf);
        }
    }
    return 0;
}

int encode_MSVCB(per_stream_t *s, const MSVCB *v) {
    per_encode_visible_string(s, v->msvcbRef, 129);
    per_encode_visible_string(s, v->svID, 255);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_constrained_int(s, v->smpRate, 0, 4294967295);
    per_encode_constrained_int(s, v->nofASDU, 0, 255);
    per_encode_bit_string_fixed(s, v->optFlds, 8);
    per_encode_constrained_int(s, v->smpMod, 0, 2);
    per_encode_octet_string_fixed(s, v->dstAddress, 6);
    per_encode_boolean(s, v->svEna);
    per_encode_boolean(s, v->reserved1);
    if (v->_has_svCbHealth) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->svCbHealth, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_svCbAlarmName) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->svCbAlarmName, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_MSVCB(per_stream_t *s, MSVCB *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->msvcbRef = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->svID = strdup(_buf);
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->smpRate = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->nofASDU = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 2);
        v->smpMod = (int)_tmp;
    }
        {
        bool _b;
        per_decode_boolean(s, &_b);
        v->svEna = _b ? 1 : 0;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->reserved1 = _b ? 1 : 0;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_svCbHealth = _b ? 1 : 0;
    }
    if (v->_has_svCbHealth) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->svCbHealth = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_svCbAlarmName = _b ? 1 : 0;
    }
    if (v->_has_svCbAlarmName) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->svCbAlarmName = strdup(_buf);
        }
    }
    return 0;
}

int encode_FileEntry(per_stream_t *s, const FileEntry *v) {
    per_encode_visible_string(s, v->fileName, 255);
    per_encode_constrained_int(s, v->fileSize, 0, 4294967295);
    if (v->_has_lastModified) {
        per_encode_boolean(s, 1); /* present */
encode_UtcTime(s, &v->lastModified);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_fileType) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->fileType, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_fileAttr) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->fileAttr, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_FileEntry(per_stream_t *s, FileEntry *v) {
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->fileName = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->fileSize = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_lastModified = _b ? 1 : 0;
    }
    if (v->_has_lastModified) {
decode_UtcTime(s, &v->lastModified);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_fileType = _b ? 1 : 0;
    }
    if (v->_has_fileType) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->fileType = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_fileAttr = _b ? 1 : 0;
    }
    if (v->_has_fileAttr) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->fileAttr = strdup(_buf);
        }
    }
    return 0;
}

int encode_Apch(per_stream_t *s, const Apch *v) {
    encode_ControlCode(s, &v->cc);
    per_encode_constrained_int(s, v->sc, 0, 255);
    per_encode_constrained_int(s, v->fl, 0, 65535);
    return 0;
}

int decode_Apch(per_stream_t *s, Apch *v) {
    decode_ControlCode(s, &v->cc);
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->sc = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->fl = (int)_tmp;
    }
    return 0;
}

int encode_ControlCode(per_stream_t *s, const ControlCode *v) {
    per_encode_boolean(s, v->next);
    per_encode_boolean(s, v->resp);
    per_encode_boolean(s, v->err);
    per_encode_constrained_int(s, v->pi, 0, 255);
    return 0;
}

int decode_ControlCode(per_stream_t *s, ControlCode *v) {
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->next = _b ? 1 : 0;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->resp = _b ? 1 : 0;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->err = _b ? 1 : 0;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->pi = (int)_tmp;
    }
    return 0;
}

int encode_Asdu(per_stream_t *s, const Asdu *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_Asdu(per_stream_t *s, Asdu *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_Apdu(per_stream_t *s, const Apdu *v) {
    encode_Apch(s, &v->apch);
    per_encode_octet_string(s, v->asdu, v->asdu_len, 65535);
    return 0;
}

int decode_Apdu(per_stream_t *s, Apdu *v) {
    decode_Apch(s, &v->apch);
    /* TODO: decode asdu */
    return 0;
}

int encode_AuthenticationParameter(per_stream_t *s, const AuthenticationParameter *v) {
    per_encode_octet_string(s, v->signatureCertificate, v->signatureCertificate_len, 65535);
    encode_UtcTime(s, &v->signedTime);
    per_encode_octet_string(s, v->signedValue, v->signedValue_len, 65535);
    return 0;
}

int decode_AuthenticationParameter(per_stream_t *s, AuthenticationParameter *v) {
    /* TODO: decode signatureCertificate */
    decode_UtcTime(s, &v->signedTime);
    /* TODO: decode signedValue */
    return 0;
}

int encode_Associate_Request(per_stream_t *s, const Associate_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->serverAccessPointReference, 129);
    if (v->_has_authenticationParameter) {
        per_encode_boolean(s, 1); /* present */
encode_AuthenticationParameter(s, &v->authenticationParameter);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_Associate_Request(per_stream_t *s, Associate_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->serverAccessPointReference = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_authenticationParameter = _b ? 1 : 0;
    }
    if (v->_has_authenticationParameter) {
decode_AuthenticationParameter(s, &v->authenticationParameter);
    }
    return 0;
}

int encode_Associate_ResponsePositive(per_stream_t *s, const Associate_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_octet_string(s, v->associationId, v->associationId_len, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    if (v->_has_authenticationParameter) {
        per_encode_boolean(s, 1); /* present */
encode_AuthenticationParameter(s, &v->authenticationParameter);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_Associate_ResponsePositive(per_stream_t *s, Associate_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    /* TODO: decode associationId */
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_authenticationParameter = _b ? 1 : 0;
    }
    if (v->_has_authenticationParameter) {
decode_AuthenticationParameter(s, &v->authenticationParameter);
    }
    return 0;
}

int encode_Associate_ResponseNegative(per_stream_t *s, const Associate_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_Associate_ResponseNegative(per_stream_t *s, Associate_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_Release_Request(per_stream_t *s, const Release_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_Release_Request(per_stream_t *s, Release_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_Release_ResponsePositive(per_stream_t *s, const Release_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_boolean(s, v->releaseResponse);
    return 0;
}

int decode_Release_ResponsePositive(per_stream_t *s, Release_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->releaseResponse = _b ? 1 : 0;
    }
    return 0;
}

int encode_Release_ResponseNegative(per_stream_t *s, const Release_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_Release_ResponseNegative(per_stream_t *s, Release_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_Abort(per_stream_t *s, const Abort *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->abortReason, 0, 8);
    return 0;
}

int decode_Abort(per_stream_t *s, Abort *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 8);
        v->abortReason = (int)_tmp;
    }
    return 0;
}

int encode_AbortReason(per_stream_t *s, int v) {
    return per_encode_constrained_int(s, v, 0, 8);
}

int decode_AbortReason(per_stream_t *s, int *v) {
    int64_t _tmp;
    int err = per_decode_constrained_int(s, &_tmp, 0, 8);
    if (err) return err;
    *v = (int)_tmp;
    return 0;
}

int encode_NegotiationParameter(per_stream_t *s, const NegotiationParameter *v) {
    per_encode_constrained_int(s, v->maxReqIdSize, 0, 65535);
    per_encode_constrained_int(s, v->maxSegmentSize, 0, 4294967295);
    per_encode_octet_string(s, v->supportedServices, v->supportedServices_len, 65535);
    per_encode_visible_string(s, v->protocolVersion, 255);
    return 0;
}

int decode_NegotiationParameter(per_stream_t *s, NegotiationParameter *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->maxReqIdSize = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->maxSegmentSize = (int)_tmp;
    }
    /* TODO: decode supportedServices */
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->protocolVersion = strdup(_buf);
    }
    return 0;
}

int encode_AssociateNegotiate_Request(per_stream_t *s, const AssociateNegotiate_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    encode_NegotiationParameter(s, &v->negotiationParameters);
    return 0;
}

int decode_AssociateNegotiate_Request(per_stream_t *s, AssociateNegotiate_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    decode_NegotiationParameter(s, &v->negotiationParameters);
    return 0;
}

int encode_AssociateNegotiate_ResponsePositive(per_stream_t *s, const AssociateNegotiate_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    encode_NegotiationParameter(s, &v->negotiationParameters);
    return 0;
}

int decode_AssociateNegotiate_ResponsePositive(per_stream_t *s, AssociateNegotiate_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    decode_NegotiationParameter(s, &v->negotiationParameters);
    return 0;
}

int encode_AssociateNegotiate_ResponseNegative(per_stream_t *s, const AssociateNegotiate_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_AssociateNegotiate_ResponseNegative(per_stream_t *s, AssociateNegotiate_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetDataValues_Entry(per_stream_t *s, const GetDataValues_Entry *v) {
    per_encode_visible_string(s, v->dataRef, 129);
    per_encode_small_non_negative(s, v->data._choice);
    return 0;
}

int decode_GetDataValues_Entry(per_stream_t *s, GetDataValues_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
        return 0;
}

int encode_GetDataValues_Request(per_stream_t *s, const GetDataValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->dataRefs_count);
    for (int _i = 0; _i < v->dataRefs_count; _i++) {
        /* TODO: encode ObjectReference element */
    }
    return 0;
}

int decode_GetDataValues_Request(per_stream_t *s, GetDataValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    /* TODO: decode SEQUENCE OF ObjectReference */
    return 0;
}

int encode_GetDataValues_ResponsePositive(per_stream_t *s, const GetDataValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->values_count);
    for (int _i = 0; _i < v->values_count; _i++) {
        encode_GetDataValues_Entry(s, &v->values[_i]);
    }
    return 0;
}

int decode_GetDataValues_ResponsePositive(per_stream_t *s, GetDataValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->values_count = (int)_count;
        v->values = calloc(_count, sizeof(*v->values));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_GetDataValues_Entry(s, &v->values[_i]);
        }
    }
    return 0;
}

int encode_GetDataValues_ResponseNegative(per_stream_t *s, const GetDataValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetDataValues_ResponseNegative(per_stream_t *s, GetDataValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SetDataValues_Entry(per_stream_t *s, const SetDataValues_Entry *v) {
    per_encode_visible_string(s, v->dataRef, 129);
    per_encode_small_non_negative(s, v->data._choice);
    return 0;
}

int decode_SetDataValues_Entry(per_stream_t *s, SetDataValues_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
        return 0;
}

int encode_SetDataValues_Request(per_stream_t *s, const SetDataValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->values_count);
    for (int _i = 0; _i < v->values_count; _i++) {
        encode_SetDataValues_Entry(s, &v->values[_i]);
    }
    return 0;
}

int decode_SetDataValues_Request(per_stream_t *s, SetDataValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->values_count = (int)_count;
        v->values = calloc(_count, sizeof(*v->values));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_SetDataValues_Entry(s, &v->values[_i]);
        }
    }
    return 0;
}

int encode_SetDataValues_ResponsePositive(per_stream_t *s, const SetDataValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_SetDataValues_ResponsePositive(per_stream_t *s, SetDataValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_SetDataValues_ResponseNegative(per_stream_t *s, const SetDataValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SetDataValues_ResponseNegative(per_stream_t *s, SetDataValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetDataDefinition_Entry(per_stream_t *s, const GetDataDefinition_Entry *v) {
    per_encode_visible_string(s, v->dataRef, 129);
    encode_DataDefinition(s, &v->dataDefinition);
    return 0;
}

int decode_GetDataDefinition_Entry(per_stream_t *s, GetDataDefinition_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
    decode_DataDefinition(s, &v->dataDefinition);
    return 0;
}

int encode_GetDataDefinition_Request(per_stream_t *s, const GetDataDefinition_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->dataRef, 129);
    return 0;
}

int decode_GetDataDefinition_Request(per_stream_t *s, GetDataDefinition_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
    return 0;
}

int encode_GetDataDefinition_ResponsePositive(per_stream_t *s, const GetDataDefinition_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    encode_GetDataDefinition_Entry(s, &v->definition);
    return 0;
}

int decode_GetDataDefinition_ResponsePositive(per_stream_t *s, GetDataDefinition_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    decode_GetDataDefinition_Entry(s, &v->definition);
    return 0;
}

int encode_GetDataDefinition_ResponseNegative(per_stream_t *s, const GetDataDefinition_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetDataDefinition_ResponseNegative(per_stream_t *s, GetDataDefinition_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetDataDirectory_Entry(per_stream_t *s, const GetDataDirectory_Entry *v) {
    per_encode_visible_string(s, v->dataRef, 129);
    per_encode_bit_string_fixed(s, v->fc, 16);
    return 0;
}

int decode_GetDataDirectory_Entry(per_stream_t *s, GetDataDirectory_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
        return 0;
}

int encode_GetDataDirectory_Request(per_stream_t *s, const GetDataDirectory_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->dataRef, 129);
    return 0;
}

int decode_GetDataDirectory_Request(per_stream_t *s, GetDataDirectory_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
    return 0;
}

int encode_GetDataDirectory_ResponsePositive(per_stream_t *s, const GetDataDirectory_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->entries_count);
    for (int _i = 0; _i < v->entries_count; _i++) {
        encode_GetDataDirectory_Entry(s, &v->entries[_i]);
    }
    return 0;
}

int decode_GetDataDirectory_ResponsePositive(per_stream_t *s, GetDataDirectory_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->entries_count = (int)_count;
        v->entries = calloc(_count, sizeof(*v->entries));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_GetDataDirectory_Entry(s, &v->entries[_i]);
        }
    }
    return 0;
}

int encode_GetDataDirectory_ResponseNegative(per_stream_t *s, const GetDataDirectory_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetDataDirectory_ResponseNegative(per_stream_t *s, GetDataDirectory_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_Select_Request(per_stream_t *s, const Select_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->actCode, 0, 3);
    per_encode_visible_string(s, v->actRef, 129);
    if (v->_has_actData) {
        per_encode_boolean(s, 1); /* present */
per_encode_small_non_negative(s, v->actData._choice);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_Select_Request(per_stream_t *s, Select_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 3);
        v->actCode = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->actRef = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_actData = _b ? 1 : 0;
    }
    if (v->_has_actData) {
    }
    return 0;
}

int encode_Select_ResponsePositive(per_stream_t *s, const Select_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_bit_string_fixed(s, v->actCnf, 16);
    return 0;
}

int decode_Select_ResponsePositive(per_stream_t *s, Select_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
        return 0;
}

int encode_Select_ResponseNegative(per_stream_t *s, const Select_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_Select_ResponseNegative(per_stream_t *s, Select_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SelectWithValue_Request(per_stream_t *s, const SelectWithValue_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->actCode, 0, 3);
    per_encode_visible_string(s, v->actRef, 129);
    per_encode_small_non_negative(s, v->actData._choice);
    return 0;
}

int decode_SelectWithValue_Request(per_stream_t *s, SelectWithValue_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 3);
        v->actCode = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->actRef = strdup(_buf);
    }
        return 0;
}

int encode_SelectWithValue_ResponsePositive(per_stream_t *s, const SelectWithValue_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_bit_string_fixed(s, v->actCnf, 16);
    return 0;
}

int decode_SelectWithValue_ResponsePositive(per_stream_t *s, SelectWithValue_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
        return 0;
}

int encode_SelectWithValue_ResponseNegative(per_stream_t *s, const SelectWithValue_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SelectWithValue_ResponseNegative(per_stream_t *s, SelectWithValue_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_Operate_Request(per_stream_t *s, const Operate_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->actCode, 0, 3);
    per_encode_visible_string(s, v->actRef, 129);
    /* skip null field */
    per_encode_constrained_int(s, v->orCat, 0, 8);
    per_encode_octet_string(s, v->orIdent, v->orIdent_len, 65535);
    return 0;
}

int decode_Operate_Request(per_stream_t *s, Operate_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 3);
        v->actCode = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->actRef = strdup(_buf);
    }
    /* skip null field */
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 8);
        v->orCat = (int)_tmp;
    }
        return 0;
}

int encode_Operate_ResponsePositive(per_stream_t *s, const Operate_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_bit_string_fixed(s, v->actCnf, 16);
    return 0;
}

int decode_Operate_ResponsePositive(per_stream_t *s, Operate_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
        return 0;
}

int encode_Operate_ResponseNegative(per_stream_t *s, const Operate_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_Operate_ResponseNegative(per_stream_t *s, Operate_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_Cancel_Request(per_stream_t *s, const Cancel_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->actRef, 129);
    per_encode_constrained_int(s, v->actCode, 0, 3);
    return 0;
}

int decode_Cancel_Request(per_stream_t *s, Cancel_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->actRef = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 3);
        v->actCode = (int)_tmp;
    }
    return 0;
}

int encode_Cancel_ResponsePositive(per_stream_t *s, const Cancel_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_bit_string_fixed(s, v->actCnf, 16);
    return 0;
}

int decode_Cancel_ResponsePositive(per_stream_t *s, Cancel_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
        return 0;
}

int encode_Cancel_ResponseNegative(per_stream_t *s, const Cancel_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_Cancel_ResponseNegative(per_stream_t *s, Cancel_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_TimeActivatedOperate_Request(per_stream_t *s, const TimeActivatedOperate_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->actCode, 0, 3);
    per_encode_visible_string(s, v->actRef, 129);
    if (v->_has_actData) {
        per_encode_boolean(s, 1); /* present */
per_encode_small_non_negative(s, v->actData._choice);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    per_encode_constrained_int(s, v->orCat, 0, 8);
    per_encode_octet_string(s, v->orIdent, v->orIdent_len, 65535);
    encode_UtcTime(s, &v->tActTm);
    return 0;
}

int decode_TimeActivatedOperate_Request(per_stream_t *s, TimeActivatedOperate_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 3);
        v->actCode = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->actRef = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_actData = _b ? 1 : 0;
    }
    if (v->_has_actData) {
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 8);
        v->orCat = (int)_tmp;
    }
        decode_UtcTime(s, &v->tActTm);
    return 0;
}

int encode_TimeActivatedOperate_ResponsePositive(per_stream_t *s, const TimeActivatedOperate_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_bit_string_fixed(s, v->actCnf, 16);
    return 0;
}

int decode_TimeActivatedOperate_ResponsePositive(per_stream_t *s, TimeActivatedOperate_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
        return 0;
}

int encode_TimeActivatedOperate_ResponseNegative(per_stream_t *s, const TimeActivatedOperate_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_TimeActivatedOperate_ResponseNegative(per_stream_t *s, TimeActivatedOperate_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_CommandTermination(per_stream_t *s, const CommandTermination *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->actRef, 129);
    per_encode_bit_string_fixed(s, v->actCnf, 16);
    if (v->_has_actData) {
        per_encode_boolean(s, 1); /* present */
per_encode_small_non_negative(s, v->actData._choice);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_CommandTermination(per_stream_t *s, CommandTermination *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->actRef = strdup(_buf);
    }
        {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_actData = _b ? 1 : 0;
    }
    if (v->_has_actData) {
    }
    return 0;
}

int encode_TimeActivatedOperateTermination(per_stream_t *s, const TimeActivatedOperateTermination *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->actRef, 129);
    per_encode_bit_string_fixed(s, v->actCnf, 16);
    encode_UtcTime(s, &v->tActTm);
    if (v->_has_actData) {
        per_encode_boolean(s, 1); /* present */
per_encode_small_non_negative(s, v->actData._choice);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_TimeActivatedOperateTermination(per_stream_t *s, TimeActivatedOperateTermination *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->actRef = strdup(_buf);
    }
        decode_UtcTime(s, &v->tActTm);
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_actData = _b ? 1 : 0;
    }
    if (v->_has_actData) {
    }
    return 0;
}

int encode_GetBRCBValues_Request(per_stream_t *s, const GetBRCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->brcbRef, 129);
    return 0;
}

int decode_GetBRCBValues_Request(per_stream_t *s, GetBRCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->brcbRef = strdup(_buf);
    }
    return 0;
}

int encode_GetBRCBValues_ResponsePositive(per_stream_t *s, const GetBRCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->brcbName, 129);
    per_encode_visible_string(s, v->rptID, 255);
    per_encode_boolean(s, v->rptEna);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_bit_string_fixed(s, v->optFlds, 10);
    per_encode_constrained_int(s, v->bufTm, 0, 4294967295);
    per_encode_constrained_int(s, v->sqNum, 0, 65535);
    per_encode_bit_string_fixed(s, v->trgOps, 6);
    per_encode_constrained_int(s, v->intgPd, 0, 4294967295);
    per_encode_boolean(s, v->gi);
    per_encode_boolean(s, v->purgeBuf);
    per_encode_octet_string_fixed(s, v->entryID, 8);
    encode_BinaryTime(s, &v->timeOfEntry);
    per_encode_octet_string(s, v->owner, v->owner_len, 65535);
    return 0;
}

int decode_GetBRCBValues_ResponsePositive(per_stream_t *s, GetBRCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->brcbName = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->rptID = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->rptEna = _b ? 1 : 0;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->bufTm = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->sqNum = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->intgPd = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->gi = _b ? 1 : 0;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->purgeBuf = _b ? 1 : 0;
    }
        decode_BinaryTime(s, &v->timeOfEntry);
    /* TODO: decode owner */
    return 0;
}

int encode_GetBRCBValues_ResponseNegative(per_stream_t *s, const GetBRCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetBRCBValues_ResponseNegative(per_stream_t *s, GetBRCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SetBRCBValues_Entry(per_stream_t *s, const SetBRCBValues_Entry *v) {
    per_encode_visible_string(s, v->reference, 129);
    if (v->_has_rptEna) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->rptEna);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_rptID) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->rptID, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_datSet) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->datSet, 129);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_confRev) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_optFlds) {
        per_encode_boolean(s, 1); /* present */
per_encode_bit_string_fixed(s, v->optFlds, 10);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_bufTm) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->bufTm, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_trgOps) {
        per_encode_boolean(s, 1); /* present */
per_encode_bit_string_fixed(s, v->trgOps, 6);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_intgPd) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->intgPd, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_gi) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->gi);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_purgeBuf) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->purgeBuf);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_entryID) {
        per_encode_boolean(s, 1); /* present */
per_encode_octet_string_fixed(s, v->entryID, 8);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_timeOfEntry) {
        per_encode_boolean(s, 1); /* present */
encode_BinaryTime(s, &v->timeOfEntry);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_reserved) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->reserved);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_SetBRCBValues_Entry(per_stream_t *s, SetBRCBValues_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->reference = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_rptEna = _b ? 1 : 0;
    }
    if (v->_has_rptEna) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->rptEna = _b ? 1 : 0;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_rptID = _b ? 1 : 0;
    }
    if (v->_has_rptID) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->rptID = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_datSet = _b ? 1 : 0;
    }
    if (v->_has_datSet) {
{
            char _buf[130];
            per_decode_visible_string(s, _buf, 129);
            v->datSet = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_confRev = _b ? 1 : 0;
    }
    if (v->_has_confRev) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->confRev = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_optFlds = _b ? 1 : 0;
    }
    if (v->_has_optFlds) {
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_bufTm = _b ? 1 : 0;
    }
    if (v->_has_bufTm) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->bufTm = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_trgOps = _b ? 1 : 0;
    }
    if (v->_has_trgOps) {
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_intgPd = _b ? 1 : 0;
    }
    if (v->_has_intgPd) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->intgPd = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_gi = _b ? 1 : 0;
    }
    if (v->_has_gi) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->gi = _b ? 1 : 0;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_purgeBuf = _b ? 1 : 0;
    }
    if (v->_has_purgeBuf) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->purgeBuf = _b ? 1 : 0;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_entryID = _b ? 1 : 0;
    }
    if (v->_has_entryID) {
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_timeOfEntry = _b ? 1 : 0;
    }
    if (v->_has_timeOfEntry) {
decode_BinaryTime(s, &v->timeOfEntry);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_reserved = _b ? 1 : 0;
    }
    if (v->_has_reserved) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->reserved = _b ? 1 : 0;
        }
    }
    return 0;
}

int encode_SetBRCBValues_Request(per_stream_t *s, const SetBRCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->brcbValues_count);
    for (int _i = 0; _i < v->brcbValues_count; _i++) {
        encode_SetBRCBValues_Entry(s, &v->brcbValues[_i]);
    }
    return 0;
}

int decode_SetBRCBValues_Request(per_stream_t *s, SetBRCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->brcbValues_count = (int)_count;
        v->brcbValues = calloc(_count, sizeof(*v->brcbValues));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_SetBRCBValues_Entry(s, &v->brcbValues[_i]);
        }
    }
    return 0;
}

int encode_SetBRCBValues_ResponsePositive(per_stream_t *s, const SetBRCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_SetBRCBValues_ResponsePositive(per_stream_t *s, SetBRCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_SetBRCBValues_ResponseNegative(per_stream_t *s, const SetBRCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SetBRCBValues_ResponseNegative(per_stream_t *s, SetBRCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetURCBValues_Request(per_stream_t *s, const GetURCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->urcbRef, 129);
    return 0;
}

int decode_GetURCBValues_Request(per_stream_t *s, GetURCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->urcbRef = strdup(_buf);
    }
    return 0;
}

int encode_GetURCBValues_ResponsePositive(per_stream_t *s, const GetURCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->urcbName, 129);
    per_encode_visible_string(s, v->rptID, 255);
    per_encode_boolean(s, v->rptEna);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_bit_string_fixed(s, v->optFlds, 10);
    per_encode_constrained_int(s, v->bufTm, 0, 4294967295);
    per_encode_constrained_int(s, v->sqNum, 0, 255);
    per_encode_bit_string_fixed(s, v->trgOps, 6);
    per_encode_constrained_int(s, v->intgPd, 0, 4294967295);
    per_encode_boolean(s, v->gi);
    per_encode_octet_string(s, v->owner, v->owner_len, 65535);
    return 0;
}

int decode_GetURCBValues_ResponsePositive(per_stream_t *s, GetURCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->urcbName = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->rptID = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->rptEna = _b ? 1 : 0;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->bufTm = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->sqNum = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->intgPd = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->gi = _b ? 1 : 0;
    }
    /* TODO: decode owner */
    return 0;
}

int encode_GetURCBValues_ResponseNegative(per_stream_t *s, const GetURCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetURCBValues_ResponseNegative(per_stream_t *s, GetURCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SetURCBValues_Entry(per_stream_t *s, const SetURCBValues_Entry *v) {
    per_encode_visible_string(s, v->reference, 129);
    if (v->_has_rptEna) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->rptEna);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_rptID) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->rptID, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_datSet) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->datSet, 129);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_confRev) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_optFlds) {
        per_encode_boolean(s, 1); /* present */
per_encode_bit_string_fixed(s, v->optFlds, 10);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_bufTm) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->bufTm, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_trgOps) {
        per_encode_boolean(s, 1); /* present */
per_encode_bit_string_fixed(s, v->trgOps, 6);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_intgPd) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->intgPd, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_gi) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->gi);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_reserved) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->reserved);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_SetURCBValues_Entry(per_stream_t *s, SetURCBValues_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->reference = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_rptEna = _b ? 1 : 0;
    }
    if (v->_has_rptEna) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->rptEna = _b ? 1 : 0;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_rptID = _b ? 1 : 0;
    }
    if (v->_has_rptID) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->rptID = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_datSet = _b ? 1 : 0;
    }
    if (v->_has_datSet) {
{
            char _buf[130];
            per_decode_visible_string(s, _buf, 129);
            v->datSet = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_confRev = _b ? 1 : 0;
    }
    if (v->_has_confRev) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->confRev = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_optFlds = _b ? 1 : 0;
    }
    if (v->_has_optFlds) {
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_bufTm = _b ? 1 : 0;
    }
    if (v->_has_bufTm) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->bufTm = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_trgOps = _b ? 1 : 0;
    }
    if (v->_has_trgOps) {
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_intgPd = _b ? 1 : 0;
    }
    if (v->_has_intgPd) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->intgPd = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_gi = _b ? 1 : 0;
    }
    if (v->_has_gi) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->gi = _b ? 1 : 0;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_reserved = _b ? 1 : 0;
    }
    if (v->_has_reserved) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->reserved = _b ? 1 : 0;
        }
    }
    return 0;
}

int encode_SetURCBValues_Request(per_stream_t *s, const SetURCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->urcbValues_count);
    for (int _i = 0; _i < v->urcbValues_count; _i++) {
        encode_SetURCBValues_Entry(s, &v->urcbValues[_i]);
    }
    return 0;
}

int decode_SetURCBValues_Request(per_stream_t *s, SetURCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->urcbValues_count = (int)_count;
        v->urcbValues = calloc(_count, sizeof(*v->urcbValues));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_SetURCBValues_Entry(s, &v->urcbValues[_i]);
        }
    }
    return 0;
}

int encode_SetURCBValues_ResponsePositive(per_stream_t *s, const SetURCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_SetURCBValues_ResponsePositive(per_stream_t *s, SetURCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_SetURCBValues_ResponseNegative(per_stream_t *s, const SetURCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SetURCBValues_ResponseNegative(per_stream_t *s, SetURCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_ReportEntryData(per_stream_t *s, const ReportEntryData *v) {
    per_encode_visible_string(s, v->dataRef, 129);
    per_encode_small_non_negative(s, v->entryData._choice);
    per_encode_bit_string_fixed(s, v->reasonCode, 6);
    if (v->_has_dataQuality) {
        per_encode_boolean(s, 1); /* present */
per_encode_bit_string_fixed(s, v->dataQuality, 13);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_dataTime) {
        per_encode_boolean(s, 1); /* present */
encode_BinaryTime(s, &v->dataTime);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_ReportEntryData(per_stream_t *s, ReportEntryData *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
            {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_dataQuality = _b ? 1 : 0;
    }
    if (v->_has_dataQuality) {
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_dataTime = _b ? 1 : 0;
    }
    if (v->_has_dataTime) {
decode_BinaryTime(s, &v->dataTime);
    }
    return 0;
}

int encode_ReportEntry(per_stream_t *s, const ReportEntry *v) {
    per_encode_octet_string_fixed(s, v->entryID, 8);
    encode_BinaryTime(s, &v->entryTime);
    per_encode_length(s, v->entryData_count);
    for (int _i = 0; _i < v->entryData_count; _i++) {
        encode_ReportEntryData(s, &v->entryData[_i]);
    }
    return 0;
}

int decode_ReportEntry(per_stream_t *s, ReportEntry *v) {
        decode_BinaryTime(s, &v->entryTime);
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->entryData_count = (int)_count;
        v->entryData = calloc(_count, sizeof(*v->entryData));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_ReportEntryData(s, &v->entryData[_i]);
        }
    }
    return 0;
}

int encode_Report(per_stream_t *s, const Report *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->rptID, 255);
    per_encode_bit_string_fixed(s, v->optFlds, 10);
    per_encode_constrained_int(s, v->sqNum, 0, 65535);
    per_encode_constrained_int(s, v->subSQNum, 0, 255);
    per_encode_boolean(s, v->moreSegmentsFollow);
    per_encode_length(s, v->entry_count);
    for (int _i = 0; _i < v->entry_count; _i++) {
        encode_ReportEntry(s, &v->entry[_i]);
    }
    return 0;
}

int decode_Report(per_stream_t *s, Report *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->rptID = strdup(_buf);
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->sqNum = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->subSQNum = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->moreSegmentsFollow = _b ? 1 : 0;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->entry_count = (int)_count;
        v->entry = calloc(_count, sizeof(*v->entry));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_ReportEntry(s, &v->entry[_i]);
        }
    }
    return 0;
}

int encode_CreateDataSet_Entry(per_stream_t *s, const CreateDataSet_Entry *v) {
    per_encode_visible_string(s, v->dataRef, 129);
    return 0;
}

int decode_CreateDataSet_Entry(per_stream_t *s, CreateDataSet_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
    return 0;
}

int encode_CreateDataSet_Request(per_stream_t *s, const CreateDataSet_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->dataSetName, 255);
    per_encode_length(s, v->dataEntries_count);
    for (int _i = 0; _i < v->dataEntries_count; _i++) {
        encode_CreateDataSet_Entry(s, &v->dataEntries[_i]);
    }
    return 0;
}

int decode_CreateDataSet_Request(per_stream_t *s, CreateDataSet_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->dataSetName = strdup(_buf);
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->dataEntries_count = (int)_count;
        v->dataEntries = calloc(_count, sizeof(*v->dataEntries));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_CreateDataSet_Entry(s, &v->dataEntries[_i]);
        }
    }
    return 0;
}

int encode_CreateDataSet_ResponsePositive(per_stream_t *s, const CreateDataSet_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_CreateDataSet_ResponsePositive(per_stream_t *s, CreateDataSet_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_CreateDataSet_ResponseNegative(per_stream_t *s, const CreateDataSet_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_CreateDataSet_ResponseNegative(per_stream_t *s, CreateDataSet_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_DeleteDataSet_Request(per_stream_t *s, const DeleteDataSet_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->dataSetRef, 129);
    return 0;
}

int decode_DeleteDataSet_Request(per_stream_t *s, DeleteDataSet_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataSetRef = strdup(_buf);
    }
    return 0;
}

int encode_DeleteDataSet_ResponsePositive(per_stream_t *s, const DeleteDataSet_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_DeleteDataSet_ResponsePositive(per_stream_t *s, DeleteDataSet_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_DeleteDataSet_ResponseNegative(per_stream_t *s, const DeleteDataSet_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_DeleteDataSet_ResponseNegative(per_stream_t *s, DeleteDataSet_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetDataSetDirectory_Request(per_stream_t *s, const GetDataSetDirectory_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->dataSetRef, 129);
    return 0;
}

int decode_GetDataSetDirectory_Request(per_stream_t *s, GetDataSetDirectory_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataSetRef = strdup(_buf);
    }
    return 0;
}

int encode_GetDataSetDirectory_ResponsePositive(per_stream_t *s, const GetDataSetDirectory_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->dataSetEntries_count);
    for (int _i = 0; _i < v->dataSetEntries_count; _i++) {
        /* TODO: encode  element */
    }
    return 0;
}

int decode_GetDataSetDirectory_ResponsePositive(per_stream_t *s, GetDataSetDirectory_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    /* TODO: decode SEQUENCE OF  */
    return 0;
}

int encode_GetDataSetDirectory_ResponseNegative(per_stream_t *s, const GetDataSetDirectory_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetDataSetDirectory_ResponseNegative(per_stream_t *s, GetDataSetDirectory_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetDataSetValues_Request(per_stream_t *s, const GetDataSetValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->dataSetRef, 129);
    return 0;
}

int decode_GetDataSetValues_Request(per_stream_t *s, GetDataSetValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataSetRef = strdup(_buf);
    }
    return 0;
}

int encode_GetDataSetValues_ResponsePositive(per_stream_t *s, const GetDataSetValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->values_count);
    for (int _i = 0; _i < v->values_count; _i++) {
        encode_Data(s, &v->values[_i]);
    }
    return 0;
}

int decode_GetDataSetValues_ResponsePositive(per_stream_t *s, GetDataSetValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->values_count = (int)_count;
        v->values = calloc(_count, sizeof(*v->values));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_Data(s, &v->values[_i]);
        }
    }
    return 0;
}

int encode_GetDataSetValues_ResponseNegative(per_stream_t *s, const GetDataSetValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetDataSetValues_ResponseNegative(per_stream_t *s, GetDataSetValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SetDataSetValues_Request(per_stream_t *s, const SetDataSetValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->dataSetRef, 129);
    per_encode_length(s, v->values_count);
    for (int _i = 0; _i < v->values_count; _i++) {
        encode_Data(s, &v->values[_i]);
    }
    return 0;
}

int decode_SetDataSetValues_Request(per_stream_t *s, SetDataSetValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataSetRef = strdup(_buf);
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->values_count = (int)_count;
        v->values = calloc(_count, sizeof(*v->values));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_Data(s, &v->values[_i]);
        }
    }
    return 0;
}

int encode_SetDataSetValues_ResponsePositive(per_stream_t *s, const SetDataSetValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_SetDataSetValues_ResponsePositive(per_stream_t *s, SetDataSetValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_SetDataSetValues_ResponseNegative(per_stream_t *s, const SetDataSetValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SetDataSetValues_ResponseNegative(per_stream_t *s, SetDataSetValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetServerDirectory_Request(per_stream_t *s, const GetServerDirectory_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_GetServerDirectory_Request(per_stream_t *s, GetServerDirectory_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_GetServerDirectory_ResponsePositive(per_stream_t *s, const GetServerDirectory_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->logicalDevices_count);
    for (int _i = 0; _i < v->logicalDevices_count; _i++) {
        /* TODO: encode  element */
    }
    return 0;
}

int decode_GetServerDirectory_ResponsePositive(per_stream_t *s, GetServerDirectory_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    /* TODO: decode SEQUENCE OF  */
    return 0;
}

int encode_GetServerDirectory_ResponseNegative(per_stream_t *s, const GetServerDirectory_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetServerDirectory_ResponseNegative(per_stream_t *s, GetServerDirectory_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetLogicalDeviceDirectory_Request(per_stream_t *s, const GetLogicalDeviceDirectory_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->ldName, 255);
    return 0;
}

int decode_GetLogicalDeviceDirectory_Request(per_stream_t *s, GetLogicalDeviceDirectory_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->ldName = strdup(_buf);
    }
    return 0;
}

int encode_GetLogicalDeviceDirectory_ResponsePositive(per_stream_t *s, const GetLogicalDeviceDirectory_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->logicalNodes_count);
    for (int _i = 0; _i < v->logicalNodes_count; _i++) {
        /* TODO: encode  element */
    }
    return 0;
}

int decode_GetLogicalDeviceDirectory_ResponsePositive(per_stream_t *s, GetLogicalDeviceDirectory_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    /* TODO: decode SEQUENCE OF  */
    return 0;
}

int encode_GetLogicalDeviceDirectory_ResponseNegative(per_stream_t *s, const GetLogicalDeviceDirectory_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetLogicalDeviceDirectory_ResponseNegative(per_stream_t *s, GetLogicalDeviceDirectory_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_ObjectClass(per_stream_t *s, const ObjectClass *v) {
    per_encode_visible_string(s, v->classType, 255);
    return 0;
}

int decode_ObjectClass(per_stream_t *s, ObjectClass *v) {
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->classType = strdup(_buf);
    }
    return 0;
}

int encode_GetLogicalNodeDirectory_Request(per_stream_t *s, const GetLogicalNodeDirectory_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->ldName, 255);
    per_encode_visible_string(s, v->lnName, 255);
    if (v->_has_objectClass) {
        per_encode_boolean(s, 1); /* present */
encode_ObjectClass(s, &v->objectClass);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_GetLogicalNodeDirectory_Request(per_stream_t *s, GetLogicalNodeDirectory_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->ldName = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->lnName = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_objectClass = _b ? 1 : 0;
    }
    if (v->_has_objectClass) {
decode_ObjectClass(s, &v->objectClass);
    }
    return 0;
}

int encode_GetLogicalNodeDirectory_ResponsePositive(per_stream_t *s, const GetLogicalNodeDirectory_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->objects_count);
    for (int _i = 0; _i < v->objects_count; _i++) {
        encode_CmsReference(s, &v->objects[_i]);
    }
    return 0;
}

int decode_GetLogicalNodeDirectory_ResponsePositive(per_stream_t *s, GetLogicalNodeDirectory_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->objects_count = (int)_count;
        v->objects = calloc(_count, sizeof(*v->objects));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_CmsReference(s, &v->objects[_i]);
        }
    }
    return 0;
}

int encode_CmsReference(per_stream_t *s, const CmsReference *v) {
    per_encode_visible_string(s, v->reference, 129);
    per_encode_bit_string_fixed(s, v->fc, 16);
    return 0;
}

int decode_CmsReference(per_stream_t *s, CmsReference *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->reference = strdup(_buf);
    }
        return 0;
}

int encode_GetLogicalNodeDirectory_ResponseNegative(per_stream_t *s, const GetLogicalNodeDirectory_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetLogicalNodeDirectory_ResponseNegative(per_stream_t *s, GetLogicalNodeDirectory_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetAllDataValues_Request(per_stream_t *s, const GetAllDataValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->ldName, 255);
    return 0;
}

int decode_GetAllDataValues_Request(per_stream_t *s, GetAllDataValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->ldName = strdup(_buf);
    }
    return 0;
}

int encode_GetAllDataValues_ResponsePositive(per_stream_t *s, const GetAllDataValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->values_count);
    for (int _i = 0; _i < v->values_count; _i++) {
        encode_CmsDataEntry(s, &v->values[_i]);
    }
    return 0;
}

int decode_GetAllDataValues_ResponsePositive(per_stream_t *s, GetAllDataValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->values_count = (int)_count;
        v->values = calloc(_count, sizeof(*v->values));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_CmsDataEntry(s, &v->values[_i]);
        }
    }
    return 0;
}

int encode_CmsDataEntry(per_stream_t *s, const CmsDataEntry *v) {
    per_encode_visible_string(s, v->dataRef, 129);
    per_encode_small_non_negative(s, v->dataValue._choice);
    return 0;
}

int decode_CmsDataEntry(per_stream_t *s, CmsDataEntry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
        return 0;
}

int encode_GetAllDataValues_ResponseNegative(per_stream_t *s, const GetAllDataValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetAllDataValues_ResponseNegative(per_stream_t *s, GetAllDataValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetAllDataDefinition_Request(per_stream_t *s, const GetAllDataDefinition_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->ldName, 255);
    return 0;
}

int decode_GetAllDataDefinition_Request(per_stream_t *s, GetAllDataDefinition_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->ldName = strdup(_buf);
    }
    return 0;
}

int encode_GetAllDataDefinition_ResponsePositive(per_stream_t *s, const GetAllDataDefinition_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->definitions_count);
    for (int _i = 0; _i < v->definitions_count; _i++) {
        encode_CmsDataDefinitionEntry(s, &v->definitions[_i]);
    }
    return 0;
}

int decode_GetAllDataDefinition_ResponsePositive(per_stream_t *s, GetAllDataDefinition_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->definitions_count = (int)_count;
        v->definitions = calloc(_count, sizeof(*v->definitions));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_CmsDataDefinitionEntry(s, &v->definitions[_i]);
        }
    }
    return 0;
}

int encode_CmsDataDefinitionEntry(per_stream_t *s, const CmsDataDefinitionEntry *v) {
    per_encode_visible_string(s, v->dataRef, 129);
    per_encode_bit_string_fixed(s, v->fc, 16);
    per_encode_visible_string(s, v->dataName, 255);
    per_encode_visible_string(s, v->dataType, 255);
    return 0;
}

int decode_CmsDataDefinitionEntry(per_stream_t *s, CmsDataDefinitionEntry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
        {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->dataName = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->dataType = strdup(_buf);
    }
    return 0;
}

int encode_GetAllDataDefinition_ResponseNegative(per_stream_t *s, const GetAllDataDefinition_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetAllDataDefinition_ResponseNegative(per_stream_t *s, GetAllDataDefinition_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_CBType(per_stream_t *s, int v) {
    return per_encode_constrained_int(s, v, 0, 5);
}

int decode_CBType(per_stream_t *s, int *v) {
    int64_t _tmp;
    int err = per_decode_constrained_int(s, &_tmp, 0, 5);
    if (err) return err;
    *v = (int)_tmp;
    return 0;
}

int encode_CmsCBValue(per_stream_t *s, const CmsCBValue *v) {
    per_encode_visible_string(s, v->cbRef, 129);
    per_encode_constrained_int(s, v->cbType, 0, 5);
    return 0;
}

int decode_CmsCBValue(per_stream_t *s, CmsCBValue *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->cbRef = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 5);
        v->cbType = (int)_tmp;
    }
    return 0;
}

int encode_CmsCBValueEntry(per_stream_t *s, const CmsCBValueEntry *v) {
    per_encode_visible_string(s, v->ldName, 255);
    per_encode_length(s, v->cbValues_count);
    for (int _i = 0; _i < v->cbValues_count; _i++) {
        encode_CmsCBValue(s, &v->cbValues[_i]);
    }
    return 0;
}

int decode_CmsCBValueEntry(per_stream_t *s, CmsCBValueEntry *v) {
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->ldName = strdup(_buf);
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->cbValues_count = (int)_count;
        v->cbValues = calloc(_count, sizeof(*v->cbValues));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_CmsCBValue(s, &v->cbValues[_i]);
        }
    }
    return 0;
}

int encode_GetAllCBValues_Request(per_stream_t *s, const GetAllCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->ldName, 255);
    return 0;
}

int decode_GetAllCBValues_Request(per_stream_t *s, GetAllCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->ldName = strdup(_buf);
    }
    return 0;
}

int encode_GetAllCBValues_ResponsePositive(per_stream_t *s, const GetAllCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->cbEntries_count);
    for (int _i = 0; _i < v->cbEntries_count; _i++) {
        encode_CmsCBValueEntry(s, &v->cbEntries[_i]);
    }
    return 0;
}

int decode_GetAllCBValues_ResponsePositive(per_stream_t *s, GetAllCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->cbEntries_count = (int)_count;
        v->cbEntries = calloc(_count, sizeof(*v->cbEntries));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_CmsCBValueEntry(s, &v->cbEntries[_i]);
        }
    }
    return 0;
}

int encode_GetAllCBValues_ResponseNegative(per_stream_t *s, const GetAllCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetAllCBValues_ResponseNegative(per_stream_t *s, GetAllCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_CmsACSIClass(per_stream_t *s, const CmsACSIClass *v) {
    per_encode_visible_string(s, v->className, 255);
    per_encode_visible_string(s, v->classVersion, 255);
    return 0;
}

int decode_CmsACSIClass(per_stream_t *s, CmsACSIClass *v) {
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->className = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->classVersion = strdup(_buf);
    }
    return 0;
}

int encode_GetACSIClasses_Request(per_stream_t *s, const GetACSIClasses_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_GetACSIClasses_Request(per_stream_t *s, GetACSIClasses_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_GetACSIClasses_ResponsePositive(per_stream_t *s, const GetACSIClasses_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->acsiClasses_count);
    for (int _i = 0; _i < v->acsiClasses_count; _i++) {
        encode_CmsACSIClass(s, &v->acsiClasses[_i]);
    }
    return 0;
}

int decode_GetACSIClasses_ResponsePositive(per_stream_t *s, GetACSIClasses_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->acsiClasses_count = (int)_count;
        v->acsiClasses = calloc(_count, sizeof(*v->acsiClasses));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_CmsACSIClass(s, &v->acsiClasses[_i]);
        }
    }
    return 0;
}

int encode_GetACSIClasses_ResponseNegative(per_stream_t *s, const GetACSIClasses_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetACSIClasses_ResponseNegative(per_stream_t *s, GetACSIClasses_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetSGCBValues_Request(per_stream_t *s, const GetSGCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->sgcbRef, 129);
    return 0;
}

int decode_GetSGCBValues_Request(per_stream_t *s, GetSGCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->sgcbRef = strdup(_buf);
    }
    return 0;
}

int encode_GetSGCBValues_ResponsePositive(per_stream_t *s, const GetSGCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->sgcbName, 129);
    per_encode_constrained_int(s, v->numOfSG, 0, 255);
    per_encode_constrained_int(s, v->actSG, 0, 255);
    per_encode_constrained_int(s, v->editSG, 0, 255);
    per_encode_boolean(s, v->cnfEdit);
    encode_UtcTime(s, &v->lActTm);
    return 0;
}

int decode_GetSGCBValues_ResponsePositive(per_stream_t *s, GetSGCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->sgcbName = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->numOfSG = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->actSG = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->editSG = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->cnfEdit = _b ? 1 : 0;
    }
    decode_UtcTime(s, &v->lActTm);
    return 0;
}

int encode_GetSGCBValues_ResponseNegative(per_stream_t *s, const GetSGCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetSGCBValues_ResponseNegative(per_stream_t *s, GetSGCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SelectActiveSG_Request(per_stream_t *s, const SelectActiveSG_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->sgcbRef, 129);
    per_encode_constrained_int(s, v->actSG, 0, 255);
    return 0;
}

int decode_SelectActiveSG_Request(per_stream_t *s, SelectActiveSG_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->sgcbRef = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->actSG = (int)_tmp;
    }
    return 0;
}

int encode_SelectActiveSG_ResponsePositive(per_stream_t *s, const SelectActiveSG_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->actSG, 0, 255);
    encode_UtcTime(s, &v->lActTm);
    return 0;
}

int decode_SelectActiveSG_ResponsePositive(per_stream_t *s, SelectActiveSG_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->actSG = (int)_tmp;
    }
    decode_UtcTime(s, &v->lActTm);
    return 0;
}

int encode_SelectActiveSG_ResponseNegative(per_stream_t *s, const SelectActiveSG_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SelectActiveSG_ResponseNegative(per_stream_t *s, SelectActiveSG_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SelectEditSG_Request(per_stream_t *s, const SelectEditSG_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->sgcbRef, 129);
    per_encode_constrained_int(s, v->editSG, 0, 255);
    return 0;
}

int decode_SelectEditSG_Request(per_stream_t *s, SelectEditSG_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->sgcbRef = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->editSG = (int)_tmp;
    }
    return 0;
}

int encode_SelectEditSG_ResponsePositive(per_stream_t *s, const SelectEditSG_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->editSG, 0, 255);
    encode_UtcTime(s, &v->lActTm);
    return 0;
}

int decode_SelectEditSG_ResponsePositive(per_stream_t *s, SelectEditSG_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->editSG = (int)_tmp;
    }
    decode_UtcTime(s, &v->lActTm);
    return 0;
}

int encode_SelectEditSG_ResponseNegative(per_stream_t *s, const SelectEditSG_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SelectEditSG_ResponseNegative(per_stream_t *s, SelectEditSG_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_ConfirmEditSGValues_Request(per_stream_t *s, const ConfirmEditSGValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->sgcbRef, 129);
    return 0;
}

int decode_ConfirmEditSGValues_Request(per_stream_t *s, ConfirmEditSGValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->sgcbRef = strdup(_buf);
    }
    return 0;
}

int encode_ConfirmEditSGValues_ResponsePositive(per_stream_t *s, const ConfirmEditSGValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    encode_UtcTime(s, &v->lActTm);
    return 0;
}

int decode_ConfirmEditSGValues_ResponsePositive(per_stream_t *s, ConfirmEditSGValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    decode_UtcTime(s, &v->lActTm);
    return 0;
}

int encode_ConfirmEditSGValues_ResponseNegative(per_stream_t *s, const ConfirmEditSGValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_ConfirmEditSGValues_ResponseNegative(per_stream_t *s, ConfirmEditSGValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetEditSGValue_Request(per_stream_t *s, const GetEditSGValue_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->dataRef, 129);
    return 0;
}

int decode_GetEditSGValue_Request(per_stream_t *s, GetEditSGValue_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
    return 0;
}

int encode_GetEditSGValue_ResponsePositive(per_stream_t *s, const GetEditSGValue_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_small_non_negative(s, v->dataValue._choice);
    return 0;
}

int decode_GetEditSGValue_ResponsePositive(per_stream_t *s, GetEditSGValue_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
        return 0;
}

int encode_GetEditSGValue_ResponseNegative(per_stream_t *s, const GetEditSGValue_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetEditSGValue_ResponseNegative(per_stream_t *s, GetEditSGValue_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SetEditSGValue_Entry(per_stream_t *s, const SetEditSGValue_Entry *v) {
    per_encode_visible_string(s, v->dataRef, 129);
    per_encode_small_non_negative(s, v->dataValue._choice);
    return 0;
}

int decode_SetEditSGValue_Entry(per_stream_t *s, SetEditSGValue_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->dataRef = strdup(_buf);
    }
        return 0;
}

int encode_SetEditSGValue_Request(per_stream_t *s, const SetEditSGValue_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->values_count);
    for (int _i = 0; _i < v->values_count; _i++) {
        encode_SetEditSGValue_Entry(s, &v->values[_i]);
    }
    return 0;
}

int decode_SetEditSGValue_Request(per_stream_t *s, SetEditSGValue_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->values_count = (int)_count;
        v->values = calloc(_count, sizeof(*v->values));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_SetEditSGValue_Entry(s, &v->values[_i]);
        }
    }
    return 0;
}

int encode_SetEditSGValue_ResponsePositive(per_stream_t *s, const SetEditSGValue_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_SetEditSGValue_ResponsePositive(per_stream_t *s, SetEditSGValue_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_SetEditSGValue_ResponseNegative(per_stream_t *s, const SetEditSGValue_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SetEditSGValue_ResponseNegative(per_stream_t *s, SetEditSGValue_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetLCBValues_Request(per_stream_t *s, const GetLCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->lcbRef, 129);
    return 0;
}

int decode_GetLCBValues_Request(per_stream_t *s, GetLCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->lcbRef = strdup(_buf);
    }
    return 0;
}

int encode_GetLCBValues_ResponsePositive(per_stream_t *s, const GetLCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->lcbName, 129);
    per_encode_visible_string(s, v->rptID, 255);
    per_encode_boolean(s, v->logEna);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_bit_string_fixed(s, v->optFlds, 6);
    per_encode_constrained_int(s, v->bufTm, 0, 4294967295);
    per_encode_bit_string_fixed(s, v->trgOps, 6);
    per_encode_constrained_int(s, v->intgPd, 0, 4294967295);
    per_encode_visible_string(s, v->logRef, 129);
    return 0;
}

int decode_GetLCBValues_ResponsePositive(per_stream_t *s, GetLCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->lcbName = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->rptID = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->logEna = _b ? 1 : 0;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->bufTm = (int)_tmp;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->intgPd = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->logRef = strdup(_buf);
    }
    return 0;
}

int encode_GetLCBValues_ResponseNegative(per_stream_t *s, const GetLCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetLCBValues_ResponseNegative(per_stream_t *s, GetLCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SetLCBValues_Entry(per_stream_t *s, const SetLCBValues_Entry *v) {
    per_encode_visible_string(s, v->reference, 129);
    if (v->_has_logEna) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->logEna);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_rptID) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->rptID, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_datSet) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->datSet, 129);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_confRev) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_optFlds) {
        per_encode_boolean(s, 1); /* present */
per_encode_bit_string_fixed(s, v->optFlds, 6);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_bufTm) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->bufTm, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_trgOps) {
        per_encode_boolean(s, 1); /* present */
per_encode_bit_string_fixed(s, v->trgOps, 6);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_intgPd) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->intgPd, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_logRef) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->logRef, 129);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_SetLCBValues_Entry(per_stream_t *s, SetLCBValues_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->reference = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_logEna = _b ? 1 : 0;
    }
    if (v->_has_logEna) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->logEna = _b ? 1 : 0;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_rptID = _b ? 1 : 0;
    }
    if (v->_has_rptID) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->rptID = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_datSet = _b ? 1 : 0;
    }
    if (v->_has_datSet) {
{
            char _buf[130];
            per_decode_visible_string(s, _buf, 129);
            v->datSet = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_confRev = _b ? 1 : 0;
    }
    if (v->_has_confRev) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->confRev = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_optFlds = _b ? 1 : 0;
    }
    if (v->_has_optFlds) {
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_bufTm = _b ? 1 : 0;
    }
    if (v->_has_bufTm) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->bufTm = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_trgOps = _b ? 1 : 0;
    }
    if (v->_has_trgOps) {
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_intgPd = _b ? 1 : 0;
    }
    if (v->_has_intgPd) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->intgPd = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_logRef = _b ? 1 : 0;
    }
    if (v->_has_logRef) {
{
            char _buf[130];
            per_decode_visible_string(s, _buf, 129);
            v->logRef = strdup(_buf);
        }
    }
    return 0;
}

int encode_SetLCBValues_Request(per_stream_t *s, const SetLCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->lcbValues_count);
    for (int _i = 0; _i < v->lcbValues_count; _i++) {
        encode_SetLCBValues_Entry(s, &v->lcbValues[_i]);
    }
    return 0;
}

int decode_SetLCBValues_Request(per_stream_t *s, SetLCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->lcbValues_count = (int)_count;
        v->lcbValues = calloc(_count, sizeof(*v->lcbValues));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_SetLCBValues_Entry(s, &v->lcbValues[_i]);
        }
    }
    return 0;
}

int encode_SetLCBValues_ResponsePositive(per_stream_t *s, const SetLCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_SetLCBValues_ResponsePositive(per_stream_t *s, SetLCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_SetLCBValues_ResponseNegative(per_stream_t *s, const SetLCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SetLCBValues_ResponseNegative(per_stream_t *s, SetLCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_LogStatusValue(per_stream_t *s, const LogStatusValue *v) {
    per_encode_visible_string(s, v->ldName, 255);
    per_encode_octet_string_fixed(s, v->oldEntr, 8);
    per_encode_octet_string_fixed(s, v->newEntr, 8);
    encode_BinaryTime(s, &v->oldTm);
    encode_BinaryTime(s, &v->newTm);
    return 0;
}

int decode_LogStatusValue(per_stream_t *s, LogStatusValue *v) {
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->ldName = strdup(_buf);
    }
            decode_BinaryTime(s, &v->oldTm);
    decode_BinaryTime(s, &v->newTm);
    return 0;
}

int encode_GetLogStatusValues_Request(per_stream_t *s, const GetLogStatusValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->logRef, 129);
    return 0;
}

int decode_GetLogStatusValues_Request(per_stream_t *s, GetLogStatusValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->logRef = strdup(_buf);
    }
    return 0;
}

int encode_GetLogStatusValues_ResponsePositive(per_stream_t *s, const GetLogStatusValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->logs_count);
    for (int _i = 0; _i < v->logs_count; _i++) {
        encode_LogStatusValue(s, &v->logs[_i]);
    }
    return 0;
}

int decode_GetLogStatusValues_ResponsePositive(per_stream_t *s, GetLogStatusValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->logs_count = (int)_count;
        v->logs = calloc(_count, sizeof(*v->logs));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_LogStatusValue(s, &v->logs[_i]);
        }
    }
    return 0;
}

int encode_GetLogStatusValues_ResponseNegative(per_stream_t *s, const GetLogStatusValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetLogStatusValues_ResponseNegative(per_stream_t *s, GetLogStatusValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_LogEntry(per_stream_t *s, const LogEntry *v) {
    per_encode_octet_string_fixed(s, v->entryID, 8);
    encode_BinaryTime(s, &v->entryTime);
    per_encode_small_non_negative(s, v->entryData._choice);
    return 0;
}

int decode_LogEntry(per_stream_t *s, LogEntry *v) {
        decode_BinaryTime(s, &v->entryTime);
        return 0;
}

int encode_QueryLogAfter_Request(per_stream_t *s, const QueryLogAfter_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->logRef, 129);
    per_encode_octet_string_fixed(s, v->entryID, 8);
    encode_BinaryTime(s, &v->timeOfEntry);
    return 0;
}

int decode_QueryLogAfter_Request(per_stream_t *s, QueryLogAfter_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->logRef = strdup(_buf);
    }
        decode_BinaryTime(s, &v->timeOfEntry);
    return 0;
}

int encode_QueryLogAfter_ResponsePositive(per_stream_t *s, const QueryLogAfter_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->entry_count);
    for (int _i = 0; _i < v->entry_count; _i++) {
        encode_LogEntry(s, &v->entry[_i]);
    }
    return 0;
}

int decode_QueryLogAfter_ResponsePositive(per_stream_t *s, QueryLogAfter_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->entry_count = (int)_count;
        v->entry = calloc(_count, sizeof(*v->entry));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_LogEntry(s, &v->entry[_i]);
        }
    }
    return 0;
}

int encode_QueryLogAfter_ResponseNegative(per_stream_t *s, const QueryLogAfter_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_QueryLogAfter_ResponseNegative(per_stream_t *s, QueryLogAfter_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_QueryLogByTime_Request(per_stream_t *s, const QueryLogByTime_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->logRef, 129);
    encode_BinaryTime(s, &v->startTime);
    encode_BinaryTime(s, &v->stopTime);
    return 0;
}

int decode_QueryLogByTime_Request(per_stream_t *s, QueryLogByTime_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->logRef = strdup(_buf);
    }
    decode_BinaryTime(s, &v->startTime);
    decode_BinaryTime(s, &v->stopTime);
    return 0;
}

int encode_QueryLogByTime_ResponsePositive(per_stream_t *s, const QueryLogByTime_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->entry_count);
    for (int _i = 0; _i < v->entry_count; _i++) {
        encode_LogEntry(s, &v->entry[_i]);
    }
    return 0;
}

int decode_QueryLogByTime_ResponsePositive(per_stream_t *s, QueryLogByTime_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->entry_count = (int)_count;
        v->entry = calloc(_count, sizeof(*v->entry));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_LogEntry(s, &v->entry[_i]);
        }
    }
    return 0;
}

int encode_QueryLogByTime_ResponseNegative(per_stream_t *s, const QueryLogByTime_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_QueryLogByTime_ResponseNegative(per_stream_t *s, QueryLogByTime_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetFile_Request(per_stream_t *s, const GetFile_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->fileName, 255);
    return 0;
}

int decode_GetFile_Request(per_stream_t *s, GetFile_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->fileName = strdup(_buf);
    }
    return 0;
}

int encode_GetFile_ResponsePositive(per_stream_t *s, const GetFile_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->fileSize, 0, 4294967295);
    per_encode_octet_string(s, v->fileData, v->fileData_len, 65535);
    return 0;
}

int decode_GetFile_ResponsePositive(per_stream_t *s, GetFile_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->fileSize = (int)_tmp;
    }
    /* TODO: decode fileData */
    return 0;
}

int encode_GetFile_ResponseNegative(per_stream_t *s, const GetFile_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetFile_ResponseNegative(per_stream_t *s, GetFile_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SetFile_Request(per_stream_t *s, const SetFile_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->fileName, 255);
    per_encode_octet_string(s, v->fileData, v->fileData_len, 65535);
    return 0;
}

int decode_SetFile_Request(per_stream_t *s, SetFile_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->fileName = strdup(_buf);
    }
    /* TODO: decode fileData */
    return 0;
}

int encode_SetFile_ResponsePositive(per_stream_t *s, const SetFile_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->fileSize, 0, 4294967295);
    return 0;
}

int decode_SetFile_ResponsePositive(per_stream_t *s, SetFile_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->fileSize = (int)_tmp;
    }
    return 0;
}

int encode_SetFile_ResponseNegative(per_stream_t *s, const SetFile_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SetFile_ResponseNegative(per_stream_t *s, SetFile_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_DeleteFile_Request(per_stream_t *s, const DeleteFile_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->fileName, 255);
    return 0;
}

int decode_DeleteFile_Request(per_stream_t *s, DeleteFile_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->fileName = strdup(_buf);
    }
    return 0;
}

int encode_DeleteFile_ResponsePositive(per_stream_t *s, const DeleteFile_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_DeleteFile_ResponsePositive(per_stream_t *s, DeleteFile_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_DeleteFile_ResponseNegative(per_stream_t *s, const DeleteFile_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_DeleteFile_ResponseNegative(per_stream_t *s, DeleteFile_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_FileAttribute(per_stream_t *s, const FileAttribute *v) {
    per_encode_visible_string(s, v->fileName, 255);
    per_encode_constrained_int(s, v->fileSize, 0, 4294967295);
    if (v->_has_lastModified) {
        per_encode_boolean(s, 1); /* present */
encode_UtcTime(s, &v->lastModified);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_FileAttribute(per_stream_t *s, FileAttribute *v) {
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->fileName = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->fileSize = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_lastModified = _b ? 1 : 0;
    }
    if (v->_has_lastModified) {
decode_UtcTime(s, &v->lastModified);
    }
    return 0;
}

int encode_GetFileDirectory_Request(per_stream_t *s, const GetFileDirectory_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->directoryName, 255);
    return 0;
}

int decode_GetFileDirectory_Request(per_stream_t *s, GetFileDirectory_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->directoryName = strdup(_buf);
    }
    return 0;
}

int encode_GetFileDirectory_ResponsePositive(per_stream_t *s, const GetFileDirectory_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->files_count);
    for (int _i = 0; _i < v->files_count; _i++) {
        encode_FileAttribute(s, &v->files[_i]);
    }
    return 0;
}

int decode_GetFileDirectory_ResponsePositive(per_stream_t *s, GetFileDirectory_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->files_count = (int)_count;
        v->files = calloc(_count, sizeof(*v->files));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_FileAttribute(s, &v->files[_i]);
        }
    }
    return 0;
}

int encode_GetFileDirectory_ResponseNegative(per_stream_t *s, const GetFileDirectory_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetFileDirectory_ResponseNegative(per_stream_t *s, GetFileDirectory_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetFileAttributeValues_Request(per_stream_t *s, const GetFileAttributeValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->fileName, 255);
    return 0;
}

int decode_GetFileAttributeValues_Request(per_stream_t *s, GetFileAttributeValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->fileName = strdup(_buf);
    }
    return 0;
}

int encode_GetFileAttributeValues_ResponsePositive(per_stream_t *s, const GetFileAttributeValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    encode_FileAttribute(s, &v->fileAttributes);
    return 0;
}

int decode_GetFileAttributeValues_ResponsePositive(per_stream_t *s, GetFileAttributeValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    decode_FileAttribute(s, &v->fileAttributes);
    return 0;
}

int encode_GetFileAttributeValues_ResponseNegative(per_stream_t *s, const GetFileAttributeValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetFileAttributeValues_ResponseNegative(per_stream_t *s, GetFileAttributeValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetGoReference_Request(per_stream_t *s, const GetGoReference_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->gocbRef, 129);
    return 0;
}

int decode_GetGoReference_Request(per_stream_t *s, GetGoReference_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->gocbRef = strdup(_buf);
    }
    return 0;
}

int encode_GetGoReference_ResponsePositive(per_stream_t *s, const GetGoReference_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->goReference, 129);
    return 0;
}

int decode_GetGoReference_ResponsePositive(per_stream_t *s, GetGoReference_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->goReference = strdup(_buf);
    }
    return 0;
}

int encode_GetGoReference_ResponseNegative(per_stream_t *s, const GetGoReference_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetGoReference_ResponseNegative(per_stream_t *s, GetGoReference_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetGoCBValues_Request(per_stream_t *s, const GetGoCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->gocbRef, 129);
    return 0;
}

int decode_GetGoCBValues_Request(per_stream_t *s, GetGoCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->gocbRef = strdup(_buf);
    }
    return 0;
}

int encode_GetGoCBValues_ResponsePositive(per_stream_t *s, const GetGoCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->appID, 255);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_boolean(s, v->ndcom);
    per_encode_octet_string_fixed(s, v->dstAddress, 6);
    per_encode_constrained_int(s, v->minTime, 0, 4294967295);
    per_encode_constrained_int(s, v->maxTime, 0, 4294967295);
    per_encode_boolean(s, v->fixedOffs);
    if (v->_has_goID) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->goID, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_GetGoCBValues_ResponsePositive(per_stream_t *s, GetGoCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->appID = strdup(_buf);
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->ndcom = _b ? 1 : 0;
    }
        {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->minTime = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->maxTime = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->fixedOffs = _b ? 1 : 0;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_goID = _b ? 1 : 0;
    }
    if (v->_has_goID) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->goID = strdup(_buf);
        }
    }
    return 0;
}

int encode_GetGoCBValues_ResponseNegative(per_stream_t *s, const GetGoCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetGoCBValues_ResponseNegative(per_stream_t *s, GetGoCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_SetGoCBValues_Entry(per_stream_t *s, const SetGoCBValues_Entry *v) {
    per_encode_visible_string(s, v->reference, 129);
    if (v->_has_appID) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->appID, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_datSet) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->datSet, 129);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_confRev) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_ndcom) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->ndcom);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_minTime) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->minTime, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_maxTime) {
        per_encode_boolean(s, 1); /* present */
per_encode_constrained_int(s, v->maxTime, 0, 4294967295);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_fixedOffs) {
        per_encode_boolean(s, 1); /* present */
per_encode_boolean(s, v->fixedOffs);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    if (v->_has_goID) {
        per_encode_boolean(s, 1); /* present */
per_encode_visible_string(s, v->goID, 255);
    } else {
        per_encode_boolean(s, 0); /* absent */
    }
    return 0;
}

int decode_SetGoCBValues_Entry(per_stream_t *s, SetGoCBValues_Entry *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->reference = strdup(_buf);
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_appID = _b ? 1 : 0;
    }
    if (v->_has_appID) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->appID = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_datSet = _b ? 1 : 0;
    }
    if (v->_has_datSet) {
{
            char _buf[130];
            per_decode_visible_string(s, _buf, 129);
            v->datSet = strdup(_buf);
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_confRev = _b ? 1 : 0;
    }
    if (v->_has_confRev) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->confRev = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_ndcom = _b ? 1 : 0;
    }
    if (v->_has_ndcom) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->ndcom = _b ? 1 : 0;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_minTime = _b ? 1 : 0;
    }
    if (v->_has_minTime) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->minTime = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_maxTime = _b ? 1 : 0;
    }
    if (v->_has_maxTime) {
{
            int64_t _tmp;
            per_decode_constrained_int(s, &_tmp, 0, 4294967295);
            v->maxTime = (int)_tmp;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_fixedOffs = _b ? 1 : 0;
    }
    if (v->_has_fixedOffs) {
{
            bool _b;
            per_decode_boolean(s, &_b);
            v->fixedOffs = _b ? 1 : 0;
        }
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->_has_goID = _b ? 1 : 0;
    }
    if (v->_has_goID) {
{
            char _buf[256];
            per_decode_visible_string(s, _buf, 255);
            v->goID = strdup(_buf);
        }
    }
    return 0;
}

int encode_SetGoCBValues_Request(per_stream_t *s, const SetGoCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_length(s, v->gocbValues_count);
    for (int _i = 0; _i < v->gocbValues_count; _i++) {
        encode_SetGoCBValues_Entry(s, &v->gocbValues[_i]);
    }
    return 0;
}

int decode_SetGoCBValues_Request(per_stream_t *s, SetGoCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->gocbValues_count = (int)_count;
        v->gocbValues = calloc(_count, sizeof(*v->gocbValues));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_SetGoCBValues_Entry(s, &v->gocbValues[_i]);
        }
    }
    return 0;
}

int encode_SetGoCBValues_ResponsePositive(per_stream_t *s, const SetGoCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_SetGoCBValues_ResponsePositive(per_stream_t *s, SetGoCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_SetGoCBValues_ResponseNegative(per_stream_t *s, const SetGoCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_SetGoCBValues_ResponseNegative(per_stream_t *s, SetGoCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GoosePdu(per_stream_t *s, const GoosePdu *v) {
    per_encode_visible_string(s, v->gocbRef, 129);
    per_encode_constrained_int(s, v->timeAllowedtoLive, 0, 4294967295);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_visible_string(s, v->goID, 255);
    encode_UtcTime(s, &v->t);
    per_encode_constrained_int(s, v->stNum, 0, 4294967295);
    per_encode_constrained_int(s, v->sqNum, 0, 4294967295);
    per_encode_boolean(s, v->simulation);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_boolean(s, v->ndcom);
    per_encode_constrained_int(s, v->numDatSetEntries, 0, 4294967295);
    per_encode_length(s, v->allData_count);
    for (int _i = 0; _i < v->allData_count; _i++) {
        encode_Data(s, &v->allData[_i]);
    }
    return 0;
}

int decode_GoosePdu(per_stream_t *s, GoosePdu *v) {
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->gocbRef = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->timeAllowedtoLive = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->goID = strdup(_buf);
    }
    decode_UtcTime(s, &v->t);
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->stNum = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->sqNum = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->simulation = _b ? 1 : 0;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->ndcom = _b ? 1 : 0;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->numDatSetEntries = (int)_tmp;
    }
    {
        uint32_t _count;
        per_decode_length(s, &_count);
        v->allData_count = (int)_count;
        v->allData = calloc(_count, sizeof(*v->allData));
        for (uint32_t _i = 0; _i < _count; _i++) {
            decode_Data(s, &v->allData[_i]);
        }
    }
    return 0;
}

int encode_SendGooseMessage(per_stream_t *s, const SendGooseMessage *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    encode_GoosePdu(s, &v->goosePdu);
    return 0;
}

int decode_SendGooseMessage(per_stream_t *s, SendGooseMessage *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    decode_GoosePdu(s, &v->goosePdu);
    return 0;
}

int encode_GetGooseElementNumber_Request(per_stream_t *s, const GetGooseElementNumber_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->gocbRef, 129);
    encode_GoosePdu(s, &v->goosePdu);
    return 0;
}

int decode_GetGooseElementNumber_Request(per_stream_t *s, GetGooseElementNumber_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->gocbRef = strdup(_buf);
    }
    decode_GoosePdu(s, &v->goosePdu);
    return 0;
}

int encode_GetGooseElementNumber_ResponsePositive(per_stream_t *s, const GetGooseElementNumber_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->elementNum, 0, 4294967295);
    return 0;
}

int decode_GetGooseElementNumber_ResponsePositive(per_stream_t *s, GetGooseElementNumber_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->elementNum = (int)_tmp;
    }
    return 0;
}

int encode_GetGooseElementNumber_ResponseNegative(per_stream_t *s, const GetGooseElementNumber_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetGooseElementNumber_ResponseNegative(per_stream_t *s, GetGooseElementNumber_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_GetMSVCBValues_Request(per_stream_t *s, const GetMSVCBValues_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->msvcbRef, 129);
    return 0;
}

int decode_GetMSVCBValues_Request(per_stream_t *s, GetMSVCBValues_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->msvcbRef = strdup(_buf);
    }
    return 0;
}

int encode_GetMSVCBValues_ResponsePositive(per_stream_t *s, const GetMSVCBValues_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_visible_string(s, v->svID, 255);
    per_encode_visible_string(s, v->datSet, 129);
    per_encode_constrained_int(s, v->confRev, 0, 4294967295);
    per_encode_constrained_int(s, v->smpRate, 0, 4294967295);
    per_encode_constrained_int(s, v->nofASDU, 0, 255);
    /* TODO: encode optFlds */
    /* TODO: encode smpMod */
    per_encode_octet_string_fixed(s, v->dstAddress, 6);
    per_encode_boolean(s, v->svEna);
    return 0;
}

int decode_GetMSVCBValues_ResponsePositive(per_stream_t *s, GetMSVCBValues_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        char _buf[256];
        per_decode_visible_string(s, _buf, 255);
        v->svID = strdup(_buf);
    }
    {
        char _buf[130];
        per_decode_visible_string(s, _buf, 129);
        v->datSet = strdup(_buf);
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->confRev = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 4294967295);
        v->smpRate = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 255);
        v->nofASDU = (int)_tmp;
    }
    /* TODO: decode optFlds */
    /* TODO: decode smpMod */
        {
        bool _b;
        per_decode_boolean(s, &_b);
        v->svEna = _b ? 1 : 0;
    }
    return 0;
}

int encode_GetMSVCBValues_ResponseNegative(per_stream_t *s, const GetMSVCBValues_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_GetMSVCBValues_ResponseNegative(per_stream_t *s, GetMSVCBValues_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

int encode_Test_Request(per_stream_t *s, const Test_Request *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    return 0;
}

int decode_Test_Request(per_stream_t *s, Test_Request *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    return 0;
}

int encode_Test_ResponsePositive(per_stream_t *s, const Test_ResponsePositive *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_boolean(s, v->testResult);
    return 0;
}

int decode_Test_ResponsePositive(per_stream_t *s, Test_ResponsePositive *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        bool _b;
        per_decode_boolean(s, &_b);
        v->testResult = _b ? 1 : 0;
    }
    return 0;
}

int encode_Test_ResponseNegative(per_stream_t *s, const Test_ResponseNegative *v) {
    per_encode_constrained_int(s, v->reqId, 0, 65535);
    per_encode_constrained_int(s, v->serviceError, 0, 16);
    return 0;
}

int decode_Test_ResponseNegative(per_stream_t *s, Test_ResponseNegative *v) {
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 65535);
        v->reqId = (int)_tmp;
    }
    {
        int64_t _tmp;
        per_decode_constrained_int(s, &_tmp, 0, 16);
        v->serviceError = (int)_tmp;
    }
    return 0;
}

