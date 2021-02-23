package com.rest.shifts.services;

import com.rest.shifts.common.ShiftAlreadyDefined;
import com.rest.shifts.common.ShiftDto;
import com.rest.shifts.domain.Shift;
import com.rest.shifts.mapper.ShiftsMapper;
import com.rest.shifts.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftsMapper shiftsMapper;

    public void addShift(ShiftDto shiftDto) throws ShiftAlreadyDefined {
        Shift shiftToAdd = shiftsMapper.mapToShift(shiftDto);
        Shift shiftWithTheSameStartingAndEndDateTime = shiftRepository.getShiftWithTheSameStartingAndEnd(shiftToAdd.getFrom(),
                shiftToAdd.getTo());
        if(isShiftDefined(shiftWithTheSameStartingAndEndDateTime)) {
            throw new ShiftAlreadyDefined();
        }
        shiftRepository.save(shiftToAdd);}

    private boolean isShiftDefined(Shift shiftWithTheSameStartingAndEndDateTime) {
        return shiftWithTheSameStartingAndEndDateTime != null;
    }

}
