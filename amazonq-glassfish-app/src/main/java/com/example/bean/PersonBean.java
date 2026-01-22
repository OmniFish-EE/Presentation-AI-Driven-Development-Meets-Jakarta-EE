package com.example.bean;

import com.example.entity.Group;
import com.example.entity.Person;
import com.example.service.GroupService;
import com.example.service.PersonService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * JSF managed bean for Person UI operations
 * Follows separation of concerns by delegating business logic to service layer
 */
@Named
@SessionScoped
public class PersonBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PersonBean.class.getName());

    @Inject
    private PersonService personService;

    @Inject
    private GroupService groupService;

    private List<Person> people;
    private Person selectedPerson;
    private Person newPerson = new Person();
    private List<Group> availableGroups;

    public void loadPeople() {
        try {
            people = personService.findAll();
            LOGGER.info("Loaded " + people.size() + " people");
        } catch (Exception e) {
            LOGGER.severe("Error loading people: " + e.getMessage());
            addErrorMessage("Error loading people: " + e.getMessage());
        }
    }

    public void loadGroups() {
        try {
            availableGroups = groupService.findAll();
            LOGGER.info("Loaded " + availableGroups.size() + " groups");
        } catch (Exception e) {
            LOGGER.severe("Error loading groups: " + e.getMessage());
            addErrorMessage("Error loading groups: " + e.getMessage());
        }
    }

    public void savePerson() {
        try {
            personService.create(newPerson);
            newPerson = new Person();
            loadPeople();
            addInfoMessage("Person created successfully");
            LOGGER.info("Person created successfully");
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Validation error creating person: " + e.getMessage());
            addErrorMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error creating person: " + e.getMessage());
            addErrorMessage("Error creating person. Please try again.");
        }
    }

    public String updatePerson() {
        if (selectedPerson == null || selectedPerson.getId() == null) {
            addErrorMessage("No person selected for update");
            return null;
        }

        try {
            // Refresh the selectedPerson from database to ensure we have the latest data
            Person personToUpdate = personService.findById(selectedPerson.getId()).orElse(null);
            if (personToUpdate == null) {
                addErrorMessage("Person not found");
                return null;
            }
            
            // Update with form values
            personToUpdate.setFirstName(selectedPerson.getFirstName());
            personToUpdate.setLastName(selectedPerson.getLastName());
            personToUpdate.setGroups(selectedPerson.getGroups());
            
            personService.update(personToUpdate.getId(), personToUpdate);
            selectedPerson = null;
            loadPeople();
            addInfoMessage("Person updated successfully");
            LOGGER.info("Person updated successfully");
            return "people.xhtml?faces-redirect=true";
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Validation error updating person: " + e.getMessage());
            addErrorMessage(e.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.severe("Error updating person: " + e.getMessage());
            addErrorMessage("Error updating person. Please try again.");
            return null;
        }
    }

    public String deletePerson(Person person) {
        if (person == null || person.getId() == null) {
            addErrorMessage("Invalid person for deletion");
            return null;
        }

        try {
            personService.delete(person.getId());
            loadPeople();
            addInfoMessage("Person deleted successfully");
            LOGGER.info("Person deleted successfully: " + person.getFirstName() + " " + person.getLastName());
            return "people.xhtml?faces-redirect=true";
        } catch (Exception e) {
            LOGGER.severe("Error deleting person: " + e.getMessage());
            addErrorMessage("Error deleting person. Please try again.");
            return null;
        }
    }

    public List<Person> getPeople() {
        if (people == null) {
            loadPeople();
        }
        return people;
    }

    public List<Group> getAvailableGroups() {
        if (availableGroups == null) {
            loadGroups();
        }
        return availableGroups;
    }

    public void selectPersonForEdit(Person person) {
        this.selectedPerson = person;
    }

    public Person getSelectedPerson() { 
        if (selectedPerson == null) {
            selectedPerson = new Person();
        }
        return selectedPerson; 
    }
    public void setSelectedPerson(Person selectedPerson) { this.selectedPerson = selectedPerson; }

    public Person getNewPerson() { return newPerson; }
    public void setNewPerson(Person newPerson) { this.newPerson = newPerson; }

    private void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
}
