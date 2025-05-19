package entity;

import java.time.LocalDateTime;
import java.util.List;

public class CambioEstado {
    private String nombreEstado;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    private Empleado responsable;
    private List<MotivoTipo> motivos;
    private Estado estado;

    public CambioEstado(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin,
                        Empleado responsable, List<MotivoTipo> motivos, Estado estado) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.responsable = responsable;
        this.motivos = motivos;
        this.estado = estado;
        this.nombreEstado = estado.getNombreEstado();
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public void fueraDeServicio(Sismografo sismografo, List<MotivoTipo> motivos, Empleado responsable) {
        CambioEstado nuevoEstado = new CambioEstado(fechaHoraInicio,fechaHoraFin,responsable,motivos,estado);
        nuevoEstado.nombreEstado = estado.getNombreEstado();
        setFechaHoraFin(LocalDateTime.now());
        nuevoEstado.responsable = responsable;
        nuevoEstado.motivos = motivos;

        sismografo.actualizarEstado(nuevoEstado);
    }
}