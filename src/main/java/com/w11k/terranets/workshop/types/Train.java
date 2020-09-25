package com.w11k.terranets.workshop.types;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Train {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    private String model;
    private String locomotive;


    public Train() {
    }

    public Train(String model, String locomotive) {
        this.model = model;
        this.locomotive = locomotive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLocomotive() {
        return locomotive;
    }

    public void setLocomotive(String locomotive) {
        this.locomotive = locomotive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Train train = (Train) o;

        if (id != train.id) return false;
        if (!model.equals(train.model)) return false;
        return locomotive.equals(train.locomotive);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + model.hashCode();
        result = 31 * result + locomotive.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", locomotive='" + locomotive + '\'' +
                '}';
    }
}
