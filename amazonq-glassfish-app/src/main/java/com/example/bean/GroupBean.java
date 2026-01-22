package com.example.bean;

import com.example.entity.Group;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("groupBean")
@SessionScoped
public class GroupBean implements Serializable {

    @PersistenceContext(unitName = "peoplePU")
    private EntityManager entityManager;

    private List<Group> groups;
    private Group selectedGroup;
    private Group newGroup;
    private boolean showAddForm = false;

    @PostConstruct
    public void init() {
        newGroup = new Group();
        System.out.println("GroupBean initialized");
    }

    @Transactional
    public void loadGroups() {
        try {
            groups = entityManager.createQuery("SELECT g FROM Group g ORDER BY g.name", Group.class)
                    .getResultList();
            System.out.println("Loaded " + groups.size() + " groups");
        } catch (Exception e) {
            System.out.println("Error loading groups: " + e.getMessage());
            groups = new ArrayList<>();
        }
    }

    @Transactional
    public String saveGroup() {
        if (newGroup.getName() == null || newGroup.getName().trim().isEmpty()) {
            addErrorMessage("Group name is required");
            return null;
        }
        
        entityManager.persist(newGroup);
        newGroup = new Group();
        showAddForm = false;
        loadGroups();
        addInfoMessage("Group created successfully");
        return "groups.xhtml?faces-redirect=true";
    }

    public String toggleAddForm() {
        System.out.println("toggleAddForm method called");
        this.showAddForm = true;
        newGroup = new Group();
        return null;
    }

    public String cancelAdd() {
        showAddForm = false;
        newGroup = new Group();
        return null;
    }

    // Getters and setters
    public boolean isShowAddForm() {
        return showAddForm;
    }

    public void setShowAddForm(boolean showAddForm) {
        this.showAddForm = showAddForm;
    }

    @Transactional
    public String updateGroup() {
        if (selectedGroup == null || selectedGroup.getId() == null) {
            addErrorMessage("No group selected for update");
            return null;
        }
        
        entityManager.merge(selectedGroup);
        selectedGroup = null;
        loadGroups();
        addInfoMessage("Group updated successfully");
        return "groups.xhtml?faces-redirect=true";
    }

    @Transactional
    public String deleteGroup(Group group) {
        entityManager.remove(entityManager.merge(group));
        loadGroups();
        addInfoMessage("Group deleted successfully");
        return "groups.xhtml?faces-redirect=true";
    }

    public List<Group> getGroups() {
        if (groups == null || groups.isEmpty()) {
            loadGroups();
        }
        return groups;
    }

    public void selectGroupForEdit(Group group) {
        this.selectedGroup = group;
    }

    public Group getSelectedGroup() { 
        if (selectedGroup == null) {
            selectedGroup = new Group();
        }
        return selectedGroup; 
    }
    public void setSelectedGroup(Group selectedGroup) { this.selectedGroup = selectedGroup; }

    public Group getNewGroup() { return newGroup; }
    public void setNewGroup(Group newGroup) { this.newGroup = newGroup; }

    private void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
}
