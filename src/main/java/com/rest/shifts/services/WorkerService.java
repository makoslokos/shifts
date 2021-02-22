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
        Shift currentShift = shiftRepository.findById(shiftId).orElseThrow(ShiftNotFoundException::new);
        ShiftDto currentShiftDto = shiftsMapper.mapToDto(currentShift);
        Worker worker = workerRepository.findById(workerId).orElseThrow(WorkerNotFoundException::new);

        List<Shift> shiftList = worker.getShifts();
        if (workerDoesNotHaveAnyShifts(shiftList)) {
            saveShift(currentShiftDto, worker);
        } else {
            shiftList.sort(Comparator.comparing(Shift::getTo));
            Shift previousShift = shiftList.get(shiftList.size()-1);
            ShiftDto latestShiftDto = shiftsMapper.mapToDto(previousShift);

            if (hoursBetweenCurrentAndPassedShift(previousShift, currentShift) < HOURS_THRESHOLD) {
                throw new TwoShiftsInRowException();
            } else {
                shiftRepository.save(shiftsMapper.mapToShift(latestShiftDto));
            }
        }
    }

    private long hoursBetweenCurrentAndPassedShift(Shift previousShift, Shift currentShift) {
        return ChronoUnit.HOURS.between(previousShift.getTo(), currentShift.getFrom());
    }

    private void saveShift(ShiftDto currentShiftDto, Worker worker) {
        currentShiftDto.setWorkerList(worker);
        shiftRepository.save(shiftsMapper.mapToShift(currentShiftDto));
    }

    private boolean workerDoesNotHaveAnyShifts(List<Shift> shiftList) {
        return shiftList.size()==0;
    }
}
