package vktec.edinv.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.container.ContainerType;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.HopperContainer;
import net.minecraft.container.SimpleNamedContainerFactory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import vktec.edinv.InventoryView;

public class InvCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
			CommandManager.literal("inv")
				.requires(player -> player.hasPermissionLevel(2))
				.then(CommandManager.argument("target", EntityArgumentType.player())
					.then(CommandManager.literal("echest")
						.executes(InvCommand::editEnderChest))
					.then(CommandManager.literal("equipment")
						.executes(InvCommand::editEquipment))
					.executes(InvCommand::editInventory))
		);
	}

	public static int editEnderChest(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ServerCommandSource source = ctx.getSource();
		ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx, "target");
		EnderChestInventory echestInv = target.getEnderChestInventory();

		source.getPlayer().openContainer(new SimpleNamedContainerFactory(
			(syncId, playerInv, player) -> GenericContainer.createGeneric9x3(syncId, playerInv, echestInv),
			target.getName().append("'s Ender Chest")));
		return 1;
	}

	public static int editEquipment(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ServerCommandSource source = ctx.getSource();
		ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx, "target");
		try {
		InventoryView targetInv = new InventoryView(target.inventory, target.inventory.armor, target.inventory.offHand);

		source.getPlayer().openContainer(new SimpleNamedContainerFactory(
			(syncId, playerInv, player) -> new HopperContainer(syncId, playerInv, targetInv),
			target.getName().append("'s Inventory")));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return 1;
	}

	public static int editInventory(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		ServerCommandSource source = ctx.getSource();
		ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx, "target");
		InventoryView targetInv = new InventoryView(target.inventory, target.inventory.main);

		source.getPlayer().openContainer(new SimpleNamedContainerFactory(
			(syncId, playerInv, player) -> new GenericContainer(ContainerType.GENERIC_9X4, syncId, playerInv, targetInv, 4),
			target.getName().append("'s Inventory")));
		return 1;
	}
}
