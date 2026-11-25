package dev.ftb.mods.ftbchunks.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbchunks.net.AddWaypointPacket;
import dev.ftb.mods.ftblibrary.platform.network.Server2PlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TeamColorArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.scores.TeamColor;

public class WaypointCommand {
    static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("waypoint")
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .then(Commands.argument("position", BlockPosArgument.blockPos())
                                        .executes(context -> addWaypoint(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "name"),
                                                BlockPosArgument.getBlockPos(context, "position"))
                                        )
                                        .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                .executes(context -> addWaypoint(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "name"),
                                                        DimensionArgument.getDimension(context, "dimension"),
                                                        BlockPosArgument.getBlockPos(context, "position"))
                                                )
                                                .then(Commands.argument("color", TeamColorArgument.teamColor())
                                                        .executes(context -> addWaypoint(
                                                                context.getSource(),
                                                                StringArgumentType.getString(context, "name"),
                                                                DimensionArgument.getDimension(context, "dimension"),
                                                                BlockPosArgument.getBlockPos(context, "position"),
                                                                TeamColorArgument.getTeamColor(context, "color"))
                                                        )
                                                        .then(Commands.argument("gui", BoolArgumentType.bool())
                                                                .executes(context -> addWaypoint(
                                                                        context.getSource(),
                                                                        StringArgumentType.getString(context, "name"),
                                                                        DimensionArgument.getDimension(context, "dimension"),
                                                                        BlockPosArgument.getBlockPos(context, "position"),
                                                                        TeamColorArgument.getTeamColor(context, "color"),
                                                                        BoolArgumentType.getBool(context, "gui"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    static int addWaypoint(CommandSourceStack source, String name, ServerLevel level, BlockPos position, TeamColor color, boolean useGui) throws CommandSyntaxException {
        // 26.3: ColorArgument (ChatFormatting) was replaced by TeamColorArgument; every TeamColor has an RGB value
        Server2PlayNetworking.send(source.getPlayerOrException(), new AddWaypointPacket(name, new GlobalPos(level.dimension(), position), color.rgb(), useGui));
        source.sendSuccess(() -> Component.translatable("ftbchunks.command.waypoint_added", name), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int addWaypoint(CommandSourceStack source, String name, ServerLevel level, BlockPos position, TeamColor color) throws CommandSyntaxException {
        return addWaypoint(source, name, level, position, color, false);
    }

    private static int addWaypoint(CommandSourceStack source, String name, BlockPos position) throws CommandSyntaxException {
        int idx = source.getPlayerOrException().getRandom().nextInt(TeamColor.values().length);
        return addWaypoint(source, name, source.getLevel(), position, TeamColor.values()[idx], false);
    }

    private static int addWaypoint(CommandSourceStack source, String name, ServerLevel level, BlockPos position) throws CommandSyntaxException {
        int idx = source.getPlayerOrException().getRandom().nextInt(TeamColor.values().length);
        return addWaypoint(source, name, level, position, TeamColor.values()[idx], false);
    }
}
