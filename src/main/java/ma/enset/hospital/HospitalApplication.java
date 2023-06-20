package ma.enset.hospital;

import ma.enset.hospital.entities.*;
import ma.enset.hospital.metier.HospitalServiceImpl;
import ma.enset.hospital.metier.IHospitalService;
import ma.enset.hospital.repositories.ConsultationRepository;
import ma.enset.hospital.repositories.MedecinRepository;
import ma.enset.hospital.repositories.PatientRepository;
import ma.enset.hospital.repositories.RendezVousRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class HospitalApplication {
    private final ConsultationRepository consultationRepository;

    public HospitalApplication(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }
    @Bean
    CommandLineRunner start(
            PatientRepository patientRepository,
            MedecinRepository medecinRepository,
            RendezVousRepository rendezVousRepository,
            ConsultationRepository consultationRepository,
            IHospitalService hospitalService){
        return args ->{

            Stream.of("Mohamed","Hassan","Najat").forEach(
                    name->{
                        Patient patient = new Patient();
                        patient.setNom(name);
                        patient.setDateNaissance(new Date());
                        patient.setMalade(false);
                        hospitalService.savePatient(patient);
                    }
            );
            Stream.of("Aymen","hanane","yasmine").forEach(
                    name->{
                        Medecin medecin = new Medecin();
                        medecin.setNom(name);
                        medecin.setEmail(name+"@gmail.com");
                        medecin.setSpecialite(Math.random()>0.5?"Cardio":"Dentiste");
                        medecinRepository.save(medecin);
                    }
            );
            Patient patient =patientRepository.findById(1L).orElse(null);
            //Patient patient1 =patientRepository.findByNom("Mohamed");

            Medecin medecin = medecinRepository.findByNom("yasmine");

            RendezVous rendezVous = new RendezVous();
            rendezVous.setDate(new Date());
            rendezVous.setStatus(StatusRDV.PENDING);
            rendezVous.setPatient(patient);
            rendezVous.setMedecin(medecin);
            hospitalService.saveRDV(rendezVous);

            RendezVous rendezVous1 = rendezVousRepository.findAll().get(0);

            Consultation consultation = new Consultation();
            consultation.setDateConsultation(new Date());
            consultation.setRendezVous(rendezVous1);
            consultation.setRapport("Rapport 1");
            hospitalService.saveConsultation(consultation);

        };
    }
}
