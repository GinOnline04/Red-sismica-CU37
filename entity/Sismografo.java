package entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Sismografo {
    private Date fechaAdquisicion;
    private String identificadorSismografo;
    private int nroSerie;

    private EstacionSismologica estacionSismologica;
    private CambioEstado estadoActual;
    private Estado estado;

    public Sismografo(Date fechaAdquisicion, String identificadorSismografo, int nroSerie, EstacionSismologica estacionSismologica, CambioEstado estadoActual, Estado estado) {
        this.fechaAdquisicion = fechaAdquisicion;
        this.identificadorSismografo = identificadorSismografo;
        this.nroSerie = nroSerie;
        this.estacionSismologica = estacionSismologica;
        this.estadoActual = estadoActual;
        this.estado = estado;
    }

    public String getIdentificadorSismografo() {
        return identificadorSismografo;
    }

    public void fueraDeServicio(List<MotivoTipo> motivos, Empleado responsable, List<String> comentarios) {
        estadoActual.setFechaHoraFin(LocalDateTime.now());

        CambioEstado nuevoEstado = new CambioEstado(LocalDateTime.now(),null,responsable,estado);
        nuevoEstado.setNombreEstado(estado.getNombreEstado());

        nuevoEstado.setMotivos(motivos, comentarios);

        this.actualizarEstado(nuevoEstado);
    }

    public void actualizarEstado(CambioEstado nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }
}