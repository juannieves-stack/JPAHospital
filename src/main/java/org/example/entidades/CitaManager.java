package org.example.entidades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CitaManager {
    private EntityManager em;

    public CitaManager(EntityManager em) {
        this.em = em;
    }

    public Cita programarCita(Paciente paciente, Medico medico, Sala sala,
                              LocalDateTime fechaHora, BigDecimal costo) throws CitaException {
        // La validación de lógica de negocio se mantiene
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new CitaException("No se puede programar una cita en el pasado.");
        }
        if (costo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CitaException("El costo debe ser mayor que cero.");
        }

        // Creamos la cita
        Cita cita = Cita.builder()
                .paciente(paciente)
                .medico(medico)
                .sala(sala)
                .fechaHora(fechaHora)
                .costo(costo)
                .estado(EstadoCita.PROGRAMADA)
                .build();

        // Persistimos la cita
        em.persist(cita);
        return cita;
    }

    public List<Cita> getCitasPorPaciente(Paciente paciente) {
        TypedQuery<Cita> query = em.createQuery("SELECT c FROM Cita c WHERE c.paciente = :paciente", Cita.class);
        query.setParameter("paciente", paciente);
        return query.getResultList();
    }

    public List<Cita> getCitasPorMedico(Medico medico) {
        TypedQuery<Cita> query = em.createQuery("SELECT c FROM Cita c WHERE c.medico = :medico", Cita.class);
        query.setParameter("medico", medico);
        return query.getResultList();
    }

    public Sala findSalaByNumero(String numero) {
        try {
            return em.createQuery("SELECT s FROM Sala s WHERE s.numero = :numero", Sala.class)
                    .setParameter("numero", numero)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}