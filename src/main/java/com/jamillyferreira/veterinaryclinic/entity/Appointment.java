package com.jamillyferreira.veterinaryclinic.entity;

import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consultation")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "veterinary_id")
    private Veterinary veterinary;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private LocalDateTime scheduledAt;
    private String reason;

    @Column(nullable = false)
    private String diagnosis;

    private String observations;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.AGENDADA;
}
