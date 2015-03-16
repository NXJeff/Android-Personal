package merlin.model.raw;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table meal.
 */
public class Meal {

    private Long id;
    private String entityId;
    private String subject;
    private String description;
    private String mealType;
    private java.util.Date date;
    private String status;
    private Integer color;

    public Meal() {
    }

    public Meal(Long id) {
        this.id = id;
    }

    public Meal(Long id, String entityId, String subject, String description, String mealType, java.util.Date date, String status, Integer color) {
        this.id = id;
        this.entityId = entityId;
        this.subject = subject;
        this.description = description;
        this.mealType = mealType;
        this.date = date;
        this.status = status;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
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

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

}