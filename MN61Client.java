package com.mn61.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "mn61client", name = "mn61 Client", version = "1.0", clientSideOnly = true)
public class MN61Client {
    public static final KeyBinding MENU_KEY = new KeyBinding("Open Menu", Keyboard.KEY_RSHIFT, "mn61 Client");

    @EventHandler
    public void init(FMLInitializationEvent e) {
        ClientRegistry.registerKeyBinding(MENU_KEY);
        MinecraftForge.EVENT_BUS.register(new Events());
    }
}
