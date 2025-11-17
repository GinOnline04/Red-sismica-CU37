package control;

import boundary.*;
import entity.*;
import repository.*;

import java.time.LocalDateTime;
import java.util.*;

public class GestorCerrarOrdenInspeccion {
    // Atributos
    private Empleado empleadoLogueado;
    private List<OrdenDeInspeccion> ordenesCompletamenteRealizadas;
    private Sesion sesionActual;
    private InterfazCerrarOrdenInspeccion interfaz;
    private InterfazNotificacionMail interfazNotificacionMail;
    private InterfazMonitorCCRS interfazMonitorCCRS;
    private OrdenDeInspeccion ordenInspeccionSeleccionada;
    private String observacionCierreOrdenInspeccion;
    private List<MotivoTipo> motivos;
    private List<MotivoTipo> motivosTipoSeleccionados;
    private List<String> comentarios;
    private LocalDateTime fechaHoraFinalizacion;
    
    // Repositories
    private OrdenInspeccionRepository ordenInspeccionRepo;
    private EmpleadoRepository empleadoRepo;
    private EstadoRepository estadoRepo;
    private SismografoRepository sismografoRepo;
    private UsuarioRepository usuarioRepo;

    public GestorCerrarOrdenInspeccion(InterfazCerrarOrdenInspeccion interfaz,
                                       InterfazNotificacionMail interfazNotificacionMail,
                                       InterfazMonitorCCRS interfazMonitorCCRS,
                                       Sesion sesionActual) {
        this.interfaz = interfaz;
        this.interfazNotificacionMail = interfazNotificacionMail;
        this.interfazMonitorCCRS = interfazMonitorCCRS;
        this.sesionActual = sesionActual;
        this.motivosTipoSeleccionados = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        
        // Inicializar repositories
        this.ordenInspeccionRepo = new OrdenInspeccionRepository();
        this.empleadoRepo = new EmpleadoRepository();
        this.estadoRepo = new EstadoRepository();
        this.sismografoRepo = new SismografoRepository();
        this.usuarioRepo = new UsuarioRepository();
    }

    // Métodos

    public void iniciarCierreOrdenInspeccion() {
        // Inicializar lista de motivos tipo enum
        this.motivos = Arrays.asList(MotivoTipo.values());
        
        // Obtener empleado logueado desde la sesión
        this.empleadoLogueado = buscarEmpleadoLogueado();
        
        // Buscar órdenes completamente realizadas del empleado desde BD
        this.ordenesCompletamenteRealizadas = buscarOrdenesInspeccionDeRI(empleadoLogueado);
        
        // Ordenar por fecha de finalización
        ordenarPorFechaFinalizacion(this.ordenesCompletamenteRealizadas);
        
        // Mostrar órdenes en la interfaz
        interfaz.pedirSeleccionOrdenInspeccion(this.ordenesCompletamenteRealizadas);
    }

    private Empleado buscarEmpleadoLogueado() {
        return this.sesionActual.obtenerEmpleadoLogueado();
    }

    private List<OrdenDeInspeccion> buscarOrdenesInspeccionDeRI(Empleado empleado) {
        // Usar repository para obtener órdenes completamente realizadas del empleado
        return ordenInspeccionRepo.findByEmpleadoAndCompletamenteRealizada(empleado);
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
                .toList();
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
        cerrarOrdenDeInspeccion(buscarEstadoCerradaOI());
        ponerSismografoFueraDeServicio(empleadoLogueado, comentarios);

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
            ordenInspeccionSeleccionada.cerrar(fechaHoraFinalizacion, nuevoEstado);
            // Persistir cambios en BD
            ordenInspeccionRepo.save(ordenInspeccionSeleccionada);
        } else {
            System.out.println("No hay una orden de inspección seleccionada para cerrar.");
        }
    }

    public Estado buscarEstadoCerradaOI() {
        // Buscar desde BD el estado "Cerrada" del ámbito "OrdenInspeccion"
        return estadoRepo.findByAmbitoAndNombre("OrdenInspeccion", "Cerrada")
                .orElseThrow(() -> new RuntimeException("No se encontró el estado Cerrada para Orden de Inspección"));
    }

    public Estado buscarFueraDeServicio() {
        // Buscar desde BD el estado "Fuera de Servicio"
        return estadoRepo.findByAmbitoAndNombre("Sismografo", "Fuera de Servicio")
                .orElseThrow(() -> new RuntimeException("No se encontró un estado Fuera de Servicio."));
    }

    public void ponerSismografoFueraDeServicio(Empleado responsable, List<String> comentarios) {
        if (ordenInspeccionSeleccionada != null) {
            // Obtener todos los sismógrafos desde BD
            List<Sismografo> sismografos = sismografoRepo.findAll();
            ordenInspeccionSeleccionada.ponerSismografoFueraDeServicio(sismografos, motivosTipoSeleccionados, responsable, comentarios);
            
            // Actualizar sismógrafo en BD
            String idSismografo = ordenInspeccionSeleccionada.getIdSismografo();
            Sismografo sismografo = sismografoRepo.findByIdentificador(idSismografo)
                    .orElseThrow(() -> new RuntimeException("Sismógrafo no encontrado"));
            sismografoRepo.save(sismografo);
        } else {
            throw new RuntimeException("No hay una orden seleccionada.");
        }
    }

    public List<String> buscarResponsablesReparacion() {
        // Buscar empleados responsables de reparación desde BD
        List<Empleado> responsables = empleadoRepo.findResponsablesReparacion();
        List<String> mailsResponsables = new ArrayList<>();
        
        for (Empleado empleado : responsables) {
            mailsResponsables.add(empleado.obtenerMail());
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
}
