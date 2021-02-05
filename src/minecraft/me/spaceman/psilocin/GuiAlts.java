package me.spaceman.psilocin;

import me.spaceman.psilocin.gui.GuiPsyMenu;
import me.spaceman.psilocin.utils.LoginHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class GuiAlts extends GuiScreen {

    private int selectedIndex;
    private ArrayList<Alt> accountsList = new ArrayList<>();
    private AltList altSlots;
    private GuiButton login;
    private GuiButton back;

    public GuiAlts(String url)
    {
        try {
            URL altsUrl = new URL(url);
            Scanner s =  new Scanner(altsUrl.openStream());
            while(s.hasNextLine())
            {
                String line = s.nextLine();
                if(line.contains(":"))
                {
                    String[] split = line.split(":");
                    if(split.length == 2)
                    {
                        this.accountsList.add(new Alt(split[0], split[1]));
                    } else {
                        throw new IOException("Malformed Accounts List.");
                    }
                } else {
                    throw new IOException("Malformed Accounts List.");
                }
            }
            s.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.altSlots.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, "Alts", this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, "Login: " + EnumChatFormatting.GREEN + Minecraft.getMinecraft().session.getUsername(), 4, 20,  0xFFFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void login(int index)
    {
        if(this.accountsList.get(index) != null)
        {
            Alt alt = this.accountsList.get(index);
            Session session = LoginHelper.login(alt.username, alt.password);
            if(session != null)
            {
                Minecraft.getMinecraft().session = session;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(button.enabled)
        {
            if(button.id == 1)
            {
                this.login(selectedIndex);
            } else if(button.id == 2)
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
            } else {
                this.altSlots.actionPerformed(button);
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        this.altSlots = new AltList(Minecraft.getMinecraft());
        this.altSlots.registerScrollButtons(4, 5);
        this.buttonList.add(this.login = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, "Login"));
        this.buttonList.add(this.back = new GuiButton(2, this.width / 2 + 4, this.height - 52, 150, 20, "Back"));
        this.back.enabled = true;
        this.login.enabled = false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.altSlots.handleMouseInput();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        super.confirmClicked(result, id);
    }

    class Alt {
        private String username;
        private String password;
        public Alt(String username, String password)
        {
            this.username = username;
            this.password = password;
        }
    }

    class AltList extends GuiSlot {

        public AltList(Minecraft minecraft)
        {
            super(minecraft, GuiAlts.this.width, GuiAlts.this.height, 32, GuiAlts.this.height - 64, 36);

        }

        @Override
        protected int getSize() {
            return GuiAlts.this.accountsList.size();
        }

        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            GuiAlts.this.selectedIndex = slotIndex;
            GuiAlts.this.login.enabled = true;
            if(isDoubleClick)
            {
                GuiAlts.this.login(slotIndex);
            }
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return GuiAlts.this.selectedIndex == slotIndex;
        }

        @Override
        protected void drawBackground() {
            GuiAlts.this.drawDefaultBackground();
        }

        @Override
        protected int getContentHeight() {
            return GuiAlts.this.accountsList.size() * 36;
        }

        @Override
        protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
            String username = GuiAlts.this.accountsList.get(entryID).username;
            GuiAlts.this.drawString(GuiAlts.this.fontRendererObj, username, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
        }
    }
}
