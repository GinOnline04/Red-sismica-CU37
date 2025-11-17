package repository;

import entity.*;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

public class EntityManagerUtil {
    private static final String PERSISTENCE_UNIT_NAME = "red-sismica-pu";
    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
