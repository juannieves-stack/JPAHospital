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
public class Paciente extends Persona {
    private String telefono;
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private HistoriaClinica historiaClinica;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Cita> citas = new ArrayList<>();

    public void addCita(Cita cita) {
        citas.add(cita);
        cita.setPaciente(this);
    }

    // Método para sincronizar la relación bidireccional con HistoriaClinica
    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
        if (historiaClinica != null) {
            historiaClinica.setPaciente(this);
        }
    }
}