package nehu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdenDeInspeccion {

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFinalizacion;
    private LocalDateTime fechaHoraCierre;
    private int numeroOrden;
    private String observacionCliente;

    private Empleado empleado;
    private EstacionSismologica estacion;
    private Estado estado;

    public OrdenDeInspeccion(int numeroOrden, LocalDateTime fechaHoraInicio, Empleado empleado, EstacionSismologica estacion, Estado estado) {
        this.numeroOrden = numeroOrden;
        this.fechaHoraInicio = fechaHoraInicio;
        this.empleado = empleado;
        this.estacion = estacion;
        this.estado = estado;
    }

    // === Getters ===

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaHoraFinalizacion;
    }

    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    public int getNumeroDeOrdenDeInspeccion() {
        return numeroOrden;
    }

    public String getObservacionCliente() {
        return observacionCliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public EstacionSismologica getEstacion() {
        return estacion;
    }

    public Estado getEstado() {
        return estado;
    }

    // === Métodos de negocio ===

    public boolean sosDeEmpleado(Empleado empleado) {
        if (empleado == null) return false;
        return empleado.equals(this.empleado);
    }

    public boolean sosCompletamenteRealizada() {
        return estado != null && estado.sosCompletamenteRealizada();
    }

    public OrdenDeInspeccion obtenerDatosOrdenInspeccion() {
        return this;
    }

    public String getIdSismografo() {
        return estacion.getIdentificadorSismografo();
    }

    public String getNombreEstacionSismologica() {
        return estacion.getNombreEstacion();
    }

    public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    public void setEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void cerrar(LocalDateTime fechaCierre) {
        setFechaHoraCierre(fechaCierre);
    }

    public void ponerSismografoFueraDeServicio(Sismografo sismografo, List<MotivoFueraServicio> motivos, Empleado responsable) {
        estacion.ponerSismografoFueraDeServicio(sismografo, motivos, responsable);
    }
}