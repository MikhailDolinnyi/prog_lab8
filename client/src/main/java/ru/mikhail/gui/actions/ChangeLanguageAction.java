package ru.mikhail.gui.actions;


import ru.mikhail.gui.GuiManager;
import ru.mikhail.network.User;
import ru.mikhail.utility.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

public class ChangeLanguageAction extends Action{


    public ChangeLanguageAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox languages = new JComboBox(new Object[]{
                new Locale("ru", "RU"),
                new Locale("is"),
                new Locale("bg"),
                new Locale("sp", "CR"),
        });
        JOptionPane.showMessageDialog(null,
                languages,
                "Choose language",
                JOptionPane.INFORMATION_MESSAGE);
        guiManager.setLocale((Locale) languages.getSelectedItem());
    }
}
