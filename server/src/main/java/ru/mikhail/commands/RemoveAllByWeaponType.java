package ru.mikhail.commands;


import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.managers.CollectionManager;
import ru.mikhail.models.SpaceMarine;
import ru.mikhail.models.Weapon;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;
import ru.mikhail.network.ResponseStatus;
import ru.mikhail.utility.DatabaseHandler;

import java.util.Collection;
import java.util.Objects;

/**
 * Команда 'remove_all_weapon_type'
 * удаляет все элементы, значения поля weaponType которых эквивалентны заданному
 */
public class RemoveAllByWeaponType extends Command implements CollectionEditor {
    private final CollectionManager collectionManager;

    public RemoveAllByWeaponType(CollectionManager collectionManager) {
        super("remove_all_by_weapon_type", " weaponType: удалить все элементы" +
                ", значения поля weaponType которых эквивалентны заданному");
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
        if (request.getArgs().isBlank()) throw new IllegalArgumentsException();
        try {
            Weapon weaponType = Weapon.valueOf(request.getArgs().trim().toUpperCase());
            Collection<SpaceMarine> toRemove = collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(spaceMarine -> spaceMarine.getWeaponType() == weaponType)
                    .filter(spaceMarine -> Objects.equals(spaceMarine.getUserLogin(), request.getUser().name()))
                    .toList();

            if (DatabaseHandler.getDatabaseManager().deleteByWeaponType(request.getUser(),weaponType)) {
                collectionManager.removeElements(toRemove);
                return new Response(ResponseStatus.OK, "Ваши элементы удалены");
            }
            return new Response(ResponseStatus.ERROR, "Не удалось удалить элементы");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }


}
