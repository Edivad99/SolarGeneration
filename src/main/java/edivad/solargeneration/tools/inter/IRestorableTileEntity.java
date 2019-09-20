package edivad.solargeneration.tools.inter;

import net.minecraft.nbt.CompoundNBT;

public interface IRestorableTileEntity {

	void readRestorableFromNBT(CompoundNBT compound);

	void writeRestorableToNBT(CompoundNBT compound);
}
