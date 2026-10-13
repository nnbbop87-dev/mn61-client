package com.mn61.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Events {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final List<Long> leftClicks = new ArrayList<Long>();
    private final List<Long> rightClicks = new ArrayList<Long>();
    private float savedGamma = -1f;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        if (MN61Client.MENU_KEY.isPressed() && mc.currentScreen == null) {
            mc.displayGuiScreen(new MenuGui());
        }

        // Toggle Sprint
        if (Settings.toggleSprint && mc.thePlayer != null && mc.currentScreen == null
                && mc.gameSettings.keyBindForward.isKeyDown()
                && !mc.thePlayer.isSneaking() && !mc.thePlayer.isUsingItem()
                && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
            mc.thePlayer.setSprinting(true);
        }

        // Fullbright
        if (Settings.fullbright) {
            if (savedGamma < 0) savedGamma = mc.gameSettings.gammaSetting;
            mc.gameSettings.gammaSetting = 100f;
        } else if (savedGamma >= 0) {
            mc.gameSettings.gammaSetting = savedGamma;
            savedGamma = -1f;
        }
    }

    @SubscribeEvent
    public void onMouse(MouseEvent e) {
        if (!e.buttonstate) return;
        if (e.button == 0) leftClicks.add(System.currentTimeMillis());
        else if (e.button == 1) rightClicks.add(System.currentTimeMillis());
    }

    private int cps(List<Long> list) {
        long now = System.currentTimeMillis();
        Iterator<Long> it = list.iterator();
        while (it.hasNext()) if (now - it.next() > 1000) it.remove();
        return list.size();
    }

    private int getPing() {
        if (mc.thePlayer == null || mc.getNetHandler() == null) return 0;
        NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        return info == null ? 0 : info.getResponseTime();
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text e) {
        if (!Settings.hud || mc.thePlayer == null || mc.gameSettings.showDebugInfo) return;
        FontRenderer fr = mc.fontRendererObj;
        int y = 4;
        int c = Settings.color;

        fr.drawStringWithShadow("mn61 Client", 4, y, c); y += 12;
        if (Settings.fps)  { fr.drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), 4, y, 0xFFFFFF); y += 10; }
        if (Settings.ping) { fr.drawStringWithShadow("Ping: " + getPing() + " ms", 4, y, 0xFFFFFF); y += 10; }
        if (Settings.cps)  { fr.drawStringWithShadow("CPS: " + cps(leftClicks) + " | " + cps(rightClicks), 4, y, 0xFFFFFF); y += 10; }
        if (Settings.coords) {
            fr.drawStringWithShadow(String.format("XYZ: %.0f %.0f %.0f", mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 4, y, 0xFFFFFF);
        }

        ScaledResolution sr = new ScaledResolution(mc);
        if (Settings.keystrokes) drawKeystrokes(4, sr.getScaledHeight() - 70);
        if (Settings.armorHud) drawArmor(sr.getScaledWidth() - 20, sr.getScaledHeight() / 2 - 40);
    }

    private void key(String label, int x, int y, int w, int h, boolean down) {
        Gui.drawRect(x, y, x + w, y + h, down ? 0xAAFFFFFF : 0x66000000);
        mc.fontRendererObj.drawString(label, x + (w - mc.fontRendererObj.getStringWidth(label)) / 2, y + (h - 8) / 2,
                down ? 0x000000 : 0xFFFFFF);
    }

    private void drawKeystrokes(int x, int y) {
        int s = 20, g = 2;
        key("W", x + s + g, y, s, s, Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
        key("A", x, y + s + g, s, s, Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
        key("S", x + s + g, y + s + g, s, s, Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
        key("D", x + 2 * (s + g), y + s + g, s, s, Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));
        key("LMB", x, y + 2 * (s + g), (int) (1.5 * s), 16, Mouse.isButtonDown(0));
        key("RMB", x + (int) (1.5 * s) + g, y + 2 * (s + g), (int) (1.5 * s), 16, Mouse.isButtonDown(1));
    }

    private void drawArmor(int x, int y) {
        for (int i = 3; i >= 0; i--) {
            ItemStack st = mc.thePlayer.inventory.armorInventory[i];
            if (st == null) { y += 18; continue; }
            GlStateManager.enableRescaleNormal();
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemAndEffectIntoGUI(st, x, y);
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            if (st.isItemStackDamageable()) {
                int left = st.getMaxDamage() - st.getItemDamage();
                mc.fontRendererObj.drawStringWithShadow(String.valueOf(left),
                        x - mc.fontRendererObj.getStringWidth(String.valueOf(left)) - 2, y + 4, 0xFFFFFF);
            }
            y += 18;
        }
    }
}
