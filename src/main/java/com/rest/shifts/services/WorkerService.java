package com.rest.shifts.services;

import com.rest.shifts.common.*;
import com.rest.shifts.domain.Shift;
import com.rest.shifts.domain.Worker;
import com.rest.shifts.mapper.ShiftsMapper;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import com.rest.shifts.repository.ShiftRepository;
import com.rest.shifts.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {
    private static final int HOURS_THRESHOLD = 8;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private ShiftsMapper shiftsMapper;

    public List<Shift> getShiftsForWorker(int workerId) throws WorkerNotFoundException{
        Worker worker = workerRepository.findById(workerId).orElseThrow(WorkerNotFoundException::new);
        return worker.getShifts();
    }

    public void assignShiftToWorker(int workerId, int shiftId) throws ShiftNotFoundException, WorkerNotFoundException,
            TwoShiftsInRowException{
        Shift shiftToBeAssignedToWorker = shiftRepository.findById(shiftId).orElseThrow(ShiftNotFoundException::new);
        ShiftDto shiftToBeAssignedToWorkerDto = shiftsMapper.mapToDto(shiftToBeAssignedToWorker);
        Worker worker = workerRepository.findById(workerId).orElseThrow(WorkerNotFoundException::new);
        List<Shift> workerShifts = worker.getShifts();

        if (workerDoesNotHaveAnyShifts(workerShifts)) {
            assignShift(shiftToBeAssignedToWorkerDto, worker);
        } else {
            Shift latestShift = getLatestShift(workerShifts);
            if (hoursBetweenCurrentAndPassedShift(latestShift, shiftToBeAssignedToWorker) < HOURS_THRESHOLD) {
                throw new TwoShiftsInRowException();
            } else {
                assignShift(shiftToBeAssignedToWorkerDto, worker);
            }
        }
    }

    private Shift getLatestShift(List<Shift> workerShifts){
        workerShifts.sort(Comparator.comparing(Shift::getTo));
        return workerShifts.get(workerShifts.size()-1);
    }

    private long hoursBetweenCurrentAndPassedShift(Shift previousShift, Shift currentShift) {
        return ChronoUnit.HOURS.between(previousShift.getTo(), currentShift.getFrom());
    }

    private void assignShift(ShiftDto shiftToBeAssignedToWorkerDto, Worker worker) {
        shiftToBeAssignedToWorkerDto.setWorkerList(worker);
        shiftRepository.save(shiftsMapper.mapToShift(shiftToBeAssignedToWorkerDto));
    }

    private boolean workerDoesNotHaveAnyShifts(List<Shift> shiftList) {
        return shiftList.size()==0;
    }
}
