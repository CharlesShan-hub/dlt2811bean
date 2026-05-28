#include "gen_dlt2811b_datatypes.h"
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

