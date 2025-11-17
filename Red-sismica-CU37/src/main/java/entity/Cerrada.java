package entity;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CERRADA")
public class Cerrada extends EstadoOrdenInspeccion {

    public Cerrada() {
        this.nombre = "Cerrada";
    }

    @Override
    public void cerrar(LocalDateTime fechaHoraCierre, OrdenDeInspeccion orden) {
        // ya está cerrada, no se puede volver a cerrar
        throw new IllegalStateException("La orden ya está cerrada");
    }

    // no permitimos más transiciones desde acá
}