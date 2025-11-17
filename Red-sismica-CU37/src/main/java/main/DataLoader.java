package main;

import entity.*;
import jakarta.persistence.EntityManager;
import repository.EntityManagerUtil;
import java.time.LocalDateTime;

public class DataLoader {

        public static void loadInitialData() {
                EntityManager em = EntityManagerUtil.getEntityManager();

                try {
                        // Verificar si ya existen datos
                        Long count = em.createQuery("SELECT COUNT(r) FROM Rol r", Long.class).getSingleResult();
                        if (count > 0) {
                                System.out.println(
                                                "✓ Datos ya existentes en la base de datos. No se cargarán datos iniciales.");
                                em.close();
                                return;
                        }

                        em.getTransaction().begin();

                        // ROLES
                        Rol rolResponsable = new Rol("ResponsableReparacion",
                                        "Encargado de coordinar y realizar reparaciones del sismógrafo");
                        Rol rolTecnico = new Rol("Tecnico", "Encargado de tareas técnicas generales");
                        Rol rolSupervisor = new Rol("Supervisor", "Supervisa las tareas de inspección y mantenimiento");
                        Rol rolAdministrador = new Rol("Administrador",
                                        "Gestiona usuarios y configuraciones del sistema");

                        em.persist(rolResponsable);
                        em.persist(rolTecnico);
                        em.persist(rolSupervisor);
                        em.persist(rolAdministrador);

                        // ESTADOS
                        Estado estadoCompletamenteRealizada = new Estado("OrdenInspeccion", "Completamente Realizada");
                        Estado estadoCerrada = new Estado("OrdenInspeccion", "Cerrada");
                        Estado estadoFueraServicio = new Estado("Sismografo", "Fuera de Servicio");
                        Estado estadoEnServicio = new Estado("Sismografo", "En Servicio");
                        Estado estadoEnProceso = new Estado("OrdenInspeccion", "En Proceso");

                        em.persist(estadoCompletamenteRealizada);
                        em.persist(estadoCerrada);
                        em.persist(estadoFueraServicio);
                        em.persist(estadoEnServicio);
                        em.persist(estadoEnProceso);

                        // EMPLEADOS
                        Empleado lucia = new Empleado("Lucía", "Gómez", "lucia.gomez@sismo.gob.ar", "3511234567",
                                        rolResponsable);
                        Empleado martin = new Empleado("Martín", "Fernández", "martin.fernandez@sismo.gob.ar",
                                        "3512345678",
                                        rolResponsable);
                        Empleado sofia = new Empleado("Sofía", "López", "sofia.lopez@sismo.gob.ar", "3513456789",
                                        rolResponsable);
                        Empleado carlos = new Empleado("Carlos", "Ramírez", "carlos.ramirez@sismo.gob.ar", "3514567890",
                                        rolSupervisor);
                        Empleado ana = new Empleado("Ana", "Torres", "ana.torres@sismo.gob.ar", "3515678901",
                                        rolAdministrador);

                        em.persist(lucia);
                        em.persist(martin);
                        em.persist(sofia);
                        em.persist(carlos);
                        em.persist(ana);

                        // USUARIOS
                        Usuario usuarioLucia = new Usuario("lucia.g", "pass123", lucia);
                        Usuario usuarioMartin = new Usuario("martin.f", "mantenimiento123", martin);

                        em.persist(usuarioLucia);
                        em.persist(usuarioMartin);

                        // CAMBIOS DE ESTADO (estados iniciales para sismógrafos)
                        CambioEstado cambio1 = new CambioEstado(LocalDateTime.of(2024, 10, 1, 8, 0), null, lucia,
                                        estadoEnServicio);
                        CambioEstado cambio2 = new CambioEstado(LocalDateTime.of(2024, 10, 5, 9, 30), null, martin,
                                        estadoEnServicio);
                        CambioEstado cambio3 = new CambioEstado(LocalDateTime.of(2024, 10, 10, 14, 15), null, sofia,
                                        estadoEnServicio);

                        em.persist(cambio1);
                        em.persist(cambio2);
                        em.persist(cambio3);

                        // SISMÓGRAFOS (necesitamos crear Date desde LocalDateTime)
                        Sismografo sis1 = new Sismografo(java.sql.Date.valueOf("2020-06-15"), "SIS-001", 1001, null,
                                        cambio1,
                                        estadoEnServicio);
                        Sismografo sis2 = new Sismografo(java.sql.Date.valueOf("2021-09-10"), "SIS-002", 1002, null,
                                        cambio2,
                                        estadoEnServicio);
                        Sismografo sis3 = new Sismografo(java.sql.Date.valueOf("2022-01-25"), "SIS-003", 1003, null,
                                        cambio3,
                                        estadoEnServicio);

                        em.persist(sis1);
                        em.persist(sis2);
                        em.persist(sis3);

                        // ESTACIONES SISMOLÓGICAS
                        EstacionSismologica estacion1 = new EstacionSismologica(
                                        101, "Cert-2023-0001", java.sql.Date.valueOf("2023-05-10"),
                                        -34.6037, -58.3816, "Estacion Centro", 5001, sis1);
                        EstacionSismologica estacion2 = new EstacionSismologica(
                                        102, "Cert-2023-0002", java.sql.Date.valueOf("2024-01-20"),
                                        -33.4489, -70.6693, "Estacion Norte", 5002, sis2);
                        EstacionSismologica estacion3 = new EstacionSismologica(
                                        103, "Cert-2023-0003", java.sql.Date.valueOf("2023-11-15"),
                                        -31.4201, -64.1888, "Estacion Sur", 5003, sis3);

                        em.persist(estacion1);
                        em.persist(estacion2);
                        em.persist(estacion3);

                        // Actualizar los sismógrafos con la referencia a las estaciones (usar setter si
                        // existe)
                        estacion1.setSismografo(sis1);
                        estacion2.setSismografo(sis2);
                        estacion3.setSismografo(sis3);

                        // === NUEVOS: Estados de Orden de Inspección (tabla 'estados_orden_inspeccion')
                        // ===
                        EstadoOrdenInspeccion estadoOICompletamenteRealizada = new CompletamenteRealizada();
                        EstadoOrdenInspeccion estadoOICerrada = new Cerrada();
                        EstadoOrdenInspeccion estadoOIPendiente = new PendienteDeRealizacion();
                        EstadoOrdenInspeccion estadoOIParcial = new ParcialmenteRealizada();

                        em.persist(estadoOICompletamenteRealizada);
                        em.persist(estadoOICerrada);
                        em.persist(estadoOIPendiente);
                        em.persist(estadoOIParcial);

                        // ÓRDENES DE INSPECCIÓN (usar EstadoOrdenInspeccion)
                        OrdenDeInspeccion orden1 = new OrdenDeInspeccion(
                                        "ORD-0001",
                                        LocalDateTime.of(2025, 1, 10, 9, 30),
                                        LocalDateTime.of(2025, 5, 12, 16, 0),
                                        martin, estacion1, estadoOICompletamenteRealizada);
                        OrdenDeInspeccion orden2 = new OrdenDeInspeccion(
                                        "ORD-0002",
                                        LocalDateTime.of(2025, 2, 11, 14, 0),
                                        LocalDateTime.of(2025, 4, 11, 14, 0),
                                        lucia, estacion1, estadoOICompletamenteRealizada);
                        OrdenDeInspeccion orden3 = new OrdenDeInspeccion(
                                        "ORD-0003",
                                        LocalDateTime.of(2025, 4, 11, 14, 0),
                                        LocalDateTime.of(2025, 5, 11, 14, 0),
                                        lucia, estacion2, estadoOICompletamenteRealizada);
                        OrdenDeInspeccion orden4 = new OrdenDeInspeccion(
                                        "ORD-0004",
                                        LocalDateTime.of(2025, 3, 15, 10, 0),
                                        LocalDateTime.of(2025, 6, 20, 17, 0),
                                        lucia, estacion3, estadoOICompletamenteRealizada);
                        OrdenDeInspeccion orden5 = new OrdenDeInspeccion(
                                        "ORD-0005",
                                        LocalDateTime.of(2025, 5, 1, 8, 0),
                                        LocalDateTime.of(2025, 7, 15, 18, 30),
                                        lucia, estacion1, estadoOICompletamenteRealizada);
                        OrdenDeInspeccion orden6 = new OrdenDeInspeccion(
                                        "ORD-0006",
                                        LocalDateTime.of(2025, 6, 10, 10, 15),
                                        LocalDateTime.of(2025, 8, 5, 16, 45),
                                        martin, estacion2, estadoOICompletamenteRealizada);
                        OrdenDeInspeccion orden7 = new OrdenDeInspeccion(
                                        "ORD-0007",
                                        LocalDateTime.of(2025, 7, 20, 9, 0),
                                        LocalDateTime.of(2025, 9, 10, 15, 0),
                                        lucia, estacion3, estadoOICompletamenteRealizada);
                        OrdenDeInspeccion orden8 = new OrdenDeInspeccion(
                                        "ORD-0008",
                                        LocalDateTime.of(2025, 8, 12, 11, 30),
                                        LocalDateTime.of(2025, 10, 18, 17, 20),
                                        sofia, estacion1, estadoOICompletamenteRealizada);

                        em.persist(orden1);
                        em.persist(orden2);
                        em.persist(orden3);
                        em.persist(orden4);
                        em.persist(orden5);
                        em.persist(orden6);
                        em.persist(orden7);
                        em.persist(orden8);

                        em.getTransaction().commit();

                        System.out.println("✓ Datos iniciales cargados correctamente");

                } catch (Exception e) {
                        if (em.getTransaction().isActive()) {
                                em.getTransaction().rollback();
                        }
                        System.err.println("Error cargando datos iniciales: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Error al cargar datos iniciales", e);
                } finally {
                        em.close();
                }
        }
}
