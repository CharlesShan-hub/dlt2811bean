cmake_minimum_required(VERSION 3.14)
project(cmsapp VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

set(CMS_PER_DIR  ${CMAKE_CURRENT_SOURCE_DIR}/../cmsper)
set(CMS_GEN_DIR  ${CMAKE_CURRENT_SOURCE_DIR}/../cmsgenerator)
set(CMS_APP_DIR  ${CMAKE_CURRENT_SOURCE_DIR})

# ---- Example 1: SGCB roundtrip (datatypes only) ----

add_executable(sgcb_roundtrip examples/sgcb_roundtrip.c)

target_include_directories(sgcb_roundtrip
    PRIVATE
        ${CMS_PER_DIR}/include
        ${CMS_GEN_DIR}/generated
)

target_link_libraries(sgcb_roundtrip
    ${CMS_PER_DIR}/build/libcmsper.a
    ${CMS_GEN_DIR}/build/libcmsper_datatypes.a
)

# ---- Example 2: SEQUENCE OF roundtrip ----

file(GLOB TEST_GEN_SRC tests/generated/gen_*.c)
if(TEST_GEN_SRC)
    add_library(cmsapp_test_datatypes STATIC ${TEST_GEN_SRC})
    target_include_directories(cmsapp_test_datatypes
        PUBLIC  ${CMS_APP_DIR}/tests/generated
        PRIVATE ${CMS_PER_DIR}/include
    )
    target_link_libraries(cmsapp_test_datatypes
        ${CMS_PER_DIR}/build/libcmsper.a
    )

    add_executable(seq_of_roundtrip examples/seq_of_roundtrip.c)
    target_include_directories(seq_of_roundtrip
        PRIVATE
            ${CMS_PER_DIR}/include
            ${CMS_APP_DIR}/tests/generated
    )
    target_link_libraries(seq_of_roundtrip
        cmsapp_test_datatypes
        ${CMS_PER_DIR}/build/libcmsper.a
    )
endif()

# ---- Example 3: Service roundtrip (Associate, GetDataValues, Report) ----

add_executable(service_roundtrip examples/service_roundtrip.c)

target_include_directories(service_roundtrip
    PRIVATE
        ${CMS_PER_DIR}/include
        ${CMS_GEN_DIR}/generated
)

target_link_libraries(service_roundtrip
    ${CMS_PER_DIR}/build/libcmsper.a
    ${CMS_GEN_DIR}/build/libcmsper_datatypes.a
)
