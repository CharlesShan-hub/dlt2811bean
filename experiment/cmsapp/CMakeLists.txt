cmake_minimum_required(VERSION 3.14)
project(cmsapp VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

set(CMS_DIR  ${CMAKE_CURRENT_SOURCE_DIR}/../ccms)
set(CMS_INC  ${CMS_DIR}/include)
set(CMS_LIB  ${CMS_DIR}/build/libccms.dll.a)
set(CMS_CORE ${CMS_DIR}/build/libccms_core.a)

# ---- Example 1: FFI roundtrip (services: Associate/Release/Abort) ----

add_executable(ffi_roundtrip examples/ffi_roundtrip.c)
target_include_directories(ffi_roundtrip PRIVATE ${CMS_INC})
target_link_libraries(ffi_roundtrip ${CMS_LIB})

# ---- Example 2: APDU roundtrip (APCH + services) ----

add_executable(apdu_roundtrip examples/apdu_roundtrip.c)
target_include_directories(apdu_roundtrip PRIVATE ${CMS_INC})
target_link_libraries(apdu_roundtrip ${CMS_LIB})

# ---- Example 3: SEQUENCE OF roundtrip ----

file(GLOB TEST_GEN_SRC tests/generated/gen_*.c)
if(TEST_GEN_SRC)
    add_library(cmsapp_test_datatypes STATIC ${TEST_GEN_SRC})
    target_include_directories(cmsapp_test_datatypes
        PUBLIC  ${CMAKE_CURRENT_SOURCE_DIR}/tests/generated
        PRIVATE ${CMS_INC}
    )
    target_link_libraries(cmsapp_test_datatypes ${CMS_CORE})

    add_executable(seq_of_roundtrip examples/seq_of_roundtrip.c)
    target_include_directories(seq_of_roundtrip
        PRIVATE
            ${CMS_INC}
            ${CMAKE_CURRENT_SOURCE_DIR}/tests/generated
    )
    target_link_libraries(seq_of_roundtrip
        cmsapp_test_datatypes
        ${CMS_CORE}
    )
endif()
