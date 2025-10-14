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
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String telefono;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Departamento> departamentos = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Paciente> pacientes = new ArrayList<>();

    public void agregarDepartamento(Departamento departamento) {
        departamentos.add(departamento);
        departamento.setHospital(this);
    }

    public void agregarPaciente(Paciente paciente) {
        pacientes.add(paciente);
        paciente.setHospital(this);
    }
}
