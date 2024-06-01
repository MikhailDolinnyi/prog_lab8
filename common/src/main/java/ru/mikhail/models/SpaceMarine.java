package ru.mikhail.models;


import ru.mikhail.utility.OutputColors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


import java.util.Objects;


/**
 * Класс космический морской пехотинец
 */


public class SpaceMarine implements FieldValidator, Comparable<SpaceMarine>, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int health; //Значение поля должно быть больше 0
    private String achievements; //Поле не может быть null
    private Long height; //Поле может быть null
    private Weapon weaponType; //Поле не может быть null
    private Chapter chapter; //Поле может быть null
    private String userLogin;


    private static Long nextId = 1L;


    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public SpaceMarine(String name, Coordinates coordinates, LocalDateTime creationDate, int health, String achievements, Long height, Weapon weaponType,
                       Chapter chapter) {
        this.id = incNextId();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.achievements = achievements;
        this.height = height;
        this.weaponType = weaponType;
        this.chapter = chapter;
    }



    public SpaceMarine(Long id, String name, Coordinates coordinates, Timestamp creationDate, int health, String achievements, long height, Weapon weaponType, Chapter chapter, String ownerLogin) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate.toLocalDateTime();
        this.health = health;
        this.achievements = achievements;
        this.height = height;
        this.weaponType = weaponType;
        this.chapter = chapter;
        this.userLogin = ownerLogin;
    }

    public SpaceMarine(String name, Coordinates coordinates, LocalDateTime creationDate, int health, String achievements, long height, Weapon weaponType, Chapter chapter, String ownerLogin) {
        this.id = incNextId();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.achievements = achievements;
        this.height = height;
        this.weaponType = weaponType;
        this.chapter = chapter;
        this.userLogin = ownerLogin;
    }

    /**
     * @return возвращает следующий id во избежание их повторения
     */
    private static Long incNextId() {
        return nextId++;
    }

    public static void updateId(PriorityQueue<SpaceMarine> collection) {
        nextId = collection.stream()
                .filter(Objects::nonNull)
                .map(SpaceMarine::getId)
                .mapToLong(Long::longValue) // Меняем на mapToLong для использования Long
                .max()
                .orElse(0L) + 1; // Указываем 0L вместо 0, чтобы явно указать на тип Long

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Weapon getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(Weapon weaponType) {
        this.weaponType = weaponType;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    /**
     * Компаратор объектов основанный на количестве очков здоровья
     *
     * @param obj объект с которым нужно сравнить объект
     */
    @Override
    public int compareTo(SpaceMarine obj) {
        return this.getName().compareTo(obj.getName());
    }

    /**
     * Метод валидирующие поля по условию
     *
     * @return true если поля валидные, false иначе
     */
    @Override
    public boolean validate() {
        if (this.id == null || this.id <= 0) {
            return false;
        }
        if (this.name == null || this.name.isEmpty()) {
            return false;
        }
        if (this.coordinates == null) {
            return false;
        }
        if (this.creationDate == null) {
            return false;
        }
        if (this.health <= 0) {
            return false;
        }
        if (this.achievements == null) {
            return false;
        }
        if (!this.chapter.validate()) {
            return false;
        }
        return this.weaponType != null;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        SpaceMarine that = (SpaceMarine) o;
        return id.equals(that.id) &&
                health == that.health &&
                Objects.equals(achievements, that.achievements) &&
                Objects.equals(height, that.height) &&
                Objects.equals(name, that.name) &&
                Objects.equals(coordinates, that.coordinates) &&
                creationDate.toString().equals(that.creationDate.toString()) &&
                weaponType.toString().equals(that.weaponType.toString()) &&
                Objects.equals(chapter, that.chapter) &&
                Objects.equals(userLogin, that.userLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, health, achievements, height, weaponType, chapter, userLogin);
    }

    @Override
    public String toString() {
        return "SpaceMarine{" + '\n' +
                OutputColors.toColor("id: ", OutputColors.CYAN) + id + '\n' +
                OutputColors.toColor("name: ", OutputColors.CYAN) + name + '\n' +
                OutputColors.toColor("coordinates: ", OutputColors.CYAN) + coordinates + '\n' +
                OutputColors.toColor("creationDate: ", OutputColors.CYAN) + creationDate + '\n' +
                OutputColors.toColor("health: ", OutputColors.CYAN) + health + '\n' +
                OutputColors.toColor("achievements ", OutputColors.CYAN) + achievements + '\n' +
                OutputColors.toColor("height: ", OutputColors.CYAN) + height + '\n' +
                OutputColors.toColor("weaponType: ", OutputColors.CYAN) + weaponType + '\n' +
                OutputColors.toColor("chapter: ", OutputColors.CYAN) + chapter + '}' + '\n' +
                OutputColors.toColor("userLogin = ", OutputColors.CYAN) + userLogin + '\n';
    }


}
