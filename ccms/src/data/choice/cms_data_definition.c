#include "data/choice/cms_data_definition.h"
#include "per/cms_choice.h"
#include "data/basic/cms_integer.h"
#include "data/fc/cms_functional_constraint.h"

/* ---- internal stream version ---- */

int cms_data_definition_encode_stream(per_stream_t *s, const cms_data_definition_t *def)
{
    int rc = per_encode_choice(s, (uint32_t)def->choice);
    if (rc) return rc;

    switch (def->choice) {
    case 0:
        rc = cms_service_error_encode_stream(s, &def->value.error);
        break;

    case 1:
        rc = cms_int32_encode_stream(s, &def->value.array.numberOfElement);
        if (rc) return rc;
        if (def->value.array.elementType){
            rc = cms_data_definition_encode_stream(s, def->value.array.elementType);
            if (rc) return rc;
        }
        break;

    case 2:
        per_encode_length(s, (uint32_t)def->value.structure.count.value);
        for (int32_t i = 0; i < def->value.structure.count.value; i++) {
            const cms_data_definition_member_t *m = &def->value.structure.elements[i];
            rc = cms_object_name_encode_stream(s, &m->name);
            if (rc) return rc;
            rc = cms_boolean_encode_stream(s, &m->has_fc);
            if (rc) return rc;
            if (m->has_fc.value){
                rc = cms_functional_constraint_encode_stream(s, &m->fc);
                if (rc) return rc;
            }
            if (m->type){
                rc = cms_data_definition_encode_stream(s, m->type);
                if (rc) return rc;
            }
        }
        break;

    case 3: case 4: case 5: case 6: case 7:
    case 8: case 9: case 10: case 11: case 12: case 13:
    case 18: case 19: case 20: case 21: case 22: case 23:
        break;

    case 14: case 15: case 16: case 17:
        rc = per_encode_constrained_int(s, def->value.string_length.value, 0, 65535);
        break;
    }
    return rc;
}

int cms_data_definition_decode_stream(per_stream_t *s, cms_data_definition_t *def)
{
    uint32_t idx;
    int rc = per_decode_choice(s, &idx);
    if (rc) return rc;
    def->choice = (int32_t)idx;

    switch (def->choice) {
    case 0:
        rc = cms_service_error_decode_stream(s, &def->value.error);
        break;

    case 1:
        rc = cms_int32_decode_stream(s, &def->value.array.numberOfElement);
        if (rc) return rc;
        def->value.array.elementType = (cms_data_definition_t *)calloc(1, sizeof(cms_data_definition_t));
        if (!def->value.array.elementType) return CMS_ERR;
        rc = cms_data_definition_decode_stream(s, def->value.array.elementType);
        break;

    case 2: {
        uint32_t count;
        per_decode_length(s, &count);
        def->value.structure.count.value = (int32_t)count;
        def->value.structure.elements = NULL;
        if (count) {
            def->value.structure.elements = (cms_data_definition_member_t *)calloc(count, sizeof(cms_data_definition_member_t));
            if (!def->value.structure.elements) return CMS_ERR;
            for (uint32_t i = 0; i < count; i++) {
                cms_data_definition_member_t *m = &def->value.structure.elements[i];
                rc = cms_object_name_decode_stream(s, &m->name);
                if (rc) return rc;
                rc = cms_boolean_decode_stream(s, &m->has_fc);
                if (rc) return rc;
                if (m->has_fc.value){
                    rc = cms_functional_constraint_decode_stream(s, &m->fc);
                    if (rc) return rc;
                }
                m->type = (cms_data_definition_t *)calloc(1, sizeof(cms_data_definition_t));
                if (!m->type) return CMS_ERR;
                rc = cms_data_definition_decode_stream(s, m->type);
                if (rc) return rc;
            }
        }
        break;
    }

    case 3: case 4: case 5: case 6: case 7:
    case 8: case 9: case 10: case 11: case 12: case 13:
    case 18: case 19: case 20: case 21: case 22: case 23:
        break;

    case 14: case 15: case 16: case 17: {
        int64_t t;
        rc = per_decode_constrained_int(s, &t, 0, 65535);
        if (rc) return rc;
        def->value.string_length.value = (int32_t)t;
        break;
    }
    }
    return rc;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_data_definition_encode(const cms_data_definition_t *def, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    int rc = cms_data_definition_encode_stream(&w, def);
    *out_len = (int)per_stream_bytes_written(&w);
    return rc;
}

CMS_EXPORT int cms_data_definition_decode(cms_data_definition_t *def, const uint8_t *in_buf, int in_len)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    return cms_data_definition_decode_stream(&r, def);
}

/* ---- free ---- */

static void free_members(cms_data_definition_member_t *members, int32_t count)
{
    for (int32_t i = 0; i < count; i++) {
        if (members[i].type) {
            cms_data_definition_free(members[i].type);
            free(members[i].type);
        }
    }
}

CMS_EXPORT void cms_data_definition_free(cms_data_definition_t *def)
{
    if (!def) return;
    if (def->choice == 1) {
        if (def->value.array.elementType) {
            cms_data_definition_free(def->value.array.elementType);
            free(def->value.array.elementType);
            def->value.array.elementType = NULL;
        }
    } else if (def->choice == 2) {
        free_members(def->value.structure.elements, def->value.structure.count.value);
        free(def->value.structure.elements);
        def->value.structure.elements = NULL;
        def->value.structure.count.value = 0;
    }
}
