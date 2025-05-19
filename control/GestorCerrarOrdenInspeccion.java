package control;

import boundary.*;
import entity.*;

import java.time.LocalDateTime;
import java.util.*;

public class GestorCerrarOrdenInspeccion {
    // Atributos
    private Empleado empleadoLogueado;
    private List<OrdenDeInspeccion> ordenesCompletamenteRealizadas;
    private List<OrdenDeInspeccion> todasLasOrdenes; // este atributo es solo para que no salte error, despues hay que cambiarlo cuando tengamos la BD.
    private Sesion sesionActual;
    private InterfazCerrarOrdenInspeccion interfaz;
    private InterfazNotificacionMail interfazNotificacionMail;
    private InterfazMonitorCCRS interfazMonitorCCRS;
    private OrdenDeInspeccion ordenInspeccionSeleccionada;
    private String observacionCierreOrdenInspeccion;
    private List<MotivoTipo> motivos;
    private List<MotivoTipo> motivosTipoSeleccionados;
    private List<String> comentarios;
    private List<Estado> estados;
    private List<Sismografo> sismografos;
    private List<Empleado> empleados;
    private LocalDateTime fechaHoraFinalizacion;

    public GestorCerrarOrdenInspeccion(InterfazCerrarOrdenInspeccion interfaz,
                                       InterfazNotificacionMail interfazNotificacionMail,
                                       InterfazMonitorCCRS interfazMonitorCCRS) {
        this.interfaz = interfaz;
        this.interfazNotificacionMail = interfazNotificacionMail;
        this.interfazMonitorCCRS = interfazMonitorCCRS;
        this.motivosTipoSeleccionados = new ArrayList<>();
        this.comentarios = new ArrayList<>();
    }

    // Métodos

    public void iniciarCierreOrdenInspeccion() {

        // GUARDAMOS DATOS MOCK
        generardatos();
        // ARRANCAMOS EL FLUJO
        this.empleadoLogueado = buscarEmpleadoLogueado();
        this.ordenesCompletamenteRealizadas = buscarOrdenesInspeccionDeRI(empleadoLogueado, todasLasOrdenes);
        ordenarPorFechaFinalizacion(this.ordenesCompletamenteRealizadas);
        interfaz.pedirSeleccionOrdenInspeccion(this.ordenesCompletamenteRealizadas);
    }

    private Empleado buscarEmpleadoLogueado() {
        return this.sesionActual.obtenerEmpleadoLogueado();
    }

    private List<OrdenDeInspeccion> buscarOrdenesInspeccionDeRI(Empleado empleado, List<OrdenDeInspeccion> todasLasOrdenes) {
        List<OrdenDeInspeccion> ordenesFiltradas = new ArrayList<>();

        for (OrdenDeInspeccion orden : todasLasOrdenes) {
            if (orden.sosDeEmpleado(empleado) && orden.sosCompletamenteRealizada()) {
                ordenesFiltradas.add(orden);
            }
        }

        return ordenesFiltradas;
    }


    private void ordenarPorFechaFinalizacion(List<OrdenDeInspeccion> ordenesFiltradas) {
        ordenesFiltradas.sort(Comparator.comparing(OrdenDeInspeccion::getFechaFinalizacion));
    }

    public void tomarOrdenInspeccionSeleccionada(String numeroOrden) {
        for (OrdenDeInspeccion orden : ordenesCompletamenteRealizadas) {
            if (orden.getNumeroDeOrdenDeInspeccion().equals(numeroOrden)) {
                this.ordenInspeccionSeleccionada = orden;
                pedirObservacionCierreOrden();
                return;
            }
        }
    }

    public void tomarObservacionCierreOrden(String observacionCierreOrden) {
        this.observacionCierreOrdenInspeccion = observacionCierreOrden;
        this.buscarTiposMotivoFueraDeServicio(motivos);
        this.pedirSeleccionMotivoTipo(motivos);
    }

    private void pedirObservacionCierreOrden(){
       interfaz.pedirObservacionCierreOrden();
    }

    public List<String> buscarTiposMotivoFueraDeServicio(List<MotivoTipo> motivos) {
        return motivos.stream()
                .map(MotivoTipo::getDescripcion)
                .toList(); // Si usás Java 16+, si no, usá .collect(Collectors.toList())
    }

    private void pedirSeleccionMotivoTipo(List<MotivoTipo> motivos) {
        interfaz.pedirSeleccionMotivoTipo(motivos);
    }

    public void tomarMotivoTipo(String motivoSeleccionado, List<MotivoTipo> motivosTipoDisponibles) {
        for (MotivoTipo motivo : motivosTipoDisponibles) {
            if (motivo.toString().equals(motivoSeleccionado)) {
                motivosTipoSeleccionados.add(motivo);
                System.out.println("MOTIVOS SELECCIONADOS:"+ motivosTipoSeleccionados);
                break;
            }
        }
    }

    public void tomarComentario(String comentario){

        comentarios.add(comentario);
        pedirSeleccionMotivoTipo(motivos);
    }

    public void pedirConfirmacionCierreOrden() {
        // Continuar con el flujo
        System.out.println("Fin de selección de motivos");
        interfaz.pedirConfirmacionCierreOrden();
    }

    public void tomarConfirmacionCierreOrden(boolean confirmacion){
        System.out.println("confirmacion recibida y es: " + confirmacion);
        validarExistenciaObservacion(comentarios);
        validarExistenciaMotivoSeleccionado(motivosTipoSeleccionados);
        cerrarOrdenDeInspeccion(buscarEstadoCerradaOI(estados));
        ponerSismografoFueraServicio(sismografos,empleadoLogueado);

        //GENERAMOS UN MAP CON MOTIVO Y COMENTARIO
        Map<MotivoTipo, String> motivoConComentario = new HashMap<>();

        for (int i = 0; i < motivosTipoSeleccionados.size(); i++) {
            MotivoTipo motivo = motivosTipoSeleccionados.get(i);
            String comentario = i < comentarios.size() ? comentarios.get(i) : "";
            motivoConComentario.put(motivo, comentario);
        }
        notificar(ordenInspeccionSeleccionada.getIdSismografo(), this.buscarFueraDeServicio(), fechaHoraFinalizacion, motivoConComentario);
    }

    public boolean validarExistenciaObservacion(List<String> comentariosIngresados) {
        if (comentariosIngresados == null || comentariosIngresados.isEmpty()) {
            System.out.println("Debe ingresar Comentarios.");
            return false;
        }
        return true;
    }

    public boolean validarExistenciaMotivoSeleccionado(List<MotivoTipo> motivosSeleccionados) {
        if (motivosSeleccionados == null || motivosSeleccionados.isEmpty()) {
            System.out.println("Debe seleccionar al menos un motivo de fuera de servicio.");
            return false;
        }
        return true;
    }

    public LocalDateTime getFechaHoraActual() {
        return LocalDateTime.now();
    }

    public void cerrarOrdenDeInspeccion(Estado nuevoEstado) {
        if (ordenInspeccionSeleccionada != null) {
            this.fechaHoraFinalizacion = getFechaHoraActual();
            ordenInspeccionSeleccionada.cerrar(fechaHoraFinalizacion,nuevoEstado);
        } else {
            System.out.println("No hay una orden de inspección seleccionada para cerrar.");
        }
    }

    public Estado buscarEstadoCerradaOI(List<Estado> estados) {
        for (Estado estado : estados) {
            if (estado.sosCerrada() && estado.sosAmbitoOrdenInspeccion()) {
                return estado;
            }
        }
        return null; // o lanzar excepción si es obligatorio encontrarlo
    }

    public Estado buscarFueraDeServicio() {
        for (Estado estado : estados) {
            if (estado.sosFueraDeServicio()) {
                return estado;
            }
        }
        throw new RuntimeException("No se encontró un estado Fuera de Servicio.");
    }

    public void ponerSismografoFueraServicio(List<Sismografo> sismografos, Empleado responsable) {
        if (ordenInspeccionSeleccionada != null) {
            ordenInspeccionSeleccionada.ponerSismografoFueraDeServicio(sismografos, motivosTipoSeleccionados, responsable);
        } else {
            throw new RuntimeException("No hay una orden seleccionada.");
        }
    }

    public List<String> buscarResponsablesReparacion() {
        List<String> mailsResponsables = new ArrayList<>();

        for (Empleado empleado : empleados) {
            if (empleado.esResponsableReparacion()) {
                mailsResponsables.add(empleado.obtenerMail());
            }
        }

        return mailsResponsables;
    }

    public void notificar(String idSismografo, Estado estado, LocalDateTime fechaHoraRegistro,  Map<MotivoTipo, String> motivosYComentarios) {

        // 1. Notificar por mail a los responsables
        List<String> mailsResponsables = buscarResponsablesReparacion();
        String asunto = "Cambio de estado de sismógrafo a " + estado.getNombreEstado();
        String cuerpo = "Se ha registrado un cambio de estado en el sismógrafo con ID: " + idSismografo + "\n" +
                "Nuevo estado: " + estado.getNombreEstado() + "\n" +
                "Fecha y hora del cambio: " + fechaHoraRegistro + "\n" +
                "Motivos y comentarios:\n";

        for (Map.Entry<MotivoTipo, String> entry : motivosYComentarios.entrySet()) {
            String motivo = entry.getKey().getDescripcion();
            String comentario = entry.getValue();
            cuerpo += "- " + motivo + ": " + comentario + "\n";
        }

        for (String mail : mailsResponsables) {
            interfazNotificacionMail.enviarNotificacion(mail, asunto, cuerpo);
        }

        // 2. Publicar en el monitor del CCRS
        interfazMonitorCCRS.publicarNotificacion(
                idSismografo,
                estado.getNombreEstado(),
                fechaHoraRegistro,
                motivosYComentarios
        );
    }

    public void generardatos() {
        // ---------
        // LISTADO MOTIVOS TIPO
        // ---------
        motivos = new ArrayList<>();
        motivos.add(MotivoTipo.AVERIA_VIBRACION);
        motivos.add(MotivoTipo.DESGASTE_COMPONENTE);
        motivos.add(MotivoTipo.FALLO_REGISTRO);
        motivos.add(MotivoTipo.VANDALISMO);
        motivos.add(MotivoTipo.FALLO_ALIMENTACION);
        motivos.add(MotivoTipo.OTRO); // Por si querés dejar un motivo genérico


        // ---------
        // ROLES
        // ---------
        Rol responsableReparacion = new Rol("ResponsableReparacion", "Encargado de coordinar y realizar reparaciones del sismógrafo.");
        Rol tecnico = new Rol("Tecnico", "Encargado de tareas técnicas generales.");
        Rol supervisor = new Rol("Supervisor", "Supervisa las tareas de inspección y mantenimiento.");
        Rol administrador = new Rol("Administrador", "Gestiona usuarios y configuraciones del sistema.");

        // ---------
        // EMPLEADOS
        // ---------
        Empleado emp1 = new Empleado("Lucía", "Gómez", "lucia.gomez@sismo.gob.ar", "3511234567", responsableReparacion);
        Empleado emp2 = new Empleado("Martín", "Fernández", "martin.fernandez@sismo.gob.ar", "3512345678", responsableReparacion);
        Empleado emp3 = new Empleado("Sofía", "López", "sofia.lopez@sismo.gob.ar", "3513456789", responsableReparacion);
        Empleado emp4 = new Empleado("Carlos", "Ramírez", "carlos.ramirez@sismo.gob.ar", "3514567890", supervisor);
        Empleado emp5 = new Empleado("Ana", "Torres", "ana.torres@sismo.gob.ar", "3515678901", administrador);

        // Listado empleados
        empleados = new ArrayList<>();
        empleados.add(emp1);
        empleados.add(emp2);
        empleados.add(emp3);
        empleados.add(emp4);
        empleados.add(emp5);

        // ---------
        // USUARIOS
        // ---------
        Usuario usuario1 = new Usuario("lucia.g", "pass123", emp1);
        Usuario usuario2 = new Usuario("martin.f", "mantenimiento123", emp3);

        // ---------
        // SESION
        // ---------

        //CASO DE QUE TIENE 2  ORDENES
        this.sesionActual = new Sesion(usuario1, LocalDateTime.now());

        // FLUJO ALTERNATIVO --> NO TIENE OI REALIZADAS
        //this.sesionActual = new Sesion(usuario2, LocalDateTime.now());

        // ---------
        // CAMBIOS ESTADO y ESTADOS
        // ---------

        Estado estado1 = new Estado("OrdenInspeccion", "Completamente Realizada");
        Estado estado2 = new Estado("Sismografo", "Cerrada");
        Estado estado3 = new Estado("OrdenInspeccion", "Fuera de Servicio");
        Estado estado7 = new Estado("Sismografo", "Completamente Realizada");

        CambioEstado cambio1 = new CambioEstado(
                LocalDateTime.of(2024, 10, 1, 8, 0),
                null,
                emp1,
                null,
                estado1
        );

        CambioEstado cambio2 = new CambioEstado(
                LocalDateTime.of(2024, 10, 5, 9, 30),
                null,
                emp2,
                null,
                estado2
        );

        CambioEstado cambio3 = new CambioEstado(
                LocalDateTime.of(2024, 10, 10, 14, 15),
                null,
                emp3,
                null,
                estado7
        );

        // LISTADO DE ESTADOS
        estados = new ArrayList<>();
        estados.add(estado1);
        estados.add(estado2);
        estados.add(estado3);
        estados.add(estado7);

        // ---------
        // ESTACIONES
        // ---------

        EstacionSismologica estacion1 = new EstacionSismologica(
                101,
                "Cert-2023-0001",
                new Date(123, 4, 10),  // Año 2023, mayo 10 (recuerda que el mes es 0-based)
                -34.6037,
                -58.3816,
                "Estacion Centro",
                5001,
                null // Sismografo no asignado para evitar ciclo
        );

        EstacionSismologica estacion2 = new EstacionSismologica(
                102,
                "Cert-2023-0002",
                new Date(124, 0, 20),  // Año 2024, enero 20
                -33.4489,
                -70.6693,
                "Estacion Norte",
                5002,
                null
        );

        // ---------
        // SISMOGRAFO
        // ---------

        Sismografo sismografo1 = new Sismografo(
                new Date(120, 5, 15),  // 15 junio 2020
                "SIS-001",
                1001,
                estacion1,
                cambio3,
                estado7
        );

        Sismografo sismografo2 = new Sismografo(
                new Date(121, 8, 10),  // 10 septiembre 2021
                "SIS-002",
                1002,
                estacion1,
                cambio3,
                estado7
        );

        Sismografo sismografo3 = new Sismografo(
                new Date(122, 0, 25),  // 25 enero 2022
                "SIS-003",
                1003,
                estacion2,
                cambio2,
                estado2
        );

        // LISTADO DE SISMOGRAFOS
        sismografos = new ArrayList<>();
        sismografos.add(sismografo1);
        sismografos.add(sismografo2);
        sismografos.add(sismografo3);

        // SETEAMOS LOS SISMOGRAFOS A LAS ESTACIONES
        estacion1.setSismografo(sismografo1);
        estacion2.setSismografo(sismografo2);

        // ---------
        // ORDENES DE INSPECCION
        // ---------

        OrdenDeInspeccion orden1 = new OrdenDeInspeccion(
                "ORD-0001",
                LocalDateTime.of(2025, 1, 10, 9, 30),
                LocalDateTime.of(2025,5, 12, 16, 0),

                emp2,
                estacion1,
                estado1
        );

        OrdenDeInspeccion orden2 = new OrdenDeInspeccion(
                "ORD-0002",
                LocalDateTime.of(2025, 2, 11, 14, 0),
                LocalDateTime.of(2025,4, 11, 14, 0),
                emp1,
                estacion1,
                estado1
        );
        OrdenDeInspeccion orden3 = new OrdenDeInspeccion(
                "ORD-0003",
                LocalDateTime.of(2025, 4, 11, 14, 0),
                LocalDateTime.of(2025,5, 11, 14, 0),
                emp1,
                estacion2,
                estado1
        );

        //listado de ordenes
        todasLasOrdenes = new ArrayList<>();
        todasLasOrdenes.add(orden1);
        todasLasOrdenes.add(orden2);
        todasLasOrdenes.add(orden3);

    }
}
