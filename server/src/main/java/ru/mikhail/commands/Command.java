package ru.mikhail.commands;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.mikhail.exceptions.CommandRuntimeException;
import ru.mikhail.exceptions.ExitException;
import ru.mikhail.exceptions.IllegalArgumentsException;
import ru.mikhail.exceptions.InvalidFormException;
import ru.mikhail.network.Request;
import ru.mikhail.network.Response;

import java.util.Objects;

/**
 * Абстрактный класс для всех команд
 */
public abstract class Command {
    private final String name;
    private final String description;

    protected Logger commandLogger = LogManager.getLogger(this.getClass());

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public abstract Response execute(Request request) throws CommandRuntimeException, ExitException, IllegalArgumentsException, InvalidFormException;

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) &&
                Objects.equals(description, command.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return name + description;
    }

}
