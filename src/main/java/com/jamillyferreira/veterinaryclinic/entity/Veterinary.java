package com.jamillyferreira.veterinaryclinic.entity;

import com.jamillyferreira.veterinaryclinic.enums.Specialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "veterinary")
public class Veterinary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String crmv;

    @Column(nullable = false)
    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialty specialty;

    @OneToMany(mappedBy = "veterinary", fetch = FetchType.LAZY)
    private List<Consultation> consultations = new ArrayList<>();

    @Column(nullable = false)
    private boolean active = true;

    public Veterinary(String name, String crmv, String contact, Specialty specialty) {
        this.name = name;
        this.crmv = crmv;
        this.contact = contact;
        this.specialty = specialty;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Veterinary that = (Veterinary) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
