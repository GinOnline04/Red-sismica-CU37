package repository;

import entity.Usuario;
import jakarta.persistence.*;
import java.util.Optional;

public class UsuarioRepository {
    
    public Optional<Usuario> findById(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        } finally {
            em.close();
        }
    }

    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario", 
                Usuario.class);
            query.setParameter("nombreUsuario", nombreUsuario);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public Usuario save(Usuario usuario) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (usuario.getId() == null) {
                em.persist(usuario);
            } else {
                usuario = em.merge(usuario);
            }
            tx.commit();
            return usuario;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar Usuario", e);
        } finally {
            em.close();
        }
    }
}
