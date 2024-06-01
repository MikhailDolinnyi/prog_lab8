package ru.mikhail.gui.actions;



import ru.mikhail.gui.GuiManager;
import ru.mikhail.models.Chapter;
import ru.mikhail.models.Coordinates;
import ru.mikhail.models.SpaceMarine;
import ru.mikhail.models.Weapon;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;
import ru.mikhail.network.User;
import ru.mikhail.utility.Client;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class RemoveLowerAction extends Action{
    public RemoveLowerAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);


//        JLabel mainLabel = new JLabel(resourceBundle.getString("Name"));
        JLabel nameLabel = new JLabel(resourceBundle.getString("Name"));
        JLabel cordXLabel = new JLabel(resourceBundle.getString("CoordX"));
        JLabel cordYLabel = new JLabel(resourceBundle.getString("CoordY"));
        JLabel healthLabel = new JLabel(resourceBundle.getString("Health"));
        JLabel achievementsLabel = new JLabel(resourceBundle.getString("Achievements"));
        JLabel heightLabel = new JLabel(resourceBundle.getString("Height"));
        JLabel weaponTypeLabel = new JLabel(resourceBundle.getString("WeaponType"));
        JLabel chapterLabel = new JLabel(resourceBundle.getString("Chapter"));
        JLabel chapterNameLabel = new JLabel(resourceBundle.getString("ChapterName"));
        JLabel chapterMarinesCountLabel = new JLabel(resourceBundle.getString("ChapterMarinesCount"));

        JFormattedTextField nameField;
        JFormattedTextField cordXField;
        JFormattedTextField cordYField;
        JFormattedTextField healthField;
        JFormattedTextField achievementsField;
        JFormattedTextField heightField;
        JComboBox weaponTypeField;
        JFormattedTextField chapterNameField;
        JFormattedTextField chapterMarinesCountField;


        // Action Listeners
        {
            nameField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    if (text.trim().isEmpty()) {
                        throw new ParseException(resourceBundle.getString("FiledNotEmpty"), 0);
                    }
                    return super.stringToValue(text);
                }
            });
            cordXField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Double num;
                    if (text.trim().isEmpty()) {
                        throw new ParseException(resourceBundle.getString("FiledNotEmpty"), 0);
                    }
                    try {
                        num = Double.parseDouble(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + "double", 0);
                    }
                    return num;
                }
            });
            cordYField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Double num;
                    try {
                        num = Double.parseDouble(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "double", 0);
                    }
                    if (num < -273) throw new ParseException(resourceBundle.getString("MaxNum") + " -273", 0);
                    return num;
                }
            });
            healthField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Integer num;
                    try {
                        num = Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "int", 0);
                    }
                    if (num <= 0)
                        throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });
            achievementsField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    if (text.trim().isEmpty()) {
                        throw new ParseException(resourceBundle.getString("FiledNotEmpty"), 0);
                    }
                    return super.stringToValue(text);
                }
            });
            heightField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Long num;
                    try {
                        num = Long.parseLong(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "long", 0);
                    }
                    if (num <= 0)
                        throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });
            weaponTypeField = new JComboBox<>(Weapon.values());
            chapterNameField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    if (text.trim().isEmpty()) {
                        throw new ParseException(resourceBundle.getString("FiledNotEmpty"), 0);
                    }
                    return super.stringToValue(text);
                }
            });
            chapterMarinesCountField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Integer num;
                    try {
                        num = Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "int", 0);
                    }
                    if (num <= 0)
                        throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });


        }
        // Default Values
        {
            nameField.setValue("P3132");
            cordXField.setValue("10.0");
            cordYField.setValue("10.0");
            healthField.setValue("15");
            achievementsField.setValue("2");
            heightField.setValue("4");
            chapterNameField.setValue("Moon");
            chapterMarinesCountField.setValue("5");
        }
        // Group Layout
        {
            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                            .addComponent(nameLabel)
                            .addComponent(nameField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(cordXLabel)
                            .addComponent(cordXField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(cordYLabel)
                            .addComponent(cordYField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(healthLabel)
                            .addComponent(healthField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(achievementsLabel)
                            .addComponent(achievementsField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(heightLabel)
                            .addComponent(heightField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(weaponTypeLabel)
                            .addComponent(weaponTypeField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(chapterLabel))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(chapterNameLabel)
                            .addComponent(chapterNameField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(chapterMarinesCountLabel)
                            .addComponent(chapterMarinesCountField))

            );
            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                            .addComponent(nameLabel)
                            .addComponent(cordXLabel)
                            .addComponent(cordYLabel)
                            .addComponent(healthLabel)
                            .addComponent(achievementsLabel)
                            .addComponent(heightLabel)
                            .addComponent(weaponTypeLabel)
                            .addComponent(chapterLabel)
                            .addComponent(chapterNameLabel)
                            .addComponent(chapterMarinesCountLabel)
                    )
                    .addGroup(layout.createParallelGroup()
                            .addComponent(nameField)
                            .addComponent(cordXField)
                            .addComponent(cordYField)
                            .addComponent(healthField)
                            .addComponent(achievementsField)
                            .addComponent(heightField)
                            .addComponent(weaponTypeField)
                            .addComponent(chapterNameField)
                            .addComponent(chapterMarinesCountField)

                    ));
        }
        int result = JOptionPane.showOptionDialog(null, panel, resourceBundle.getString("Remove"), JOptionPane.YES_OPTION,
                QUESTION_MESSAGE, null, new String[]{resourceBundle.getString("Remove")}, resourceBundle.getString("Remove"));
        if (result == OK_OPTION) {
            SpaceMarine spaceMarine = new SpaceMarine(
                    nameField.getText(),
                    new Coordinates(
                            Double.parseDouble(cordXField.getText()),
                            Double.parseDouble(cordYField.getText())
                    ),
                    LocalDateTime.now(),
                    Integer.parseInt(healthField.getText()),
                    achievementsField.getText(),
                    Long.parseLong(heightField.getText()),
                    (Weapon) weaponTypeField.getSelectedItem(),
                    new Chapter(
                            chapterNameField.getText(),
                            Integer.parseInt(chapterMarinesCountField.getText())

                    ),
                    user.name()
            );
            if (!spaceMarine.validate()) {
                JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotValid"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            Response response = client.sendAndAskResponse(new Request("remove_lower", "", user, spaceMarine, GuiManager.getLocale()));
            if(response.getStatus() == ResponseStatus.OK) JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectDeleted"), resourceBundle.getString("Ok"), JOptionPane.PLAIN_MESSAGE);
            else JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotDeleted"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
