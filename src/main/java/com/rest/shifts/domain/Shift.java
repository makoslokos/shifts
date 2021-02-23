package com.rest.shifts.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Table(name="SHIFTS")
@NamedNativeQuery(
        name = "Shift.getShiftWithTheSameStartingAndEnd",
        query = "SELECT * FROM SHIFTS WHERE STARTING_DATETIME=:STARTING_DATETIME" +
                " AND  END_DATETIME=:END_DATETIME",
        resultClass = Shift.class
)
public class Shift {
    private int id;
    private LocalDateTime from;
    private LocalDateTime to;

    private List<Worker> workerList = new ArrayList<>();

    public Shift() {
    }

    public Shift(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "SHIFT_ID", unique = true)
    public int getId() {
        return id;
    }

    @Column(name="STARTING_DATETIME")
    public LocalDateTime getFrom() {
        return from;
    }

    @Column(name="END_DATETIME")
    public LocalDateTime getTo() {
        return to;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "JOIN_SHIFT_WORKER",
            joinColumns = {@JoinColumn(name = "SHIFT_ID", referencedColumnName = "SHIFT_ID")},
                    inverseJoinColumns = {@JoinColumn(name = "WORKER", referencedColumnName = "WORKER_ID")}
            )
    public List<Worker> getWorkerList() {
        return workerList;
    }

    public void setWorker(Worker worker){
        workerList.add(worker);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public void setWorkerList(List<Worker> workerList) {
        this.workerList = workerList;
    }
}
