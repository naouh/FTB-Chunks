#version 330
#extension GL_ARB_separate_shader_objects : require

// 26.3: shaders are compiled with shaderc (#include instead of #moj_import, explicit in/out locations)
#include <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

layout(location = 0) in vec2 texCoord0;
layout(location = 1) in vec4 vertexColor;

layout(location = 0) out vec4 fragColor;

void main() {
    vec2 maskUV = vertexColor.rg;
    float maskA = texture(Sampler1, maskUV).a;
    vec4 mapColor = texture(Sampler0, texCoord0);
    float alpha = mapColor.a * vertexColor.a * maskA;
    if (alpha <= 0.0) {
        discard;
    }
    fragColor = vec4(mapColor.rgb, alpha) * ColorModulator;
}
