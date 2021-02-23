package com.rest.shifts.mapper;

import com.rest.shifts.domain.Worker;
import com.rest.shifts.common.WorkerDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerMapper {
    public Worker mapToWorker(WorkerDto workerDto){
        return new Worker(workerDto.getFirstName(), workerDto.getLastName());
    }

    public WorkerDto mapToDto(Worker worker){
        return new WorkerDto(worker.getId(),worker.getFirstName(), worker.getLastName());
    }

    public List<WorkerDto> mapToTaskDtoList(final List<Worker> taskList) {
        return taskList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
