package com.rest.shifts.common;

import com.rest.shifts.domain.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class WorkerDto {
    private int id;
    private String firstName;
    private String lastName;
    private List<Shift> shifts = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Shift shifts) {
        this.shifts.add(shifts);
    }

    public WorkerDto(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
