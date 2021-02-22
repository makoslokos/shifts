package com.rest.shifts.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="WORKER")
public class Worker {
    private int id;
    private String firstName;
    private String lastName;
    private List<Shift> shifts;

    public Worker(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        shifts = new ArrayList<>();
    }

    public Worker() {
    }

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "WORKER_ID", unique = true)
    public int getId() {
        return id;
    }
    @Column(name="FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }
    @Column(name="LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    @ManyToMany(mappedBy = "workerList")
    @JsonIgnore // https://dzone.com/articles/introduction-to-spring-data-jpa-part-4-bidirection CTRL+F i szukaj StackOverflow, taki był błąd, dodałem to JsonIgnore, ale nie testowałem bo mi docker z postgresem coś padł :) Powinno działać
    public List<Shift> getShifts() {
        return shifts;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }
}
