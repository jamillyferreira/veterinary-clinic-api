package com.jamillyferreira.veterinaryclinic.repository;

import com.jamillyferreira.veterinaryclinic.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
    boolean existsByTutorId(Long tutorId);
}
