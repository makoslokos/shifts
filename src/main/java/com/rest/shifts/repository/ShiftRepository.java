package com.rest.shifts.repository;

import com.rest.shifts.domain.Shift;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends CrudRepository<Shift, Integer> {
    @Override
    List<Shift> findAll();
    @Override
    Shift save(Shift shift);
    @Query
    Shift getShiftWithTheSameStartingAndEnd(@Param("STARTING_DATETIME")LocalDateTime from,
                                            @Param("END_DATETIME") LocalDateTime to);
}
