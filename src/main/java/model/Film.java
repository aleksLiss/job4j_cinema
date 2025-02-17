package model;

public class Film implements Builder {

    private int id;
    private String name;
    private String description;
    private int year;
    private int genreId;
    private int minimalAge;
    private int durationInTime;
    private int fileId;

    public Film(int id, String name, String description, int year, int genreId, int minimalAge, int durationInTime, int fileId) {
    }

    public Film build() {
        return new Film(id, name, description, year, genreId, minimalAge, durationInTime, fileId);
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    @Override
    public void setMinimalAge(int minimalAge) {
        this.minimalAge = minimalAge;
    }

    @Override
    public void setDurationInTime(int durationInTime) {
        this.durationInTime = durationInTime;
    }

    @Override
    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
