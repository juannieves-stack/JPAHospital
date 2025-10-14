package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HistoriaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroHistoria;
    private LocalDateTime fechaCreacion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ElementCollection // Para listas de tipos básicos (String)
    @CollectionTable(name="historia_diagnosticos", joinColumns=@JoinColumn(name="historia_id"))
    @Column(name="diagnostico")
    @Singular
    private List<String> diagnosticos ;

    @ElementCollection
    @CollectionTable(name="historia_tratamientos", joinColumns=@JoinColumn(name="historia_id"))
    @Column(name="tratamiento")
    @Singular
    private List<String> tratamientos ;

    @ElementCollection
    @CollectionTable(name="historia_alergias", joinColumns=@JoinColumn(name="historia_id"))
    @Column(name="alergia")
    @Singular
    private List<String> alergias;
}