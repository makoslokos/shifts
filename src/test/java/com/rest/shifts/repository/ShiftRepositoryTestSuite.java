package com.rest.shifts.repository;

import com.rest.shifts.domain.Shift;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ShiftRepositoryTestSuite {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @Test
    public void savingShift(){
        //given
        Shift shift = new Shift(LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        //when
        shiftRepository.save(shift);
        int shiftId = shift.getId();
        //then
        Assertions.assertNotEquals(0, shiftId);
        shiftRepository.deleteById(shiftId);
    }

    @Test
    public void gettingShift(){
        //given
        Shift shift = new Shift(LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        //when
        shiftRepository.save(shift);
        int shiftId = shift.getId();
        //then
        Assertions.assertEquals(shiftId, shiftRepository.findById(shiftId).get().getId());
        shiftRepository.deleteById(shiftId);
    }

    @Test
    public void getShiftBetweenDates(){
        //given
        LocalDateTime from = LocalDateTime.of(2021,02,20,10,0);
        LocalDateTime to = LocalDateTime.of(2021,02,20,18,0);
        Shift shift = new Shift(from, to);
        //when
        shiftRepository.save(shift);
        int shiftId = shift.getId();
        //then
        Assertions.assertEquals(shiftId, shiftRepository.getShiftWithTheSameStartingAndEnd(from, to).getId());
        shiftRepository.deleteById(shiftId);
    }

}
