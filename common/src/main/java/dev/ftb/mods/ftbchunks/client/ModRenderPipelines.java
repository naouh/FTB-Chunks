package dev.ftb.mods.ftbchunks.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;

public class ModRenderPipelines {
    // 26.3: pipelines are built from bind group layouts, vertex bindings and a primitive topology
    // (renderpearl API). This mirrors vanilla's GUI_TEXTURED_SNIPPET, with a second sampler for the mask.
    // GUI pipelines no longer carry a depth/stencil state (previously ALWAYS_PASS without depth writes).
    public static final RenderPipeline MINIMAP_MASKED = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
            .withBindGroupLayout(BindGroupLayouts.PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
            .withVertexShader(FTBChunksAPI.id("core/minimap_mask"))
            .withFragmentShader(FTBChunksAPI.id("core/minimap_mask"))
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER1)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withLocation(FTBChunksAPI.id("pipeline/minimap_mask"))
            .build();
}
