package repository;

import entity.OrdenDeInspeccion;
import entity.Empleado;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

public class OrdenInspeccionRepository {
    
    public List<OrdenDeInspeccion> findAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<OrdenDeInspeccion> query = em.createQuery(
                "SELECT o FROM OrdenDeInspeccion o", OrdenDeInspeccion.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<OrdenDeInspeccion> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            OrdenDeInspeccion orden = em.find(OrdenDeInspeccion.class, id);
            return Optional.ofNullable(orden);
        } finally {
            em.close();
        }
    }

    public Optional<OrdenDeInspeccion> findByNumeroOrden(String numeroOrden) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<OrdenDeInspeccion> query = em.createQuery(
                "SELECT o FROM OrdenDeInspeccion o WHERE o.numeroOrden = :numeroOrden", 
                OrdenDeInspeccion.class);
            query.setParameter("numeroOrden", numeroOrden);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public List<OrdenDeInspeccion> findByEmpleadoAndCompletamenteRealizada(Empleado empleado) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<OrdenDeInspeccion> query = em.createQuery(
                "SELECT o FROM OrdenDeInspeccion o " +
                "WHERE o.empleado.id = :empleadoId " +
                "AND o.estado.nombreEstado = 'Completamente Realizada' " +
                "ORDER BY o.fechaHoraFinalizacion", 
                OrdenDeInspeccion.class);
            query.setParameter("empleadoId", empleado.getId());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public OrdenDeInspeccion save(OrdenDeInspeccion orden) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (orden.getId() == null) {
                em.persist(orden);
            } else {
                orden = em.merge(orden);
            }
            tx.commit();
            return orden;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar OrdenDeInspeccion", e);
        } finally {
            em.close();
        }
    }

    public void delete(OrdenDeInspeccion orden) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrdenDeInspeccion merged = em.merge(orden);
            em.remove(merged);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar OrdenDeInspeccion", e);
        } finally {
            em.close();
        }
    }
}
