package mrriegel.storagenetwork.gui.cable;

import java.util.HashMap;
import java.util.Map;

import mrriegel.storagenetwork.helper.StackWrapper;
import mrriegel.storagenetwork.tile.TileKabel;
import mrriegel.storagenetwork.tile.TileMaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class ContainerCable extends Container {
	// InventoryFilter inv;
	InventoryPlayer playerInv;
	public TileKabel tile;
	private Map<Integer, StackWrapper> filter;

	public ContainerCable(TileKabel tile, InventoryPlayer playerInv) {
		this.playerInv = playerInv;
		this.tile = tile;
		// inv = new InventoryFilter(tile, 9);
		// id = new InventoryFilter(tile, 1);
		/*
		 * for (int j = 0; j < inv.getSizeInventory(); ++j) {
		 * this.addSlotToContainer(new Slot(inv, j, 8 + j * 18, 26)); }
		 */
		// this.addSlotToContainer(new Slot(id, 0, 8, -18));
		filter = new HashMap<Integer, StackWrapper>();
		NBTTagCompound nbt = new NBTTagCompound();
		tile.writeToNBT(nbt);
		NBTTagList invList = nbt.getTagList("crunchTE", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < invList.tagCount(); i++) {
			NBTTagCompound stackTag = invList.getCompoundTagAt(i);
			int slot = stackTag.getByte("Slot");
			filter.put(slot, StackWrapper.loadStackWrapperFromNBT(stackTag));
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 - 39 + 10 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142 - 39 + 10));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tile != null && tile.getMaster() != null && tile.getWorld().getTileEntity(tile.getMaster()) instanceof TileMaster;
	}

	public void slotChanged() {
		NBTTagCompound nbt = new NBTTagCompound();
		tile.writeToNBT(nbt);
		NBTTagList invList = new NBTTagList();
		for (int i = 0; i < 9; i++) {
			if (filter.get(i) != null) {
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte) i);
				filter.get(i).writeToNBT(stackTag);
				invList.appendTag(stackTag);
			}
		}
		nbt.setTag("crunchTE", invList);
		tile.readFromNBT(nbt);
	}

	// @Override
	// public ItemStack slotClick(int slotId, int clickedButton, int mode,
	// EntityPlayer playerIn) {
	// if (slotId >= 9 || slotId == -999)
	// return super.slotClick(slotId, clickedButton, mode, playerIn);
	// if (playerInv.getItemStack() == null) {
	// getSlot(slotId).putStack(null);
	// slotChanged();
	// } else {
	// ItemStack s = playerInv.getItemStack().copy();
	// if (!in(s)) {
	// s.stackSize = 1;
	// getSlot(slotId).putStack(s);
	// slotChanged();
	// }
	// }
	// return playerInv.getItemStack();
	//
	// }

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		Slot slot = this.inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			if (itemstack1 == null)
				return null;
			for (int i = 0; i < 9; i++) {
				if (filter.get(i) == null && !in(new StackWrapper(itemstack1, 1))) {
					filter.put(i, new StackWrapper(itemstack1.copy(), itemstack1.stackSize));
					slotChanged();
					break;
				}
			}
		}
		return null;
	}

	// int getSlot(ItemStack stack) {
	// if (in(stack))
	// return -1;
	// for (int i = 0; i < 9; i++) {
	// if (!getSlot(i).getHasStack())
	// return i;
	// }
	// return -1;
	// }

	boolean in(StackWrapper stack) {
		for (int i = 0; i < 9; i++) {
			if (filter.get(i) != null && filter.get(i).getStack().isItemEqual(stack.getStack()))
				return true;
		}
		return false;
	}

	public Map<Integer, StackWrapper> getFilter() {
		return filter;
	}

	public void setFilter(Map<Integer, StackWrapper> filter) {
		this.filter = filter;
	}

}