package ru.mikhail.gui.actions;



import ru.mikhail.gui.GuiManager;
import ru.mikhail.models.Weapon;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;
import ru.mikhail.network.User;
import ru.mikhail.utility.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class RemoveAllByWeaponTypeAction extends Action {
    public RemoveAllByWeaponTypeAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    private String askWeaponType(){
        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);
        JLabel question = new JLabel(resourceBundle.getString("EnterAvgMark"));
        JLabel weaponTypeLabel = new JLabel(resourceBundle.getString("WeaponType"));
        JComboBox weaponTypeField;
        weaponTypeField = new JComboBox<>(Weapon.values());

        layout.addLayoutComponent(question, BorderLayout.NORTH);
        layout.addLayoutComponent(weaponTypeLabel, BorderLayout.WEST);
        layout.addLayoutComponent(weaponTypeField, BorderLayout.EAST);
        JOptionPane.showMessageDialog(null,
                weaponTypeField,
                "Remove",
                JOptionPane.PLAIN_MESSAGE);
        return Objects.requireNonNull(weaponTypeField.getSelectedItem()).toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Response response = client.sendAndAskResponse(new Request("remove_all_by_weapon_type", this.askWeaponType(), user, GuiManager.getLocale()));
        if(response.getStatus() == ResponseStatus.OK) JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectDeleted"), resourceBundle.getString("Result"), JOptionPane.PLAIN_MESSAGE);
        else JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotDeleted"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
    }
}
