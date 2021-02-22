package com.rest.shifts.repository;

import com.rest.shifts.domain.Shift;
import com.rest.shifts.domain.Worker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;
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
        int id = shift.getId();
        //then
        Assertions.assertNotEquals(0, id);
        shiftRepository.deleteById(id);
    }

    @Test
    public void gettingShift(){
        //given
        Shift shift = new Shift(LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        //when
        shiftRepository.save(shift);
        int id = shift.getId();
        //then
        Assertions.assertEquals(id, shiftRepository.findById(id).get().getId());
        shiftRepository.deleteById(id);
    }

    @Test
    public void getShiftBetweenDates(){
        //given
        LocalDateTime from = LocalDateTime.of(2021,02,20,10,0);
        LocalDateTime to = LocalDateTime.of(2021,02,20,18,0);
        Shift shift = new Shift(from, to);
        //when
        shiftRepository.save(shift);
        int id = shift.getId();
        //then
        Assertions.assertEquals(id, shiftRepository.getShift(from, to).getId());
        shiftRepository.deleteById(id);
    }

}
