package edivad.solargeneration.tools;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class ItemNBTHelper {

	// SETTERS ///////////////////////////////////////////////////////////////////
	public static NBTTagCompound getCompound(ItemStack stack)
	{
		if(stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());
		return stack.getTagCompound();
	}

	public static ItemStack setByte(ItemStack stack, String tag, byte b)
	{
		getCompound(stack).setByte(tag, b);
		return stack;
	}

	public static ItemStack setBoolean(ItemStack stack, String tag, boolean b)
	{
		getCompound(stack).setBoolean(tag, b);
		return stack;
	}

	public static ItemStack setShort(ItemStack stack, String tag, short s)
	{
		getCompound(stack).setShort(tag, s);
		return stack;
	}

	public static ItemStack setInteger(ItemStack stack, String tag, int i)
	{
		getCompound(stack).setInteger(tag, i);
		return stack;
	}

	public static ItemStack setLong(ItemStack stack, String tag, long i)
	{
		getCompound(stack).setLong(tag, i);
		return stack;
	}

	public static ItemStack setFloat(ItemStack stack, String tag, float f)
	{
		getCompound(stack).setFloat(tag, f);
		return stack;
	}

	public static ItemStack setDouble(ItemStack stack, String tag, double d)
	{
		getCompound(stack).setDouble(tag, d);
		return stack;
	}

	public static ItemStack setString(ItemStack stack, String tag, String s)
	{
		getCompound(stack).setString(tag, s);
		return stack;
	}

	// GETTERS ///////////////////////////////////////////////////////////////////

	public static boolean verifyExistance(ItemStack stack, String tag)
	{
		NBTTagCompound compound = stack.getTagCompound();
		if(compound == null)
			return false;
		return stack.getTagCompound().hasKey(tag);
	}

	public static byte getByte(ItemStack stack, String tag, byte defaultExpected)
	{
		return verifyExistance(stack, tag) ? stack.getTagCompound().getByte(tag) : defaultExpected;
	}

	public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected)
	{
		return verifyExistance(stack, tag) ? stack.getTagCompound().getBoolean(tag) : defaultExpected;
	}

	public static short getShort(ItemStack stack, String tag, short defaultExpected)
	{
		return verifyExistance(stack, tag) ? stack.getTagCompound().getShort(tag) : defaultExpected;
	}

	public static int getInteger(ItemStack stack, String tag, int defaultExpected)
	{
		return verifyExistance(stack, tag) ? stack.getTagCompound().getInteger(tag) : defaultExpected;
	}

	public static long getLong(ItemStack stack, String tag, long defaultExpected)
	{
		return verifyExistance(stack, tag) ? stack.getTagCompound().getLong(tag) : defaultExpected;
	}

	public static float getFloat(ItemStack stack, String tag, float defaultExpected)
	{
		return verifyExistance(stack, tag) ? stack.getTagCompound().getFloat(tag) : defaultExpected;
	}

	public static double getDouble(ItemStack stack, String tag, double defaultExpected)
	{
		return verifyExistance(stack, tag) ? stack.getTagCompound().getDouble(tag) : defaultExpected;
	}

	public static String getString(ItemStack stack, String tag, String defaultExpected)
	{
		return verifyExistance(stack, tag) ? stack.getTagCompound().getString(tag) : defaultExpected;
	}
}
