package com.w11k.terranets.workshop.types;

public class TrainNotFoundException extends RuntimeException {
    public TrainNotFoundException(int id) {
        super("Could not find train with id " + id);
    }
}
