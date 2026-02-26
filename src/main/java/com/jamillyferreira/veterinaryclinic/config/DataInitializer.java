package com.jamillyferreira.veterinaryclinic.config;

import com.jamillyferreira.veterinaryclinic.entity.Pet;
import com.jamillyferreira.veterinaryclinic.entity.Tutor;
import com.jamillyferreira.veterinaryclinic.entity.Veterinary;
import com.jamillyferreira.veterinaryclinic.enums.Specialty;
import com.jamillyferreira.veterinaryclinic.repository.PetRepository;
import com.jamillyferreira.veterinaryclinic.repository.TutorRepository;
import com.jamillyferreira.veterinaryclinic.repository.VeterinaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final VeterinaryRepository veterinaryRepository;
    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;

    @Override
    public void run(String... args) throws Exception {
        if (veterinaryRepository.count() == 0) {
            veterinaryRepository.saveAll(List.of(
                    new Veterinary("Dra. Mariana Oliveira", "12345-AM", "92-991234567", Specialty.CLINICO_GERAL),
                    new Veterinary("Dr. Lucas Andrade", "12346-AM", "92-991234568", Specialty.CLINICO_GERAL),
                    new Veterinary("Dra. Camila Rodrigues", "32345-AM", "92-991234571", Specialty.ORTOPEDIA),
                    new Veterinary("Dra. Juliana Nogueira", "42345-AM", "92-991234573", Specialty.CARDIOLOGIA),
                    new Veterinary("Dra. Beatriz Almeida", "52345-AM", "92-991234575", Specialty.ODONTOLOGIA),
                    new Veterinary("Dra. Larissa Mendes", "62345-AM", "92-991234577", Specialty.MEDICINA_FELINA)
            ));
        }

        if (tutorRepository.count() == 0) {
            Tutor tutor1 = new Tutor("Carlos Silva", "12345678901", "92999999999", "carlos@email.com");
            Tutor tutor2 = new Tutor("Ana Souza", "98765432100", "92988888888", "ana@email.com");
            tutorRepository.saveAll(List.of(tutor1, tutor2));

            if (petRepository.count() == 0) {
                petRepository.saveAll(List.of(
                        new Pet(
                                "Rex",
                                "CACHORRO",
                                "Labrador",
                                28.5,
                                LocalDate.of(2021, 5, 10),
                                tutor1
                        ),
                        new Pet(
                                "Mimi",
                                "GATO",
                                "Siamês",
                                4.2,
                                LocalDate.of(2022, 3, 15),
                                tutor2
                        ),
                        new Pet(
                                "Thor",
                                "CACHORRO",
                                "Pitbull",
                                32.0,
                                LocalDate.of(2020, 8, 20),
                                tutor1
                        )
                ));
            }
        }


    }
}
