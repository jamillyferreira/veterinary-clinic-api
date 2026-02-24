package com.jamillyferreira.veterinaryclinic.repository;

import com.jamillyferreira.veterinaryclinic.entity.Veterinary;
import com.jamillyferreira.veterinaryclinic.enums.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeterinaryRepository extends JpaRepository<Veterinary, Long> {
    List<Veterinary> findBySpecialty(Specialty specialty);

    boolean existsByCrmv(String crmv);

  
}
