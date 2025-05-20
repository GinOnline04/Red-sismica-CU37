package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CambioEstado {
    private String nombreEstado;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    private Empleado responsable;
    private List<MotivoFueraServicio> motivos;
    private Estado estado;

    public CambioEstado(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin,
                        Empleado responsable, Estado estado) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.responsable = responsable;
        this.motivos = new ArrayList<MotivoFueraServicio>();
        this.estado = estado;
        this.nombreEstado = estado.getNombreEstado();
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public void setResponsable(Empleado responsable) {
        this.responsable = responsable;
    }

    public void crearMotivoFueraSerivicio(String comentario, MotivoTipo motivo) {
        MotivoFueraServicio nuevoMotivo = new MotivoFueraServicio(comentario, motivo);
        motivos.add(nuevoMotivo);
    }
    public void setMotivos(List<MotivoTipo> motivos, List<String> comentarios) {
        if (motivos == null || comentarios == null) {
            throw new IllegalArgumentException("Las listas no pueden ser nulas.");
        }

        if (motivos.size() != comentarios.size()) {
            throw new IllegalArgumentException("Las listas de motivos y comentarios deben tener el mismo tamaño.");
        }

        for (int i = 0; i < motivos.size(); i++) {
            MotivoTipo motivo = motivos.get(i);
            String comentario = comentarios.get(i);
            crearMotivoFueraSerivicio(comentario, motivo);
        }
    }

}