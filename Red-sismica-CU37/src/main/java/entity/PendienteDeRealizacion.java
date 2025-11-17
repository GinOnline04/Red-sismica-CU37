package entity;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import repository.EntityManagerUtil;

@Entity
@DiscriminatorValue("PENDIENTE_DE_REALIZACION")
public class PendienteDeRealizacion extends EstadoOrdenInspeccion {

    public PendienteDeRealizacion() {
        this.nombre = "Pendiente de realización";
    }

    @Override
    public void cerrar(LocalDateTime fechaHoraCierre, OrdenDeInspeccion orden) {
        // Buscar el estado "Completamente realizada" desde la BD
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            EstadoOrdenInspeccion estadoCompleta = em.createQuery(
                "SELECT e FROM EstadoOrdenInspeccion e WHERE e.nombre = :nombre", 
                EstadoOrdenInspeccion.class)
                .setParameter("nombre", "Completamente realizada")
                .getSingleResult();
            
            orden.setFechaHoraCierre(fechaHoraCierre);
            orden.setEstado(estadoCompleta);
        } finally {
            em.close();
        }
    }

    @Override
    public void pasarAParcialmenteRealizada(OrdenDeInspeccion orden) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            EstadoOrdenInspeccion estadoParcial = em.createQuery(
                "SELECT e FROM EstadoOrdenInspeccion e WHERE e.nombre = :nombre", 
                EstadoOrdenInspeccion.class)
                .setParameter("nombre", "Parcialmente realizada")
                .getSingleResult();
            orden.setEstado(estadoParcial);
        } finally {
            em.close();
        }
    }

    @Override
    public void pasarACompletamenteRealizada(OrdenDeInspeccion orden) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            EstadoOrdenInspeccion estadoCompleta = em.createQuery(
                "SELECT e FROM EstadoOrdenInspeccion e WHERE e.nombre = :nombre", 
                EstadoOrdenInspeccion.class)
                .setParameter("nombre", "Completamente realizada")
                .getSingleResult();
            orden.setEstado(estadoCompleta);
        } finally {
            em.close();
        }
    }
}