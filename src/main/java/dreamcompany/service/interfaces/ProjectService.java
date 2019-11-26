package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.ProjectServiceModel;

import java.util.List;

public interface ProjectService {

    ProjectServiceModel create(ProjectServiceModel projectServiceModel);

    ProjectServiceModel edit(String id, ProjectServiceModel projectServiceModel);

    ProjectServiceModel delete(String id);

    List<ProjectServiceModel> findAll();

    List<ProjectServiceModel> findAllNotCompleted();

    List<ProjectServiceModel> findAllByStatus(String status);

    ProjectServiceModel findById(String id);

    boolean projectIsCompleted(String id);

    void complete(String id);
}
