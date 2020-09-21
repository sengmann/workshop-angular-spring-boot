package com.w11k.terranets.workshop.train;

import com.w11k.terranets.workshop.types.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Integer> {
}
