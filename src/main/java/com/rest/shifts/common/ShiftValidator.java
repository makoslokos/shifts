package com.rest.shifts.common;

import org.springframework.stereotype.Component;

@Component
public class ShiftValidator {
    private boolean validateIfShiftDatesAreNotNull(ShiftDto shiftDto) {
        return shiftDto.getFrom()!=null && shiftDto.getTo()!=null;
    }

    private boolean validateIfShiftToIsLaterThanFrom(ShiftDto shiftDto){
        return shiftDto.getTo().isAfter(shiftDto.getFrom());
    }

    public boolean validateShift(ShiftDto shiftDto){
        return validateIfShiftDatesAreNotNull(shiftDto) && validateIfShiftToIsLaterThanFrom(shiftDto);
    }
}
