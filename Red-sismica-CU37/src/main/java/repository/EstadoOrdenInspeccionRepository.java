package repository;

import entity.EstadoOrdenInspeccion;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

public class EstadoOrdenInspeccionRepository {
    
    public List<EstadoOrdenInspeccion> findAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<EstadoOrdenInspeccion> query = em.createQuery(
                "SELECT e FROM EstadoOrdenInspeccion e", EstadoOrdenInspeccion.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<EstadoOrdenInspeccion> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            EstadoOrdenInspeccion estado = em.find(EstadoOrdenInspeccion.class, id);
            return Optional.ofNullable(estado);
        } finally {
            em.close();
        }
    }

    public Optional<EstadoOrdenInspeccion> findByNombre(String nombre) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<EstadoOrdenInspeccion> query = em.createQuery(
                "SELECT e FROM EstadoOrdenInspeccion e WHERE e.nombre = :nombre", 
                EstadoOrdenInspeccion.class);
            query.setParameter("nombre", nombre);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public EstadoOrdenInspeccion save(EstadoOrdenInspeccion estado) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (estado.getId() == null) {
                em.persist(estado);
            } else {
                estado = em.merge(estado);
            }
            tx.commit();
            return estado;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar EstadoOrdenInspeccion", e);
        } finally {
            em.close();
        }
    }
}
