package com.jamillyferreira.veterinaryclinic.config;

import com.jamillyferreira.veterinaryclinic.entity.Veterinary;
import com.jamillyferreira.veterinaryclinic.enums.Specialty;
import com.jamillyferreira.veterinaryclinic.repository.VeterinaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final VeterinaryRepository veterinaryRepository;

    @Override
    public void run(String... args) throws Exception {
        veterinaryRepository.saveAll(List.of(
                new Veterinary("Dra. Mariana Oliveira", "12345-AM", "92-991234567", Specialty.CLINICO_GERAL),
                new Veterinary("Dr. Lucas Andrade", "12346-AM", "92-991234568", Specialty.CLINICO_GERAL),
                new Veterinary("Dra. Camila Rodrigues", "32345-AM", "92-991234571", Specialty.ORTOPEDIA),
                new Veterinary("Dra. Juliana Nogueira", "42345-AM", "92-991234573", Specialty.CARDIOLOGIA),
                new Veterinary("Dra. Beatriz Almeida", "52345-AM", "92-991234575", Specialty.ODONTOLOGIA),
                new Veterinary("Dra. Larissa Mendes", "62345-AM", "92-991234577", Specialty.MEDICINA_FELINA)
        ));
    }
}
