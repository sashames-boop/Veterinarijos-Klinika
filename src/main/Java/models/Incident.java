package models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;
    private String comment;


    public Incident() {}
    public Incident(LocalDateTime registrationDate, LocalDateTime modificationDate,
                    IncidentStatus status, String comment) {
        this.registrationDate = registrationDate;
        this.modificationDate = modificationDate;
        this.status = status;
        this.comment = comment;
    }



    //Geteriai ir Setteriai
    public int getId() { return id; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public LocalDateTime getModificationDate() { return modificationDate; }
    public void setModificationDate(LocalDateTime modificationDate) { this.modificationDate = modificationDate; }

    public IncidentStatus getStatus() { return status; }
    public void setStatus(IncidentStatus status) { this.status = status; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}