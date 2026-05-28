cmake_minimum_required(VERSION 3.14)
project(cmsgenerator VERSION 1.0.0 LANGUAGES C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

# --- Generator tool ---
file(GLOB SRC src/*.c)
add_executable(cmsgen ${SRC})
target_include_directories(cmsgen PRIVATE include)

# --- Datatypes library (only if generated files exist) ---
file(GLOB GEN_SRC generated/gen_*.c)
if(GEN_SRC)
    add_library(cmsper_datatypes STATIC ${GEN_SRC})
    target_include_directories(cmsper_datatypes
        PUBLIC  $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/generated>
        PRIVATE ${CMAKE_SOURCE_DIR}/../cmsper/include
    )
    target_link_libraries(cmsper_datatypes
        PUBLIC  ${CMAKE_SOURCE_DIR}/../cmsper/build/libcmsper.a
    )
endif()
