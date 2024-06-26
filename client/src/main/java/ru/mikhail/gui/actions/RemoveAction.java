package ru.mikhail.gui.actions;


import ru.mikhail.gui.GuiManager;
import ru.mikhail.models.SpaceMarine;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;
import ru.mikhail.network.User;
import ru.mikhail.utility.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class RemoveAction extends Action {

    public RemoveAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    private Long getSelectedId() {
        Long[] userOwnedIds = guiManager.getCollection().stream()
                .filter((s) -> s.getUserLogin().equals(user.name()))
                .map(SpaceMarine::getId)
                .toArray(Long[]::new);

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);
        JLabel question = new JLabel(resourceBundle.getString("SelectIdForDelete"));
        JLabel idLabel = new JLabel(resourceBundle.getString("SelectId"));
        JComboBox idField = new JComboBox(userOwnedIds);

        layout.addLayoutComponent(question, BorderLayout.NORTH);
        layout.addLayoutComponent(idLabel, BorderLayout.WEST);
        layout.addLayoutComponent(idField, BorderLayout.EAST);


        int result = JOptionPane.showOptionDialog(null, idField, resourceBundle.getString("Delete"), JOptionPane.YES_OPTION,
                QUESTION_MESSAGE, null, new String[]{resourceBundle.getString("Delete")}, resourceBundle.getString("Delete"));
        if (result != OK_OPTION) {
            return null;
        }
//        JOptionPane.showMessageDialog(null,
//                idField,
//                resourceBundle.getString("Delete"),
//                JOptionPane.PLAIN_MESSAGE);
        return (Long) idField.getSelectedItem();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Long id = this.getSelectedId();
        if (id != null) {
//            if (id == null)
//                JOptionPane.showMessageDialog(null, resourceBundle.getString("NoObjects"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            Response response = client.sendAndAskResponse(new Request("remove_by_id", id.toString(), user, GuiManager.getLocale()));
            if (response.getStatus() == ResponseStatus.OK) {
                JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectDeleted"), resourceBundle.getString("Ok"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotDeleted"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
