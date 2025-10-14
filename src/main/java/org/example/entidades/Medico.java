package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Medico extends Persona {
    @Embedded // Incrustamos la clase Matricula aquí
    private Matricula matricula;

    @Enumerated(EnumType.STRING)
    private EspecialidadMedica especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Cita> citas = new ArrayList<>();

    public void addCita(Cita cita) {
        citas.add(cita);
        cita.setMedico(this);
    }
}