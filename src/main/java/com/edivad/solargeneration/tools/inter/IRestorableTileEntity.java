package com.edivad.solargeneration.tools.inter;

import net.minecraft.nbt.NBTTagCompound;

public interface IRestorableTileEntity {

	void readRestorableFromNBT(NBTTagCompound compound);

	void writeRestorableToNBT(NBTTagCompound compound);
}
