package com.Projet_E2425G3_1.GestionProjets.entities;

import com.Projet_E2425G3_1.GestionProjets.entities.ids.UserProjectId;
import jakarta.persistence.*;

@Entity
public class UserProject {
    @EmbeddedId
    private UserProjectId id;

    @ManyToOne
    @MapsId("userId") //
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_user_userproject"))
    private User user;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_project_userproject"))
    private Project project;


    public UserProject() {

    }

    public UserProject(UserProjectId id, User user, Project project) {
        this.id = id;
        this.user = user;
        this.project = project;
    }

    public static UserProjectBuilder builder() {
        return new UserProjectBuilder();
    }

    public UserProjectId getId() {
        return id;
    }

    public void setId(UserProjectId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    public static class UserProjectBuilder {
        private UserProjectId id;
        private User user;
        private Project project;

        UserProjectBuilder() {
        }

        public UserProjectBuilder id(UserProjectId id) {
            this.id = id;
            return this;
        }

        public UserProjectBuilder user(User user) {
            this.user = user;
            return this;
        }

        public UserProjectBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public UserProject build() {
            return new UserProject(this.id, this.user, this.project);
        }

        public String toString() {
            return "UserProject.UserProjectBuilder(id=" + this.id + ", user=" + this.user + ", project=" + this.project + ")";
        }
    }
}
