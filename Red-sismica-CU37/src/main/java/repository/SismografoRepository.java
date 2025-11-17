package repository;

import entity.Sismografo;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

public class SismografoRepository {
    
    public List<Sismografo> findAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Sismografo> query = em.createQuery(
                "SELECT s FROM Sismografo s", Sismografo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Sismografo> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            Sismografo sismografo = em.find(Sismografo.class, id);
            return Optional.ofNullable(sismografo);
        } finally {
            em.close();
        }
    }

    public Optional<Sismografo> findByIdentificador(String identificador) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Sismografo> query = em.createQuery(
                "SELECT s FROM Sismografo s WHERE s.identificadorSismografo = :identificador", 
                Sismografo.class);
            query.setParameter("identificador", identificador);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Sismografo save(Sismografo sismografo) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (sismografo.getId() == null) {
                em.persist(sismografo);
            } else {
                sismografo = em.merge(sismografo);
            }
            tx.commit();
            return sismografo;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar Sismografo", e);
        } finally {
            em.close();
        }
    }
}
