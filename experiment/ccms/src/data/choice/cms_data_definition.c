#include "data/choice/cms_data_definition.h"

/* ---- internal stream version ---- */

int cms_data_definition_encode_stream(per_stream_t *s, const cms_data_definition_t *def)
{
    per_encode_small_non_negative(s, (uint32_t)def->choice);

    switch (def->choice) {
    case 0:
        cms_service_error_encode_stream(s, def->value.error);
        break;

    case 1:
        cms_int32_encode_stream(s, def->value.array.numberOfElement);
        if (def->value.array.elementType)
            cms_data_definition_encode_stream(s, def->value.array.elementType);
        break;

    case 2:
        per_encode_length(s, (uint32_t)def->value.structure.count);
        for (int i = 0; i < def->value.structure.count; i++) {
            cms_object_name_encode_stream(s, def->value.structure.elements[i].name);
            if (def->value.structure.elements[i].has_fc) {
                per_stream_write_bit(s, 1);
                cms_fc_encode_stream(s, def->value.structure.elements[i].fc);
            } else {
                per_stream_write_bit(s, 0);
            }
            if (def->value.structure.elements[i].type)
                cms_data_definition_encode_stream(s, def->value.structure.elements[i].type);
        }
        break;

    case 3: case 4: case 5: case 6: case 7:
    case 8: case 9: case 10: case 11: case 12: case 13:
    case 18: case 19: case 20: case 21: case 22: case 23:
        break;

    case 14: case 15: case 16: case 17:
        per_encode_constrained_int(s, def->value.string_length, 0, 65535);
        break;
    }
    return CMS_OK;
}

int cms_data_definition_decode_stream(per_stream_t *s, cms_data_definition_t *def)
{
    uint32_t _idx;
    per_decode_small_non_negative(s, &_idx);
    def->choice = (int)_idx;

    switch (def->choice) {
    case 0:
        cms_service_error_decode_stream(s, &def->value.error);
        break;

    case 1:
        cms_int32_decode_stream(s, &def->value.array.numberOfElement);
        def->value.array.elementType = (cms_data_definition_t *)malloc(sizeof(cms_data_definition_t));
        if (!def->value.array.elementType) return CMS_ERR;
        memset(def->value.array.elementType, 0, sizeof(cms_data_definition_t));
        cms_data_definition_decode_stream(s, def->value.array.elementType);
        break;

    case 2: {
        uint32_t count;
        per_decode_length(s, &count);
        def->value.structure.count = (int)count;
        def->value.structure.elements = NULL;
        if (count) {
            def->value.structure.elements = (cms_data_definition_member_t *)calloc(count, sizeof(cms_data_definition_member_t));
            if (!def->value.structure.elements) return CMS_ERR;
            for (uint32_t i = 0; i < count; i++) {
                cms_object_name_decode_stream(s, def->value.structure.elements[i].name);
                int has_fc;
                per_stream_read_bit(s, &has_fc);
                if (has_fc) {
                    def->value.structure.elements[i].has_fc = 1;
                    cms_fc_decode_stream(s, def->value.structure.elements[i].fc);
                }
                def->value.structure.elements[i].type = (cms_data_definition_t *)malloc(sizeof(cms_data_definition_t));
                if (!def->value.structure.elements[i].type) return CMS_ERR;
                memset(def->value.structure.elements[i].type, 0, sizeof(cms_data_definition_t));
                cms_data_definition_decode_stream(s, def->value.structure.elements[i].type);
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
        per_decode_constrained_int(s, &t, 0, 65535);
        def->value.string_length = (int)t;
        break;
    }
    }
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_data_definition_encode(const cms_data_definition_t *def, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    cms_data_definition_encode_stream(&w, def);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_data_definition_decode(const uint8_t *in_buf, int in_len, cms_data_definition_t *def)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    return cms_data_definition_decode_stream(&r, def);
}

/* ---- free ---- */

static void free_members(cms_data_definition_member_t *members, int count)
{
    for (int i = 0; i < count; i++) {
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
        free_members(def->value.structure.elements, def->value.structure.count);
        free(def->value.structure.elements);
        def->value.structure.elements = NULL;
        def->value.structure.count = 0;
    }
}
