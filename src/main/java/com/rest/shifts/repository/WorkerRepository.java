package com.rest.shifts.repository;

import com.rest.shifts.domain.Worker;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Integer> {
    @Override
    List<Worker> findAll();
    @Override
    Worker save(Worker worker);
}
