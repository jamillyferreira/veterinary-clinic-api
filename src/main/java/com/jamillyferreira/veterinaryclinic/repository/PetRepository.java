package com.jamillyferreira.veterinaryclinic.repository;

import com.jamillyferreira.veterinaryclinic.entity.Pet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    boolean existsByTutorId(Long tutorId);

    List<Pet> findByTutorId(Long tutorId);

    @EntityGraph(attributePaths = "tutor")
    List<Pet> findAll();
}
