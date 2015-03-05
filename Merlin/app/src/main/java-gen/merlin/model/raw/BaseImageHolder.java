package merlin.model.raw;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table image_holder.
 */
public class BaseImageHolder {

    private Long id;
    private String entityId;
    private String path;

    public BaseImageHolder() {
    }

    public BaseImageHolder(Long id) {
        this.id = id;
    }

    public BaseImageHolder(Long id, String entityId, String path) {
        this.id = id;
        this.entityId = entityId;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
