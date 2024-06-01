package ru.mikhail.utility;


import ru.mikhail.commandLine.Printable;
import ru.mikhail.commands.CommandManager;
import ru.mikhail.exceptions.ExitException;
import ru.mikhail.gui.GuiManager;
import ru.mikhail.models.SpaceMarine;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * Класс обработки пользовательского ввода
 */
public class InputManager {


    private final CommandManager commandManager;

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("GuiLabels", GuiManager.getLocale());


    private final Printable console;
    private final Client client;
    private User user;


    public InputManager(Printable console, Client client, User user, CommandManager commandManager) {
        this.console = console;
        this.client = client;
        this.user = user;
        this.commandManager = commandManager;


    }


    /**
     * Перманентная работа с пользователем и выполнение команд
     */


    private void printResponse(Response response) {
        switch (response.getStatus()) {
            case OK -> {
                if ((Objects.isNull(response.getCollection()))) {
                    console.println(response.getResponse());
                } else {
                    console.println(response.getResponse() + "\n" + response.getCollection().toString());
                }
            }
            case ERROR -> console.printError(response.getResponse());
            case WRONG_ARGUMENTS -> console.printError("Неверное использование команды!");
            default -> {
            }
        }
    }

    public void fileExecution(String args) throws ExitException {
        if (args == null || args.isEmpty()) console.printError(resourceBundle.getString("Путь не распознан"));
//            return;
//        } else console.println(OutputColors.toColor(resourceBundle.getString("Путь получен успешно"), OutputColors.PURPLE));
        assert args != null;
        args = args.trim();
        try {
            ExecuteManager.pushFile(args);
            for (String line = ExecuteManager.readLine(); line != null; line = ExecuteManager.readLine()) {
                String[] userCommand = (line + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                if (userCommand[0].isBlank()) return;
                if (userCommand[0].equals("execute_script")) {
                    if (ExecuteManager.fileRepeat(userCommand[1])) {
                        console.printError(MessageFormat.format(resourceBundle.getString("FoundRecursion"), new File(userCommand[1]).getAbsolutePath()));
                    }
                } else if (commandManager.containsCommand(userCommand[0])) {
//                    console.println(OutputColors.toColor(resourceBundle.getString("Выполнение команды ") + userCommand[0], OutputColors.YELLOW));
                    SpaceMarine spaceMarine = commandManager.execute(commandManager.getCommand(userCommand[0]), userCommand[1]);
                    Response response = client.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim(), user, spaceMarine));
                    this.printResponse(response);
                } else {
//                    console.println(OutputColors.toColor(resourceBundle.getString("Выполнение команды ") + userCommand[0], OutputColors.YELLOW));
                    Response response = client.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim(), user));
                    this.printResponse(response);
                    switch (response.getStatus()) {
                        case EXIT -> throw new ExitException();
                        case EXECUTE_SCRIPT -> {
                            this.fileExecution(response.getResponse());
                            ExecuteManager.popRecursion();
                        }
                        case LOGIN_FAILED -> {
                            console.printError("Ошибка с вашим аккаунтом. Зайдите в него снова");
                            this.user = null;

                        }
                        default -> {
                        }
                    }
                }

            }
            ExecuteManager.popFile();
        } catch (FileNotFoundException fileNotFoundException) {
            console.printError(resourceBundle.getString("FileNotExists"));
        } catch (IOException e) {
            console.printError("Ошибка ввода вывода");
        }
    }
}

