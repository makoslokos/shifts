package com.rest.shifts.controller;

import com.rest.shifts.common.*;
import com.rest.shifts.domain.Shift;
import com.rest.shifts.domain.Worker;
import com.rest.shifts.repository.ShiftRepository;
import com.rest.shifts.services.ShiftService;
import com.rest.shifts.services.WorkerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/shifts")
@RequiredArgsConstructor
public class ShiftsController {
    @Autowired
    private WorkerService workerService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftValidator shiftValidator;

    @RequestMapping(method = RequestMethod.POST, value = "createShift", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addShift(@RequestBody ShiftDto shiftDto) {
        if(!shiftValidator.validateShift(shiftDto)){
            return new ResponseEntity(new ApiError("Wrong data provided"), HttpStatus.BAD_REQUEST);
        }
        try {
            shiftService.addShift(shiftDto);
        } catch(Exception e) {
            return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="shift/{shiftId}")
    public ResponseEntity deleteShift(@PathVariable("shiftId") int shiftId){
        try{
            shiftRepository.findById(shiftId).orElseThrow(ShiftNotFoundException::new);
        }catch (ShiftNotFoundException e){
            return new ResponseEntity(new ApiError("Shift not found"), HttpStatus.BAD_REQUEST);
        }
        shiftRepository.deleteById(shiftId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="worker/{workerId}")
    public ResponseEntity<List<Shift>> getShiftsForWorker(@PathVariable("workerId") int workerId){
        List<Shift> shifts = null;
        try{
            shifts = workerService.getShiftsForWorker(workerId);
        }catch (WorkerNotFoundException e){
            return new ResponseEntity(new ApiError("Worker not found"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(shifts);
    }

    @RequestMapping(method = RequestMethod.GET, value="/getAllShifts")
    public List<Shift> getAllShifts(){
        return shiftRepository.findAll();
    }

}
