package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "dni")
@MappedSuperclass // No es una tabla, sus campos se heredan a las entidades hijas
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Estrategia de herencia
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    protected String nombre;

    @Column(nullable = false)
    protected String apellido;

    @Column(unique = true, nullable = false)
    protected String dni;

    protected LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    protected TipoSangre tipoSangre;

    @Transient // Este campo no se persiste en la BD
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Transient // Este campo no se persiste en la BD
    public int getEdad() {
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }
}
