package vktec.edinv;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

public class InventoryView implements Inventory {
	private final PlayerInventory playerInv;
	private final DefaultedList<ItemStack>[] stackLists;
	private final int size;

	public InventoryView(PlayerInventory playerInv, DefaultedList<ItemStack>... stackLists) {
		this.playerInv = playerInv;
		this.stackLists = stackLists;
		int size = 0;
		for (DefaultedList<ItemStack> stackList : this.stackLists) {
			size += stackList.size();
		}
		this.size = size;
	}

	public int getInvSize() {
		return this.size;
	}

	public boolean isInvEmpty() {
		for (DefaultedList<ItemStack> stackList : this.stackLists) {
			for (ItemStack stack : stackList) {
				if (!stack.isEmpty()) return false;
			}
		}
		return true;
	}

	public ItemStack getInvStack(int slot) {
		for (DefaultedList<ItemStack> stackList : this.stackLists) {
			if (slot < stackList.size()) {
				return stackList.get(slot);
			}
			slot -= stackList.size();
		}
		return ItemStack.EMPTY;
	}

	public ItemStack takeInvStack(int slot, int count) {
		for (DefaultedList<ItemStack> stackList : this.stackLists) {
			if (slot < stackList.size()) {
				if (stackList.get(slot).isEmpty()) return ItemStack.EMPTY;
				return Inventories.splitStack(stackList, slot, count);
			}
			slot -= stackList.size();
		}
		return ItemStack.EMPTY;
	}

	public ItemStack removeInvStack(int slot) {
		for (DefaultedList<ItemStack> stackList : this.stackLists) {
			if (slot < stackList.size()) {
				ItemStack stack = stackList.get(slot);
				if (stack.isEmpty()) return ItemStack.EMPTY;
				stackList.set(slot, ItemStack.EMPTY);
				return stack;
			}
			slot -= stackList.size();
		}
		return ItemStack.EMPTY;
	}

	public void setInvStack(int slot, ItemStack stack) {
		for (DefaultedList<ItemStack> stackList : this.stackLists) {
			if (slot < stackList.size()) {
				stackList.set(slot, stack);
				return;
			}
			slot -= stackList.size();
		}
	}

	public void markDirty() {
		this.playerInv.markDirty();
	}

	public boolean canPlayerUseInv(PlayerEntity arg) {
		return !this.playerInv.player.removed;
	}

	public void clear() {
		for (DefaultedList<ItemStack> stackList : this.stackLists) {
			stackList.clear();
		}
	}
}
