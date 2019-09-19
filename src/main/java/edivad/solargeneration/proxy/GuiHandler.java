package edivad.solargeneration.proxy;

import javax.annotation.Nullable;

import edivad.solargeneration.Main;
import edivad.solargeneration.tools.inter.IGuiTile;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages.OpenContainer;

public class GuiHandler {

	@Nullable
	@OnlyIn(Dist.CLIENT)
	public static GuiScreen getClientGuiElement(OpenContainer container)
	{
		PacketBuffer buf = container.getAdditionalData();
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();

		World world = Main.proxy.getClientWorld();
		EntityPlayer player = Main.proxy.getClientPlayer();

		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof IGuiTile)
		{
			return ((IGuiTile) te).createGui(player);
		}
		return null;
	}
}
