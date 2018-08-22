package com.sensocon.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Contact.
 */
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "alert_number")
    private String alertNumber;

    @Column(name = "alert_email")
    private String alertEmail;

    @ManyToMany(mappedBy = "contacts")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotificationGroup> notificationGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Contact emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public Contact firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Contact lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Contact phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAlertNumber() {
        return alertNumber;
    }

    public Contact alertNumber(String alertNumber) {
        this.alertNumber = alertNumber;
        return this;
    }

    public void setAlertNumber(String alertNumber) {
        this.alertNumber = alertNumber;
    }

    public String getAlertEmail() {
        return alertEmail;
    }

    public Contact alertEmail(String alertEmail) {
        this.alertEmail = alertEmail;
        return this;
    }

    public void setAlertEmail(String alertEmail) {
        this.alertEmail = alertEmail;
    }

    public Set<NotificationGroup> getNotificationGroups() {
        return notificationGroups;
    }

    public Contact notificationGroups(Set<NotificationGroup> notificationGroups) {
        this.notificationGroups = notificationGroups;
        return this;
    }

    public Contact addNotificationGroup(NotificationGroup notificationGroup) {
        this.notificationGroups.add(notificationGroup);
        notificationGroup.getContacts().add(this);
        return this;
    }

    public Contact removeNotificationGroup(NotificationGroup notificationGroup) {
        this.notificationGroups.remove(notificationGroup);
        notificationGroup.getContacts().remove(this);
        return this;
    }

    public void setNotificationGroups(Set<NotificationGroup> notificationGroups) {
        this.notificationGroups = notificationGroups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contact contact = (Contact) o;
        if (contact.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contact.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", alertNumber='" + getAlertNumber() + "'" +
            ", alertEmail='" + getAlertEmail() + "'" +
            "}";
    }
}
