package ru.job4j.cinema.dto;

public class TicketDto {

    private String filmName;
    private String hallName;
    private String startTime;
    private int rowNumber;
    private int placeNumber;

    public TicketDto(String filmName, String hallName, String startTime, int rowNumber, int placeNumber) {
        this.filmName = filmName;
        this.hallName = hallName;
        this.startTime = startTime;
        this.rowNumber = rowNumber;
        this.placeNumber = placeNumber;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }
}
