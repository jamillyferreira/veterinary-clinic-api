package com.jamillyferreira.veterinaryclinic.specification;

import com.jamillyferreira.veterinaryclinic.entity.Appointment;
import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

public final class AppointmentSpecification {
    private AppointmentSpecification() {}

    public static Specification<Appointment> withFilters(
            Long petId,
            Long veterinaryId,
            AppointmentStatus status
    ) {
        return Specification
                .where(hasPet(petId))
                .and(hasVeterinary(veterinaryId))
                .and(hasStatus(status)
        );
    }

    private static PredicateSpecification<Appointment> hasPet(Long petId) {
        return (root, cb) ->
                petId == null ? null : cb.equal(root.get("pet").get("id"), petId);
    }

    private static PredicateSpecification<Appointment> hasVeterinary(Long veterinaryId) {
        return (root, cb) ->
                veterinaryId == null ? null : cb.equal(root.get("veterinary").get("id"), veterinaryId);
    }

    private static PredicateSpecification<Appointment> hasStatus(AppointmentStatus status) {
        return (root, cb) ->
               status == null ? null : cb.equal(root.get("status"), status);
    }
}
