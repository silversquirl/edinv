package vktec.edinv.mixin;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import vktec.edinv.BasicInventoryDuck;

@Mixin(BasicInventory.class)
abstract public class BasicInventoryMixin implements BasicInventoryDuck {
	@Shadow @Final @Mutable private DefaultedList<ItemStack> stackList;

	public void setStackList(DefaultedList<ItemStack> stackList) {
		this.stackList = stackList;
	}
}
