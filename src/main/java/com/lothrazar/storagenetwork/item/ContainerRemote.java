package com.lothrazar.storagenetwork.item;
import com.lothrazar.storagenetwork.api.data.DimPos;
import com.lothrazar.storagenetwork.block.master.TileMaster;
import com.lothrazar.storagenetwork.gui.ContainerNetworkBase;
import com.lothrazar.storagenetwork.registry.SsnRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;

public class ContainerRemote extends ContainerNetworkBase {

  private final TileMaster master;

  public ContainerRemote(int id, PlayerInventory pInv) {
    super(SsnRegistry.remote, id);

ItemStack remote=    pInv.player.getHeldItem(Hand.MAIN_HAND);

DimPos dp = ItemRemote.getPosStored(remote);
TileEntity te= pInv.player.world.getTileEntity(dp.getBlockPos());

this.master = (TileMaster)te;

  }

  @Override public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  @Override public TileMaster getTileMaster() {


    return master;
  }

  @Override public void slotChanged() {
  }
}
