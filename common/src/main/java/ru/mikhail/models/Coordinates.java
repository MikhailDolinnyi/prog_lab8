package ru.mikhail.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс координат
 */

public class Coordinates implements FieldValidator, Serializable {
    private Double x; //Поле не может быть null
    private double y; //Значение поля должно быть больше -273

    public Coordinates(Double x, Double y) {
        this.x = x;
        this.y = y;

    }


    public Double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    /**
     * Валидирует правильность полей.
     *
     * @return true, если все верно, иначе false
     */
    @Override
    public boolean validate() {
        if (this.x == null) return false;
        return !(this.y < -273);
    }

    public float getRadius() {
        return (float) (Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        if (Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}
