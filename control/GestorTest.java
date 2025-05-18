package control;

import boundary.*;
import entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestorTest {
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
    private String comentario;
    private List<Estado> estados;
    private List<Sismografo> sismografos;
    private List<Empleado> empleados;
    private LocalDateTime fechaHoraFinalizacion;

    public GestorTest(InterfazCerrarOrdenInspeccion interfaz,
                      InterfazNotificacionMail interfazNotificacionMail,
                      InterfazMonitorCCRS interfazMonitorCCRS) {
        this.interfaz = interfaz;
        this.interfazNotificacionMail = interfazNotificacionMail;
        this.interfazMonitorCCRS = interfazMonitorCCRS;
    }

    // Métodos

    public void iniciarCierreOrdenInspeccion() {
        this.empleadoLogueado = buscarEmpleadoLogueado();
        this.ordenesCompletamenteRealizadas = buscarOrdenesInspeccionDeRI(empleadoLogueado, todasLasOrdenes);
        ordenarPorFechaFinalizacion(this.ordenesCompletamenteRealizadas);
        interfaz.pedirSeleccionOrdenInspeccion(this.ordenesCompletamenteRealizadas);
        this.pedirObservacionCierreOrden();
        this.buscarTiposMotivoFueraDeServicio(motivos);
        this.pedirSeleccionMotivoTipo(motivos);
        interfaz.tomarComentario();
        validarExistenciaObservacion(this.observacionCierreOrdenInspeccion);
        validarExistenciaMotivoSeleccionado(this.motivosTipoSeleccionados);
        cerrarOrdenDeInspeccion(buscarEstadoCerradaOI(estados));
        ponerSismografoFueraServicio(sismografos,empleadoLogueado);
        notificar(ordenInspeccionSeleccionada.getIdSismografo(), this.buscarFueraDeServicio(), fechaHoraFinalizacion, motivosTipoSeleccionados);
    }

    private Empleado buscarEmpleadoLogueado() {
        return this.sesionActual.obtenerEmpleadoLogueado();
    }

    private List<OrdenDeInspeccion> buscarOrdenesInspeccionDeRI(Empleado empleado, List<OrdenDeInspeccion> todasLasOrdenes) {
        List<OrdenDeInspeccion> ordenesFiltradas = new ArrayList<>();

        for (OrdenDeInspeccion orden : todasLasOrdenes) {
            if (orden.sosDeEmpleado(empleado) && orden.sosCompletamenteRealizada()) {
                ordenesFiltradas.add(orden.obtenerDatosOrdenInspeccion());
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
                return;
            }
        }
    }

    public void tomarObservacionCierreOrden(String observacionCierreOrden) {
        this.observacionCierreOrdenInspeccion = observacionCierreOrden;
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
                break;
            }
        }
    }

    public void tomarComentario(String comentario){
        this.comentario = comentario;
        pedirSeleccionMotivoTipo(motivos);
    }

    public void pedirConfirmacionCierreOrden() {
        // Continuar con el flujo
        System.out.println("Fin de selección de motivos");
        interfaz.pedirConfirmacionCierreOrden();
    }

    public void tomarConfirmacionCierreOrden(boolean confirmacion){
        System.out.println("confirmacion recibida y es: " + confirmacion);
    }

    public boolean validarExistenciaObservacion(String comentario) {
        if (comentario == null || comentario.trim().isEmpty()) {
            System.out.println("La observación es obligatoria.");
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

    public void notificar(String idSismografo, Estado estado, LocalDateTime fechaHoraRegistro, List<MotivoTipo> motivosYComentarios) {

        // 1. Notificar por mail a los responsables
        List<String> mailsResponsables = buscarResponsablesReparacion();
        String asunto = "Cambio de estado de sismógrafo a " + estado.getNombreEstado();
        String cuerpo = "Se ha registrado un cambio de estado en el sismógrafo con ID: " + idSismografo + "\n" +
                "Nuevo estado: " + estado.getNombreEstado() + "\n" +
                "Fecha y hora del cambio: " + fechaHoraRegistro + "\n" +
                "Motivos y comentarios:\n";

        for (MotivoTipo motivo : motivosYComentarios) {
            cuerpo += "- " + motivo.getDescripcion() + "\n";
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

//    public void generarDatos(){
//        // ====== Crear un empleado logueado ======
//        Empleado empleadoLogueado = new Empleado("Juan", "Pérez", "juan.perez@sismo.org", "3543-000000");
//        Usuario usuario = new Usuario("jperez", "1234", empleadoLogueado);
//
//        // ====== Sesión actual ======
//        Sesion sesion = Sesion.getInstancia();
//        sesion.setUsuarioLogueado(usuario);
//        gestor.sesionActual = sesion;
//
//        // ====== Estado COMPLETAMENTE REALIZADA y CERRADA ======
//        Estado estadoCompletada = new Estado("Completamente Realizada", true, false, false);
//        Estado estadoCerrada = new Estado("Cerrada", false, true, false);
//        Estado estadoFueraServicio = new Estado("Fuera de Servicio", false, false, true);
//        List<Estado> estados = List.of(estadoCompletada, estadoCerrada, estadoFueraServicio);
//        gestor.estados = estados;
//
//        // ====== Sismógrafo y estación ======
//        Sismografo s1 = new Sismografo("SISMO001");
//        EstacionSismologica estacion = new EstacionSismologica("Estación Norte", s1);
//
//        // ====== Orden de inspección ======
//        OrdenDeInspeccion orden = new OrdenDeInspeccion(1001, LocalDateTime.now().minusDays(2), empleadoLogueado, estacion, estadoCompletada);
//        orden.setFechaHoraCierre(LocalDateTime.now().minusDays(1)); // simula finalización
//
//        List<OrdenDeInspeccion> todasLasOrdenes = new ArrayList<>();
//        todasLasOrdenes.add(orden);
//        gestor.todasLasOrdenes = todasLasOrdenes;
//
//        // ====== Motivos tipo ======
//        MotivoTipo motivo1 = new MotivoTipo("Sensor dañado");
//        MotivoTipo motivo2 = new MotivoTipo("Interferencia eléctrica");
//        gestor.motivos = List.of(motivo1, motivo2);
//        gestor.motivosTipoSeleccionados = new ArrayList<>();
//
//        // ====== Lista de sismógrafos (para buscar por ID) ======
//        gestor.sismografos = List.of(s1);
//
//        // ====== Lista de empleados (para notificación) ======
//        Empleado responsableReparacion = new Empleado("Marta", "López", "marta.lopez@sismo.org", "3543-111111");
//        responsableReparacion.setEsResponsableReparacion(true);
//        gestor.empleados = List.of(empleadoLogueado, responsableReparacion);
//    }
}
