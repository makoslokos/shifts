package com.rest.shifts.common;

import com.rest.shifts.domain.Worker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShiftDto {
    private int id;
    private LocalDateTime from;
    private LocalDateTime to;
    private List<Worker> workerList = new ArrayList<>();

    public ShiftDto(int id, LocalDateTime from, LocalDateTime to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }

    public int getId() {
        return id;
    }

    public void setWorkerList(Worker worker) {
        this.workerList.add(worker);
    }
}
