package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entidades.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        System.out.println("===== SISTEMA DE GESTIÓN HOSPITALARIA con JPA & H2 =====\n");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalPU");
        EntityManager em = emf.createEntityManager();
        CitaManager citaManager = new CitaManager(em);

        try {
            // --- FASE 1: CREAR Y PERSISTIR DATOS ---
            em.getTransaction().begin();
            System.out.println("Creando y persistiendo la estructura inicial...");

            // 1. Crear Hospital
            Hospital hospital = Hospital.builder()
                    .nombre("Hospital Central")
                    .direccion("Av. Libertador 1234")
                    .telefono("011-4567-8901")
                    .build();

            // 2. Crear Departamentos
            Departamento cardiologia = Departamento.builder().nombre("Cardiología").especialidad(EspecialidadMedica.CARDIOLOGIA).build();
            Departamento pediatria = Departamento.builder().nombre("Pediatría").especialidad(EspecialidadMedica.PEDIATRIA).build();
            Departamento traumatologia = Departamento.builder().nombre("Traumatología").especialidad(EspecialidadMedica.TRAUMATOLOGIA).build();

            // 3. Crear Salas y asignarlas
            cardiologia.agregarSala(Sala.builder().numero("CARD-101").tipo("Consultorio").build());
            cardiologia.agregarSala(Sala.builder().numero("CARD-102").tipo("Quirófano").build());
            pediatria.agregarSala(Sala.builder().numero("PED-201").tipo("Consultorio").build());
            traumatologia.agregarSala(Sala.builder().numero("TRAUMA-301").tipo("Emergencias").build());

            // 4. Crear Médicos y asignarlos
            cardiologia.agregarMedico(Medico.builder().nombre("Carlos").apellido("González").dni("12345678").fechaNacimiento(LocalDate.of(1975, 5, 15)).tipoSangre(TipoSangre.A_POSITIVO).matricula(new Matricula("MP-12345")).especialidad(EspecialidadMedica.CARDIOLOGIA).build());
            pediatria.agregarMedico(Medico.builder().nombre("Ana").apellido("Martínez").dni("23456789").fechaNacimiento(LocalDate.of(1980, 8, 22)).tipoSangre(TipoSangre.O_NEGATIVO).matricula(new Matricula("MP-23456")).especialidad(EspecialidadMedica.PEDIATRIA).build());
            traumatologia.agregarMedico(Medico.builder().nombre("Luis").apellido("Rodríguez").dni("34567890").fechaNacimiento(LocalDate.of(1978, 3, 10)).tipoSangre(TipoSangre.B_POSITIVO).matricula(new Matricula("MP-34567")).especialidad(EspecialidadMedica.TRAUMATOLOGIA).build());

            // 5. Crear Pacientes y sus historias
            Paciente pacienteCardiaco = Paciente.builder().nombre("María").apellido("López").dni("11111111").fechaNacimiento(LocalDate.of(1985, 12, 5)).tipoSangre(TipoSangre.A_POSITIVO).telefono("011-1111-1111").direccion("Calle Falsa 123").build();
            HistoriaClinica hc1 = HistoriaClinica.builder().numeroHistoria("HC-11111111").fechaCreacion(LocalDateTime.now()).diagnostico("Hipertensión").tratamiento("Enalapril").alergia("Penicilina").build();
            pacienteCardiaco.setHistoriaClinica(hc1);

            Paciente pacientePediatrico = Paciente.builder().nombre("Pedro").apellido("García").dni("22222222").fechaNacimiento(LocalDate.of(2010, 6, 15)).tipoSangre(TipoSangre.O_POSITIVO).telefono("011-2222-2222").direccion("Av. Siempreviva 456").build();
            HistoriaClinica hc2 = HistoriaClinica.builder().numeroHistoria("HC-22222222").fechaCreacion(LocalDateTime.now()).diagnostico("Control de rutina").build();
            pacientePediatrico.setHistoriaClinica(hc2);

            // 6. Enlazar todo al hospital
            hospital.agregarDepartamento(cardiologia);
            hospital.agregarDepartamento(pediatria);
            hospital.agregarDepartamento(traumatologia);
            hospital.agregarPaciente(pacienteCardiaco);
            hospital.agregarPaciente(pacientePediatrico);

            // 7. Persistir el objeto "raíz" (Hospital). Gracias a CascadeType.ALL, todo lo demás se guardará.
            em.persist(hospital);

            // 8. Programar citas
            System.out.println("\nProgramando citas...");
            citaManager.programarCita(pacienteCardiaco, cardiologia.getMedicos().get(0), cardiologia.getSalas().get(0), LocalDateTime.now().plusDays(1), new BigDecimal("1500"));
            citaManager.programarCita(pacientePediatrico, pediatria.getMedicos().get(0), pediatria.getSalas().get(0), LocalDateTime.now().plusDays(2), new BigDecimal("800"));

            // Confirmar la transacción
            em.getTransaction().commit();
            System.out.println("Datos iniciales persistidos exitosamente.");

            // --- FASE 2: LEER Y MOSTRAR DATOS ---
            System.out.println("\n===== CONSULTANDO Y MOSTRANDO DATOS DESDE LA BD =====");

            // Buscamos el hospital por su ID para asegurarnos que leemos desde la BD
            Hospital hospitalDesdeDB = em.find(Hospital.class, hospital.getId());
            mostrarInformacionCompleta(hospitalDesdeDB, citaManager);

        } catch (Exception e) {
            System.err.println("Error en la transacción: " + e.getMessage());
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void mostrarInformacionCompleta(Hospital hospital, CitaManager citaManager) {
        System.out.println("\n--- Información del Hospital ---");
        System.out.println("Hospital: " + hospital.getNombre());

        System.out.println("\n--- Departamentos y Personal ---");
        for (Departamento dep : hospital.getDepartamentos()) {
            System.out.println("Departamento: " + dep.getNombre());
            dep.getMedicos().forEach(m -> System.out.println("  - Médico: " + m.getNombreCompleto()));
            dep.getSalas().forEach(s -> System.out.println("  - Sala: " + s.getNumero()));
        }

        System.out.println("\n--- Pacientes y Citas ---");
        for (Paciente p : hospital.getPacientes()) {
            System.out.println("Paciente: " + p.getNombreCompleto() + " (DNI: " + p.getDni() + ")");
            List<Cita> citas = citaManager.getCitasPorPaciente(p);
            if (citas.isEmpty()) {
                System.out.println("  - No tiene citas programadas.");
            } else {
                citas.forEach(c -> System.out.println("  - Cita el " + c.getFechaHora().toLocalDate() + " con Dr. " + c.getMedico().getApellido()));
            }
        }
    }
}