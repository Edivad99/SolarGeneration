package com.edivad.solargeneration;

import org.apache.logging.log4j.Logger;

import com.edivad.solargeneration.proxy.CommonProxy;
import com.edivad.solargeneration.tabs.SolarGenerationTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.MODVERSION, dependencies = "required-after:forge@[14.23.5.2838,);required-after:thermalfoundation@[2.6.3,)", useMetadata = true)
public class Main {
	
    public static final String MODID = "solargeneration";
    public static final String MODNAME = "Solar Generation";
    public static final String MODVERSION = "1.0";
	public static final String CLIENT_PROXY_CLASS = "com.edivad.solargeneration.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.edivad.solargeneration.proxy.CommonProxy";

	@SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
	
	public static final CreativeTabs solarGenerationTab = new SolarGenerationTab("solargeneration_tab");
	
	
    @Mod.Instance
    public static Main instance;
    
    public static Logger logger;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
