package com.w11k.terranets.workshop.train;

import com.w11k.terranets.workshop.types.Train;
import com.w11k.terranets.workshop.types.TrainNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class TrainsController {

    private final TrainRepository trainsRepo;

    TrainsController(TrainRepository trainRepository) {
        this.trainsRepo = trainRepository;
    }

    @GetMapping("/api/train")
    public List<Train> getTrains() {
        return this.trainsRepo.findAll();
    }

    @PostMapping("/api/train")
    public Train addTrain(@RequestBody Train train) {
        return this.trainsRepo.save(train);
    }

    @GetMapping("/api/train/{id}")
    public Train getTrainById(@PathVariable int id) {
        return this.trainsRepo.findById(id)
                .orElseThrow(() -> new TrainNotFoundException(id));
    }

    @PutMapping("/api/train/{id}")
    public Train updateTrain(@RequestBody Train train, @PathVariable int id) {
        return this.trainsRepo.findById(id).map(t -> {
            t.setLocomotive(train.getLocomotive());
            t.setModel(train.getModel());
            return this.trainsRepo.save(t);
        }).orElseThrow(() -> new TrainNotFoundException(id));
    }

    @DeleteMapping
    public void deleteTrainById(@PathVariable int id) {
        this.trainsRepo.deleteById(id);
    }
}
