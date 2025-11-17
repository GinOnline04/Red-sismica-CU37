package repository;

import entity.Empleado;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

public class EmpleadoRepository {
    
    public List<Empleado> findAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Empleado> query = em.createQuery(
                "SELECT e FROM Empleado e", Empleado.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Empleado> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            Empleado empleado = em.find(Empleado.class, id);
            return Optional.ofNullable(empleado);
        } finally {
            em.close();
        }
    }

    public List<Empleado> findResponsablesReparacion() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Empleado> query = em.createQuery(
                "SELECT e FROM Empleado e " +
                "WHERE e.rol.nombre = 'ResponsableReparacion'", 
                Empleado.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Empleado save(Empleado empleado) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (empleado.getId() == null) {
                em.persist(empleado);
            } else {
                empleado = em.merge(empleado);
            }
            tx.commit();
            return empleado;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar Empleado", e);
        } finally {
            em.close();
        }
    }
}
