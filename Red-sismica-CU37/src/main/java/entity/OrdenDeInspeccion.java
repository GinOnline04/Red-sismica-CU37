package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordenes_inspeccion")
public class OrdenDeInspeccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFinalizacion;
    private LocalDateTime fechaHoraCierre;
    
    @Column(nullable = false, unique = true)
    private String numeroOrden;
    
    @Column(length = 2000)
    private String observacionCliente;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;
    
    @ManyToOne
    @JoinColumn(name = "estacion_id", nullable = false)
    private EstacionSismologica estacion;
    
    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    // Constructor sin argumentos para JPA
    public OrdenDeInspeccion() {
    }

    public OrdenDeInspeccion(String numeroOrden, LocalDateTime fechaHoraInicio, 
                             LocalDateTime fechaHoraFinalizacion, Empleado empleado, 
                             EstacionSismologica estacion, Estado estado) {
        this.numeroOrden = numeroOrden;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFinalizacion = fechaHoraFinalizacion;
        this.empleado = empleado;
        this.estacion = estacion;
        this.estado = estado;
    }

    // === Getters ===

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaHoraFinalizacion;
    }

    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    public String getNumeroDeOrdenDeInspeccion() {
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

    public String obtenerDatosOrdenInspeccion() {
        return "Orden Nº " + numeroOrden
                + " | Finalizada: " + (fechaHoraFinalizacion != null ? fechaHoraFinalizacion.toString() : "Sin finalizar")
                + " | Estación: " + estacion.getNombreEstacion()
                + " | Sismógrafo: " + estacion.getIdentificadorSismografo();
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

    public void cerrar(LocalDateTime fechaCierre, Estado nuevoEstado) {
        setFechaHoraCierre(fechaCierre);
        setEstado(nuevoEstado);
    }

    public void ponerSismografoFueraDeServicio(List<Sismografo> sismografos, List<MotivoTipo> motivos, 
                                                Empleado responsable, List<String> comentarios) {
        estacion.ponerSismografoFueraDeServicio(sismografos, motivos, responsable, comentarios);
    }
}