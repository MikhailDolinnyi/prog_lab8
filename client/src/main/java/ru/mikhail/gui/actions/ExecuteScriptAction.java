package ru.mikhail.gui.actions;



import ru.mikhail.App;
import ru.mikhail.commandLine.ConsoleOutput;
import ru.mikhail.gui.GuiManager;
import ru.mikhail.network.User;
import ru.mikhail.utility.Client;
import ru.mikhail.utility.InputManager;

import javax.swing.*;
import java.awt.event.ActionEvent;


import static javax.swing.JOptionPane.*;

public class ExecuteScriptAction extends Action{
    public ExecuteScriptAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        JLabel fileAsker = new JLabel(resourceBundle.getString("SelectFile"));
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(fileAsker)
                        .addComponent(fileChooser)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(fileAsker)
                .addComponent(fileChooser));

        int option = JOptionPane.showOptionDialog(null,
                panel,
                resourceBundle.getString("ScriptExecute"),
                JOptionPane.YES_NO_OPTION,
                QUESTION_MESSAGE,
                null,
                new String[]{resourceBundle.getString("Yes"), resourceBundle.getString("No")},
                resourceBundle.getString("Yes"));
        if(option == OK_OPTION) {
            try {
                ConsoleOutput.setFileMode(true);
                new InputManager(new ConsoleOutput(), client, user, App.getCommandManager()).fileExecution(fileChooser.getSelectedFile().getAbsolutePath());
                ConsoleOutput.setFileMode(false);
            } catch (Exception ignored) {}
        }
    }
}
