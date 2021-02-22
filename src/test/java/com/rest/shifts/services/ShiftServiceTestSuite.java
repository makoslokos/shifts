package com.rest.shifts.services;

import com.rest.shifts.common.ShiftAlreadyDefined;
import com.rest.shifts.common.ShiftDto;
import com.rest.shifts.domain.Shift;
import com.rest.shifts.mapper.ShiftsMapper;
import com.rest.shifts.repository.ShiftRepository;
import com.rest.shifts.repository.WorkerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ShiftServiceTestSuite {
    @MockBean
    ShiftRepository shiftRepository;
    @Autowired
    ShiftService shiftService;

    @Test
    public void addExistingShiftTest(){
        //given
        Shift shift = new Shift(LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        ShiftDto shiftDto = new ShiftDto(LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        when(shiftRepository.getShift(any(), any())).thenReturn(shift);
        when(shiftRepository.save(any())).thenReturn(shift);
        //when

        //then
        Assertions.assertThrows(ShiftAlreadyDefined.class, ()->shiftService.addShift(shiftDto));
    }

    @Test
    public void addNonExistingShiftTest(){
        //given
        Shift shift = null;
        ShiftDto shiftDto = new ShiftDto(LocalDateTime.of(2021,02,20,10,0),
                LocalDateTime.of(2021,02,20,18,0));
        when(shiftRepository.getShift(any(), any())).thenReturn(shift);
        when(shiftRepository.save(any())).thenReturn(shift);
        //when
        shiftService.addShift(shiftDto);
        //then
        verify(shiftRepository, times(1)).save(any());
    }
}
