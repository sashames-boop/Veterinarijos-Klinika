package models;

import java.time.LocalDateTime;

public class Incident {
    private int id;
    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;
    private IncidentStatus status;
    private String comment;
}