package ru.mikhail.gui.actions;


import ru.mikhail.gui.GuiManager;
import ru.mikhail.network.User;
import ru.mikhail.utility.Client;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Action extends AbstractAction {
    protected ResourceBundle resourceBundle;

    protected User user;
    protected Client client;
    protected GuiManager guiManager;

    public Action(User user, Client client, GuiManager guiManager) {
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
        this.resourceBundle = ResourceBundle.getBundle("GuiLabels", guiManager.getLocale());
    }
}
