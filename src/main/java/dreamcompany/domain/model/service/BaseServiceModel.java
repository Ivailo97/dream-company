package dreamcompany.domain.model.service;

public abstract class BaseServiceModel {

    private String id;

    protected BaseServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
