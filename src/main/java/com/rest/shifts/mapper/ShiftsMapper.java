package com.rest.shifts.mapper;

import com.rest.shifts.domain.Shift;
import com.rest.shifts.common.ShiftDto;
import org.springframework.stereotype.Service;

@Service
public class ShiftsMapper {
    public ShiftDto mapToDto(Shift shift){
        return new ShiftDto(
                shift.getId(),
                shift.getFrom(),
                shift.getTo()
        );
    }

    public Shift mapToShift(ShiftDto shiftDto){
        Shift shift = new Shift(
                shiftDto.getId(),
                shiftDto.getFrom(),
                shiftDto.getTo()
        );
        shift.setWorkerList(shiftDto.getWorkerList());
        return shift;
    }
}
