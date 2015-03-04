package merlin.model.raw;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table BASE_MEAL.
 */
public class BaseMeal {

    private Long id;
    private String subject;
    private String description;
    private java.util.Date date;
    private String entityId;
    private String status;

    public BaseMeal() {
    }

    public BaseMeal(Long id) {
        this.id = id;
    }

    public BaseMeal(Long id, String subject, String description, java.util.Date date, String entityId, String status) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.date = date;
        this.entityId = entityId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
