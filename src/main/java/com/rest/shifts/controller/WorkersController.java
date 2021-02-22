package com.rest.shifts.controller;

import com.rest.shifts.common.*;
import com.rest.shifts.domain.Worker;
import com.rest.shifts.mapper.WorkerMapper;
import com.rest.shifts.repository.WorkerRepository;
import com.rest.shifts.services.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/workers")
@RequiredArgsConstructor
public class WorkersController {
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    WorkerMapper workerMapper;
    @Autowired
    WorkerService workerService;

    @RequestMapping(method = RequestMethod.GET, value = "worker/{workerId}")
    public Worker getWorker(@PathVariable("workerId") int workerId) throws WorkerNotFoundException {
        return workerRepository.findById(workerId).orElseThrow(WorkerNotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.POST, value = "createWorker", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addWorker(@RequestBody WorkerDto workerDto){
        Worker worker = workerMapper.mapToWorker(workerDto);
        workerRepository.save(worker);
    }

    @RequestMapping(method = RequestMethod.GET, value = "getWorkers")
    public List<WorkerDto> getWorkers(){
        List<Worker> workerList = workerRepository.findAll();
        return workerMapper.mapToTaskDtoList(workerList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "updateWorker")
    public WorkerDto updateWorker(@RequestBody WorkerDto workerDto){
        Worker worker = workerMapper.mapToWorker(workerDto);
        workerRepository.save(worker);
        return workerMapper.mapToWorkerDto(worker);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "deleteWorker/{workerId}")
    public void deleteWorker(@PathVariable("workedId") int workerId){
        workerRepository.deleteById(workerId);
    }

    @RequestMapping(method = RequestMethod.POST, value="assignShiftToWorker")
    public ResponseEntity assignShiftToWorker(int shiftId, int workerId){
        try{
            workerService.assignShiftToWorker(workerId, shiftId);
        }catch(TwoShiftsInRowException e){
            return new ResponseEntity(new ApiError("Two shifts in a row"), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(WorkerNotFoundException e){
            return new ResponseEntity(new ApiError("Worker not found"), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(ShiftNotFoundException e){
            return new ResponseEntity(new ApiError("Shift not found"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
