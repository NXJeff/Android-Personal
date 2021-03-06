package merlin.model.raw;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table reminder.
 */
public class Reminder {

    private Long id;
    private String entityId;
    private String subject;
    private String description;
    private java.util.Date fromDate;
    private java.util.Date toDate;
    private java.util.Date atTime;
    private Integer repeatType;
    private String reminderType;
    private Integer customRepeatMode;
    private Integer color;
    private Integer repeatEveryNDay;
    private Integer dosesInTotal;
    private Integer dosesPerDay;
    private Boolean enabled;
    private String status;

    public Reminder() {
    }

    public Reminder(Long id) {
        this.id = id;
    }

    public Reminder(Long id, String entityId, String subject, String description, java.util.Date fromDate, java.util.Date toDate, java.util.Date atTime, Integer repeatType, String reminderType, Integer customRepeatMode, Integer color, Integer repeatEveryNDay, Integer dosesInTotal, Integer dosesPerDay, Boolean enabled, String status) {
        this.id = id;
        this.entityId = entityId;
        this.subject = subject;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.atTime = atTime;
        this.repeatType = repeatType;
        this.reminderType = reminderType;
        this.customRepeatMode = customRepeatMode;
        this.color = color;
        this.repeatEveryNDay = repeatEveryNDay;
        this.dosesInTotal = dosesInTotal;
        this.dosesPerDay = dosesPerDay;
        this.enabled = enabled;
        this.status = status;
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

    public java.util.Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(java.util.Date fromDate) {
        this.fromDate = fromDate;
    }

    public java.util.Date getToDate() {
        return toDate;
    }

    public void setToDate(java.util.Date toDate) {
        this.toDate = toDate;
    }

    public java.util.Date getAtTime() {
        return atTime;
    }

    public void setAtTime(java.util.Date atTime) {
        this.atTime = atTime;
    }

    public Integer getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(Integer repeatType) {
        this.repeatType = repeatType;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public Integer getCustomRepeatMode() {
        return customRepeatMode;
    }

    public void setCustomRepeatMode(Integer customRepeatMode) {
        this.customRepeatMode = customRepeatMode;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getRepeatEveryNDay() {
        return repeatEveryNDay;
    }

    public void setRepeatEveryNDay(Integer repeatEveryNDay) {
        this.repeatEveryNDay = repeatEveryNDay;
    }

    public Integer getDosesInTotal() {
        return dosesInTotal;
    }

    public void setDosesInTotal(Integer dosesInTotal) {
        this.dosesInTotal = dosesInTotal;
    }

    public Integer getDosesPerDay() {
        return dosesPerDay;
    }

    public void setDosesPerDay(Integer dosesPerDay) {
        this.dosesPerDay = dosesPerDay;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
