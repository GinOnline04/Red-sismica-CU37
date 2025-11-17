package entity;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import repository.EntityManagerUtil;

@Entity
@DiscriminatorValue("COMPLETAMENTE_REALIZADA")
public class CompletamenteRealizada extends EstadoOrdenInspeccion {

    public CompletamenteRealizada() {
        this.nombre = "Completamente realizada";
    }

    @Override
    public void cerrar(LocalDateTime fechaHoraCierre, OrdenDeInspeccion orden) {
        // Buscar el estado "Cerrada" desde la BD
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            EstadoOrdenInspeccion estadoCerrada = em.createQuery(
                "SELECT e FROM EstadoOrdenInspeccion e WHERE e.nombre = :nombre", 
                EstadoOrdenInspeccion.class)
                .setParameter("nombre", "Cerrada")
                .getSingleResult();
            
            orden.setEstado(estadoCerrada);
            orden.setFechaHoraCierre(fechaHoraCierre);
        } finally {
            em.close();
        }
    }

    // sosCompletamenteRealizada()
    public boolean sosCompletamenteRealizada() {
        return true;
    }
}