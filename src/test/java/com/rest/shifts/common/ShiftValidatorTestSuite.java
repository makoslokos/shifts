package com.rest.shifts.common;

import com.rest.shifts.domain.Shift;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ShiftValidatorTestSuite {
    @Autowired
    ShiftValidator shiftValidator;

    @Test
    public void validateShiftsDataPositiveTest(){
        //given
        ShiftDto shiftDto = new ShiftDto(LocalDateTime.of(2021,02,21,10,0),
                LocalDateTime.of(2021,02,22,2,0));
        //when

        //then
        Assertions.assertTrue(shiftValidator.validateShift(shiftDto));
    }

    @Test
    public void validateShiftsDataNegativeToDateBeforeFromTest(){
        //given
        ShiftDto shiftDto = new ShiftDto(LocalDateTime.of(2021,02,21,2,0),
                LocalDateTime.of(2021,02,20,10,0));
        //when

        //then
        Assertions.assertFalse(shiftValidator.validateShift(shiftDto));
    }

    @Test
    public void validateShiftsDataNegativeOneDateIsNullTest(){
        //given
        ShiftDto shiftDto = new ShiftDto(LocalDateTime.of(2021,02,21,2,0), null);
        //when

        //then
        Assertions.assertFalse(shiftValidator.validateShift(shiftDto));
    }
}
