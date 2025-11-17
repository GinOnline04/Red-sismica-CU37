package entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "estados_orden_inspeccion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_estado", discriminatorType = DiscriminatorType.STRING)
public abstract class EstadoOrdenInspeccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    protected String nombre;

    // Constructor sin argumentos para JPA
    public EstadoOrdenInspeccion() {
    }

    public EstadoOrdenInspeccion(String nombre) {
        this.nombre = nombre;
    }

    // === Getters ===

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract void cerrar(LocalDateTime fechaHoraCierre, OrdenDeInspeccion orden);

    // Estos pueden tener implementación vacía por defecto y sobrescribir solo donde
    // aplique
    public void pasarAPendienteDeRealizacion(OrdenDeInspeccion orden) {
        throw new UnsupportedOperationException("No se puede pasar a Pendiente desde " + nombre);
    }

    public void pasarAParcialmenteRealizada(OrdenDeInspeccion orden) {
        throw new UnsupportedOperationException("No se puede pasar a Parcial desde " + nombre);
    }

    public void pasarACompletamenteRealizada(OrdenDeInspeccion orden) {
        throw new UnsupportedOperationException("No se puede pasar a Completa desde " + nombre);
    }

    // sosCompletamenteRealizada()
    public boolean sosCompletamenteRealizada() {
        return false;
    }
}