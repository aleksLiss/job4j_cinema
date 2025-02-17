package model;

public interface Builder {

    void setId(int id);
    void setName(String name);
    void setDescription(String description);
    void setYear(int year);
    void setGenreId(int genreId);
    void setMinimalAge(int minimalAge);
    void setDurationInTime(int durationInTime);
    void setFileId(int fileId);
}
