package entity;

import java.time.LocalDateTime;
import java.util.List;

public class CambioEstado {
    private String nombreEstado;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    private Empleado responsable;
    private List<MotivoFueraServicio> motivos;
    private Estado estado;

    public CambioEstado(String nombreEstado, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin,
                        Empleado responsable, List<MotivoFueraServicio> motivos, Estado estado) {
        this.nombreEstado = nombreEstado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.responsable = responsable;
        this.motivos = motivos;
        this.estado = estado;
    }

    public void setFechaCambio(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = CambioEstado.this.fechaHoraFin;
    }

    public void fueraDeServicio(Sismografo sismografo, List<MotivoFueraServicio> motivos, Empleado responsable) {
        CambioEstado nuevoEstado = new CambioEstado(nombreEstado,fechaHoraInicio,fechaHoraFin,responsable,motivos,estado);
        nuevoEstado.nombreEstado = "Fuera de Servicio";
        nuevoEstado.fechaHoraInicio = LocalDateTime.now();
        nuevoEstado.responsable = responsable;
        nuevoEstado.motivos = motivos;

        sismografo.actualizarEstado(nuevoEstado);
    }
}