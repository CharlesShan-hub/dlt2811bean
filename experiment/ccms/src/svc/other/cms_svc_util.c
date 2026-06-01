#include "svc/other/cms_svc_util.h"
#include <string.h>
#include <stdlib.h>

int cms_write_out(per_stream_t *w, uint8_t *out_buf, int *out_len)
{
    size_t len;
    uint8_t *data = per_stream_detach(w, &len);
    if ((size_t)*out_len < len) {
        free(data);
        return CMS_ERR_BUF_TOO_SMALL;
    }
    memcpy(out_buf, data, len);
    *out_len = (int)len;
    free(data);
    return CMS_OK;
}
