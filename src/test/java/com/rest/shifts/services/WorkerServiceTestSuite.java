package com.rest.shifts.services;

import com.rest.shifts.common.ShiftNotFoundException;
import com.rest.shifts.common.TwoShiftsInRowException;
import com.rest.shifts.common.WorkerNotFoundException;
import com.rest.shifts.domain.Shift;
import com.rest.shifts.domain.Worker;
import com.rest.shifts.repository.ShiftRepository;
import com.rest.shifts.repository.WorkerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WorkerServiceTestSuite {
    @Autowired
    WorkerService workerService;
    @MockBean
    Worker worker;
    @MockBean
    ShiftRepository shiftRepository;
    @MockBean
    WorkerRepository workerRepository;

    @Test
    public void getShiftsForWorkerThrowsWorkerNotFoundExceptionTest(){
        //given
        Optional<Worker> worker = Optional.empty();
        int workerId = 1;
        when(workerRepository.findById(anyInt())).thenReturn(worker);
        //when

        //then
        Assertions.assertThrows(WorkerNotFoundException.class, ()->workerService.getShiftsForWorker(workerId));
    }

    @Test
    public void getShiftsForWorkerPositiveTest() throws WorkerNotFoundException{
        //given
        Optional<Worker> workerOptional = Optional.of(new Worker("maciej", "a"));
        int workerId = 1;
        List<Shift> shiftList = new ArrayList<>();
        when(workerRepository.findById(anyInt())).thenReturn(workerOptional);
        when(worker.getShifts()).thenReturn(shiftList);
        int expectedSize = 0;
        //when
        List<Shift> shiftListExpected = workerService.getShiftsForWorker(workerId);
        //then
        Assertions.assertEquals(expectedSize, shiftListExpected.size());
    }

    @Test
    public void assignShiftToWorkerThrowsShiftNotFoundExceptionTest(){
        //given
        int workerId = 1;
        int shiftId = 1;
        Optional<Shift> shiftOptional = Optional.empty();
        when(shiftRepository.findById(shiftId)).thenReturn(shiftOptional);
        //then

        //when
        Assertions.assertThrows(ShiftNotFoundException.class, ()->workerService.assignShiftToWorker(workerId, shiftId));
    }

    @Test
    public void assignShiftToWorkerThrowsWorkerNotFoundExceptionExceptionTest(){
        //given
        int workerId = 1;
        int currentShiftId = 1;
        Shift shift = new Shift(currentShiftId, LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        Optional<Shift> shiftOptional = Optional.of(shift);
        Optional<Worker> workerOptional = Optional.empty();
        when(shiftRepository.findById(currentShiftId)).thenReturn(shiftOptional);
        when(workerRepository.findById(workerId)).thenReturn(workerOptional);
        //then

        //when
        Assertions.assertThrows(WorkerNotFoundException.class, ()->workerService.assignShiftToWorker(workerId, currentShiftId));
    }

    @Test
    public void assignShiftToWorkerWithoutAnyPreviousShiftsTest() throws ShiftNotFoundException, WorkerNotFoundException,
            TwoShiftsInRowException{
        //given
        int workerId = 1;
        int currentShiftId = 2;
        Shift currentShift = new Shift(currentShiftId, LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        Worker worker = new Worker("worker1", "worker1");
        Optional<Shift> shiftOptional = Optional.of(currentShift);
        Optional<Worker> workerOptional = Optional.of(worker);
        when(shiftRepository.findById(currentShiftId)).thenReturn(shiftOptional);
        when(workerRepository.findById(workerId)).thenReturn(workerOptional);
        //then
        workerService.assignShiftToWorker(workerId, currentShiftId);
        //when
        verify(shiftRepository, times(1)).save(any());
    }

    @Test
    public void assignShiftToWorkerTwoShiftsInARowTest(){
        //given
        int workerId = 1;
        int currentShiftId = 2;
        int previousShiftId = 1;
        Shift currentShift = new Shift(currentShiftId, LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        Shift previousShift = new Shift(previousShiftId, LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        Worker worker = new Worker("worker1", "worker1");
        worker.getShifts().add(previousShift);
        Optional<Shift> shiftOptional = Optional.of(currentShift);
        Optional<Worker> workerOptional = Optional.of(worker);
        when(shiftRepository.findById(currentShiftId)).thenReturn(shiftOptional);
        when(workerRepository.findById(workerId)).thenReturn(workerOptional);
        //then

        //when
        Assertions.assertThrows(TwoShiftsInRowException.class, ()->workerService.assignShiftToWorker(workerId, currentShiftId));
    }

    @Test
    public void assignShiftToWorkerInLessThanEightHoursTest(){
        //given
        int workerId = 1;
        int currentShiftId = 2;
        int previousShiftId = 1;
        Shift currentShift = new Shift(currentShiftId, LocalDateTime.of(2021,02,21,2,0),
                LocalDateTime.of(2021,02,21,10,0));
        Shift previousShift = new Shift(previousShiftId, LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,1));
        Worker worker = new Worker("worker1", "worker1");
        worker.getShifts().add(previousShift);
        Optional<Shift> shiftOptional = Optional.of(currentShift);
        Optional<Worker> workerOptional = Optional.of(worker);
        when(shiftRepository.findById(currentShiftId)).thenReturn(shiftOptional);
        when(workerRepository.findById(workerId)).thenReturn(workerOptional);
        //then

        //when
        Assertions.assertThrows(TwoShiftsInRowException.class, ()->workerService.assignShiftToWorker(workerId, currentShiftId));
    }

    @Test
    public void assignShiftToWorkerAfterEightHoursTest()throws ShiftNotFoundException, WorkerNotFoundException,
            TwoShiftsInRowException{
        //given
        int workerId = 1;
        int currentShiftId = 2;
        int previousShiftId = 1;
        Shift currentShift = new Shift(currentShiftId, LocalDateTime.of(2021,02,21,2,1),
                LocalDateTime.of(2021,02,21,10,0));
        Shift previousShift = new Shift(previousShiftId, LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,1));
        Worker worker = new Worker("worker1", "worker1");
        worker.getShifts().add(previousShift);
        Optional<Shift> shiftOptional = Optional.of(currentShift);
        Optional<Worker> workerOptional = Optional.of(worker);
        when(shiftRepository.findById(currentShiftId)).thenReturn(shiftOptional);
        when(workerRepository.findById(workerId)).thenReturn(workerOptional);
        //then
        workerService.assignShiftToWorker(workerId, currentShiftId);
        //when
        verify(shiftRepository, times(1)).save(any());
    }
}
