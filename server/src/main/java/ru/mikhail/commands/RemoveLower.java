package ru.mikhail.commands;


import ru.mikhail.exceptions.FIleFieldException;
import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.managers.CollectionManager;
import ru.mikhail.models.SpaceMarine;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;
import ru.mikhail.utility.DatabaseHandler;

import java.util.Collection;
import java.util.Objects;

/**
 * Команда 'remove_lower'
 * Удаляет из коллекции все элементы, меньшие, чем заданный
 */
public class RemoveLower extends Command implements CollectionEditor{
    private final CollectionManager collectionManager;

    public RemoveLower(CollectionManager collectionManager) {
        super("remove_lower", "{element} : удалить из коллекции все элементы, меньшие, чем заданный");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнить команду
     *
     * @param request аргументы команды
     * @throws IllegalArgumentsException неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentsException {
        if (!request.getArgs().isBlank()) throw new IllegalArgumentsException();
        class NoElements extends RuntimeException {
        }
        try {
            Collection<SpaceMarine> toRemove = collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(spaceMarine -> spaceMarine.compareTo(request.getObject()) <= -1)
                    .filter(spaceMarine -> spaceMarine.getUserLogin().equals(request.getUser().name()))
                    .filter((obj) -> DatabaseHandler.getDatabaseManager().deleteObject(Math.toIntExact(obj.getId()), request.getUser()))
                    .toList();
            if (toRemove.isEmpty()) {
                return new Response(ResponseStatus.ERROR, "Нет элементов, меньших, чем заданный");
            } else {
                collectionManager.removeElements(toRemove);
            }
            return new Response(ResponseStatus.OK, "Удалены элементы меньшие чем заданный");
        } catch (NoElements e) {
            return new Response(ResponseStatus.ERROR, "В коллекции нет элементов");
        } catch (FIleFieldException e) {
            return new Response(ResponseStatus.ERROR, "Поля в файле не валидны! Объект не создан");
        }
    }

}
