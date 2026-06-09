cmake_minimum_required(VERSION 3.14)
project(ccms2 VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

set(CMS2_SRC
    src/per/cms_stream.c
    src/per/cms_integer.c
    src/per/cms_string.c
    src/per/cms_choice.c

    # scalar types (leaf, inline value)
    src/data/scalar/cms_boolean.c
    src/data/scalar/cms_int8.c
    src/data/scalar/cms_int8u.c
    src/data/scalar/cms_int16.c
    src/data/scalar/cms_int16u.c
    src/data/scalar/cms_int24u.c
    src/data/scalar/cms_int32.c
    src/data/scalar/cms_int32u.c
    src/data/scalar/cms_int64.c
    src/data/scalar/cms_int64u.c
    src/data/scalar/cms_float32.c
    src/data/scalar/cms_float64.c

    # time types
    src/data/time/cms_time_quality.c
    src/data/time/cms_utc_time.c
    src/data/time/cms_binary_time.c

    # string types
    src/data/string/cms_octet_string.c
    src/data/string/cms_visible_string.c
    src/data/string/cms_utf8_string.c
    src/data/string/cms_bit_string.c

    # common types
    src/data/common/cms_quality.c
    src/data/common/cms_object_name.c
    src/data/common/cms_object_reference.c
    src/data/common/cms_sub_reference.c
    src/data/common/cms_dbpos.c
    src/data/common/cms_tcmd.c

    # legacy (to be migrated) — currently disabled
    # src/cms2_basic.c
    # src/cms2_associate.c
    # src/svc/cms2_reference_choice.c
    # src/svc/cms2_get_all_cb_values.c
)

add_library(ccms2 STATIC ${CMS2_SRC})
target_include_directories(ccms2 PUBLIC include)

# Tests (old tests disabled — migrated to new cms_ prefix)
# add_executable(test_associate tests/test_associate.c)
# target_link_libraries(test_associate ccms2)
# target_include_directories(test_associate PRIVATE include)

# add_executable(test_get_all_cb tests/test_get_all_cb.c)
# target_link_libraries(test_get_all_cb ccms2)
# target_include_directories(test_get_all_cb PRIVATE include)

# New scalar test
add_executable(test_scalar tests/test_scalar.c)
target_link_libraries(test_scalar ccms2)
target_include_directories(test_scalar PRIVATE include)
