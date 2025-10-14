package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private EspecialidadMedica especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Medico> medicos = new ArrayList<>();

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Sala> salas = new ArrayList<>();

    public void agregarMedico(Medico medico) {
        medicos.add(medico);
        medico.setDepartamento(this);
    }

    public void agregarSala(Sala sala) {
        salas.add(sala);
        sala.setDepartamento(this);
    }
}