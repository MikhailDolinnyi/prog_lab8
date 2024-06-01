package ru.mikhail.gui.actions;



import ru.mikhail.gui.GuiManager;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;
import ru.mikhail.network.User;
import ru.mikhail.utility.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public class AverageOfHeightAction extends Action {
    public AverageOfHeightAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

//    private String askAverageHeight(){
//        BorderLayout layout = new BorderLayout();
//        JPanel panel = new JPanel(layout);
//        JLabel question = new JLabel(resourceBundle.getString("EnterAverageMark"));
//        JLabel markLabel = new JLabel(resourceBundle.getString("AverageMark"));
//        JFormattedTextField heightField = new JFormattedTextField(DecimalFormat.getInstance());
//
//        layout.addLayoutComponent(question, BorderLayout.NORTH);
//        layout.addLayoutComponent(markLabel, BorderLayout.WEST);
//        layout.addLayoutComponent(heightField, BorderLayout.EAST);
//
//        JOptionPane.showMessageDialog(null,
//                heightField,
//                "Count",
//                JOptionPane.PLAIN_MESSAGE);
//        return heightField.getText();
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Response response = client.sendAndAskResponse(new Request("average_of_height","", user, GuiManager.getLocale()));
        if(response.getStatus() == ResponseStatus.OK) JOptionPane.showMessageDialog(null, response.getResponse(), "Итог", JOptionPane.PLAIN_MESSAGE);
        else JOptionPane.showMessageDialog(null, resourceBundle.getString("NoResult"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
    }
}
