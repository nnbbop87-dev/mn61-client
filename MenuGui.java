package com.mn61.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class MenuGui extends GuiScreen {
    private static final String[] NAMES = {"HUD", "FPS", "Ping", "CPS", "Coords", "Keystrokes", "Toggle Sprint", "Fullbright", "Armor HUD"};

    private boolean get(int i) {
        switch (i) {
            case 0: return Settings.hud;
            case 1: return Settings.fps;
            case 2: return Settings.ping;
            case 3: return Settings.cps;
            case 4: return Settings.coords;
            case 5: return Settings.keystrokes;
            case 6: return Settings.toggleSprint;
            case 7: return Settings.fullbright;
            default: return Settings.armorHud;
        }
    }

    private void toggle(int i) {
        switch (i) {
            case 0: Settings.hud = !Settings.hud; break;
            case 1: Settings.fps = !Settings.fps; break;
            case 2: Settings.ping = !Settings.ping; break;
            case 3: Settings.cps = !Settings.cps; break;
            case 4: Settings.coords = !Settings.coords; break;
            case 5: Settings.keystrokes = !Settings.keystrokes; break;
            case 6: Settings.toggleSprint = !Settings.toggleSprint; break;
            case 7: Settings.fullbright = !Settings.fullbright; break;
            default: Settings.armorHud = !Settings.armorHud;
        }
    }

    private String label(int i) { return NAMES[i] + ": " + (get(i) ? "ON" : "OFF"); }

    @Override
    public void initGui() {
        buttonList.clear();
        int x = width / 2 - 100;
        int y = height / 2 - 90;
        for (int i = 0; i < NAMES.length; i++) {
            buttonList.add(new GuiButton(i, x, y + i * 22, 200, 20, label(i)));
        }
        buttonList.add(new GuiButton(100, x, y + NAMES.length * 22 + 6, 200, 20, "Account Manager"));
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        if (b.id == 100) { mc.displayGuiScreen(new AccountGui(this)); return; }
        toggle(b.id);
        b.displayString = label(b.id);
    }

    @Override
    public void drawScreen(int mx, int my, float pt) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "mn61 Client", width / 2, height / 2 - 112, Settings.color);
        super.drawScreen(mx, my, pt);
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
