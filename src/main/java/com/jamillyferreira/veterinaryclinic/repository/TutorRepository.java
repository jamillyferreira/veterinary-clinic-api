package com.jamillyferreira.veterinaryclinic.repository;

import com.jamillyferreira.veterinaryclinic.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByCpf(String cpf);
}
