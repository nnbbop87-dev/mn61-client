package com.mn61.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Account manager.
 * Premium: launch the client through your official launcher; that session is used.
 * Offline: sets a local username. Works only on singleplayer/LAN/offline-mode servers.
 */
public class AccountGui extends GuiScreen {
    private static Session originalSession;
    private final GuiScreen parent;
    private GuiTextField nameField;
    private String status = "";

    public AccountGui(GuiScreen parent) { this.parent = parent; }

    @Override
    public void initGui() {
        if (originalSession == null) originalSession = mc.getSession();
        nameField = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 2 - 30, 200, 20);
        nameField.setMaxStringLength(16);
        nameField.setFocused(true);
        buttonList.clear();
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 2, 200, 20, "Login (Offline / Cracked)"));
        buttonList.add(new GuiButton(2, width / 2 - 100, height / 2 + 24, 200, 20, "Restore Original (Premium)"));
        buttonList.add(new GuiButton(3, width / 2 - 100, height / 2 + 48, 200, 20, "Back"));
    }

    private void setSession(Session s) {
        ReflectionHelper.setPrivateValue(Minecraft.class, mc, s, "session", "field_71449_j");
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        if (b.id == 1) {
            String name = nameField.getText().trim();
            if (!name.matches("[A-Za-z0-9_]{3,16}")) { status = "Invalid name (3-16 chars, letters/numbers/_)"; return; }
            String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8))
                    .toString().replace("-", "");
            setSession(new Session(name, uuid, "0", "legacy"));
            status = "Offline account set: " + name;
        } else if (b.id == 2) {
            setSession(originalSession);
            status = "Original account restored: " + originalSession.getUsername();
        } else if (b.id == 3) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    protected void keyTyped(char c, int code) throws IOException {
        nameField.textboxKeyTyped(c, code);
        super.keyTyped(c, code);
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        super.mouseClicked(x, y, btn);
        nameField.mouseClicked(x, y, btn);
    }

    @Override
    public void updateScreen() { nameField.updateCursorCounter(); }

    @Override
    public void drawScreen(int mx, int my, float pt) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Account Manager", width / 2, height / 2 - 70, Settings.color);
        drawCenteredString(fontRendererObj, "Current: " + mc.getSession().getUsername(), width / 2, height / 2 - 54, 0xFFFFFF);
        nameField.drawTextBox();
        drawCenteredString(fontRendererObj, status, width / 2, height / 2 + 76, 0xFFAA00);
        drawCenteredString(fontRendererObj, "Offline works only on offline-mode servers (not Hypixel).", width / 2, height / 2 + 90, 0x888888);
        super.drawScreen(mx, my, pt);
    }
}
