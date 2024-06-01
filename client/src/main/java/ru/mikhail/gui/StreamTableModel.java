package ru.mikhail.gui;

import ru.mikhail.models.SpaceMarine;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class StreamTableModel extends AbstractTableModel {
    private String[] columnNames;
    private ArrayList<SpaceMarine> allData;
    private ArrayList<SpaceMarine> filteredData;
    private Integer sortingColumn = 0;
    private boolean reversed = false;
    private final FilterWorker filterWorker;

    public StreamTableModel(String[] columnNames, FilterWorker filterWorker) {
        this.columnNames = columnNames;
        this.filterWorker = filterWorker;
    }

    public void setDataVector(ArrayList<SpaceMarine> data, String[] columnNames) {
        this.allData = data;
        this.columnNames = columnNames;
        this.filteredData = actFiltration(data);
    }

    public void performSorting(int column) {
        this.reversed = sortingColumn == column && !reversed;
        this.sortingColumn = column;
        this.filteredData = actFiltration(this.allData);
    }

    public void performFiltration() {
        this.filteredData = actFiltration(this.allData);
    }

    @Override
    public int getRowCount() {
        return this.filteredData.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.getValueAtRow(this.filteredData.get(rowIndex), columnIndex);
    }

    private ArrayList<SpaceMarine> actFiltration(ArrayList<SpaceMarine> allData) {
        if (Objects.isNull(this.sortingColumn)) return allData;
        ArrayList<SpaceMarine> sorted = new ArrayList<>(allData.stream()
                .sorted(Comparator.comparing(o -> this.sortingColumn < 0
                        ? (float) o.getId()
                        : this.getSortedFiledFloat(o, this.sortingColumn)))
                .filter(filterWorker.getPredicate())
                .toList());
        if (reversed) Collections.reverse(sorted);
        return sorted;
    }

    public Object getValueAtRow(SpaceMarine o, int row) {
        return switch (row) {
            case 0 -> o.getId();
            case 1 -> o.getName();
            case 2 -> o.getCoordinates();
            case 3 -> o.getCreationDate();
            case 4 -> o.getHealth();
            case 5 -> o.getAchievements();
            case 6 -> o.getHeight();
            case 7 -> o.getWeaponType();
            case 8 -> o.getChapter().getName();
            case 9 -> o.getChapter().getMarinesCount();
            case 10 -> o.getUserLogin();
            default -> throw new IllegalStateException("Unexpected value: " + row);
        };
    }

    public float getSortedFiledFloat(SpaceMarine o, int column) {
        // надо подумать
        return switch (column) {
            case 0 -> o.getId();
            case 1 -> o.getName().length();
            case 2 -> o.getCoordinates().getRadius();
            case 3 -> o.getCreationDate().toString().length();
            case 4 -> o.getHealth();
            case 5 -> o.getAchievements().length();
            case 6 -> o.getHeight();
            case 7 -> o.getWeaponType().ordinal();
            case 8 -> o.getChapter().getName().length();
            case 9 -> o.getChapter().getMarinesCount();
            case 10 -> o.getUserLogin().length();
            default -> throw new IllegalStateException("Unexpected value: " + column);
        };
    }

    public SpaceMarine getRow(int row) {
        try {
            return this.filteredData.get(row);
        } catch (IndexOutOfBoundsException e) {
            return this.filteredData.get(0);
        }
    }

    public ArrayList<SpaceMarine> getAllData() {
        return allData;
    }
}
