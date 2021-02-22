package com.rest.shifts.services;

import com.rest.shifts.common.ShiftAlreadyDefined;
import com.rest.shifts.common.ShiftDto;
import com.rest.shifts.domain.Shift;
import com.rest.shifts.mapper.ShiftsMapper;
import com.rest.shifts.repository.ShiftRepository;
import com.rest.shifts.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftsMapper shiftsMapper;

    public void addShift(ShiftDto shiftDto) throws ShiftAlreadyDefined {
        Shift shift = shiftsMapper.mapToShift(shiftDto);
        Shift existingShift = shiftRepository.getShift(shift.getFrom(), shift.getTo());
        if(existingShift != null) {
            throw new ShiftAlreadyDefined();
        }
        shiftRepository.save(shift);}

}
