package repository;

import entity.Estado;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

public class EstadoRepository {
    
    public List<Estado> findAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Estado> query = em.createQuery(
                "SELECT e FROM Estado e", Estado.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Estado> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            Estado estado = em.find(Estado.class, id);
            return Optional.ofNullable(estado);
        } finally {
            em.close();
        }
    }

    public Optional<Estado> findByAmbitoAndNombre(String ambito, String nombreEstado) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Estado> query = em.createQuery(
                "SELECT e FROM Estado e WHERE e.ambito = :ambito AND e.nombreEstado = :nombreEstado", 
                Estado.class);
            query.setParameter("ambito", ambito);
            query.setParameter("nombreEstado", nombreEstado);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Estado save(Estado estado) {
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
            throw new RuntimeException("Error al guardar Estado", e);
        } finally {
            em.close();
        }
    }
}
