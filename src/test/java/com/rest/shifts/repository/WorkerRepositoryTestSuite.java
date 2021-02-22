package com.rest.shifts.repository;

import com.rest.shifts.domain.Worker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WorkerRepositoryTestSuite {
    @Autowired
    private WorkerRepository workerRepository;

    @Test
    public void savingWorker(){
        //given
        Worker worker = new Worker("maciej", "ra");
        //when
        workerRepository.save(worker);
        int id = worker.getId();
        //then
        Assertions.assertNotEquals(0, id);
        workerRepository.deleteById(id);

    }
}
