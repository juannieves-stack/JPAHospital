package org.example.entidades;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // Esta clase se "incrusta" dentro de otra entidad (Medico)
public class Matricula implements Serializable {
    private String numero;

    // La validación se puede hacer en el setter o en un método de fábrica si es necesario
    // Por simplicidad, la quitamos del constructor para compatibilidad con JPA
}
