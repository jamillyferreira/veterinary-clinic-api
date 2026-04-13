package com.jamillyferreira.veterinaryclinic.enums;

import java.util.Set;

public enum AppointmentStatus {
    AGENDADA {
        @Override
        public Set<AppointmentStatus> nextAllowed() {
            return Set.of(EM_ATENDIMENTO, CANCELADA);
        }
    },
    EM_ATENDIMENTO {
        @Override
        public Set<AppointmentStatus> nextAllowed() {
            return Set.of();
        }
    },
    CONCLUIDA {
        @Override
        public Set<AppointmentStatus> nextAllowed() {
            return Set.of();
        }
    },
    CANCELADA {
        @Override
        public Set<AppointmentStatus> nextAllowed() {
            return Set.of();
        }
    };

    public abstract Set<AppointmentStatus> nextAllowed();

    public boolean canTransitionTo(AppointmentStatus next) {
        return nextAllowed().contains(next);
    }

}
