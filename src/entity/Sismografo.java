package entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Sismografo {
    private Date fechaAdquisicion;
    private String identificadorSismografo;
    private int nroSerie;

    private EstacionSismologica estacionSismologica;
    private CambioEstado cambioEstado;
    private Estado estado;

    public Sismografo(Date fechaAdquisicion, String identificadorSismografo, int nroSerie, EstacionSismologica estacionSismologica, CambioEstado cambioEstado, Estado estado) {
        this.fechaAdquisicion = fechaAdquisicion;
        this.identificadorSismografo = identificadorSismografo;
        this.nroSerie = nroSerie;
        this.estacionSismologica = estacionSismologica;
        this.cambioEstado = cambioEstado;
        this.estado = estado;
    }

    public String getIdentificadorSismografo() {
        return identificadorSismografo;
    }

    public void fueraDeServicio(List<MotivoTipo> motivos, Empleado responsable, List<String> comentarios) {
        cambioEstado.setFechaHoraFin(LocalDateTime.now());

        CambioEstado fueraServicio = new CambioEstado(LocalDateTime.now(),null,responsable,estado);
        fueraServicio.setNombreEstado(estado.getNombreEstado());

        fueraServicio.setMotivos(motivos, comentarios);

        this.actualizarEstado(fueraServicio);
    }

    public void actualizarEstado(CambioEstado nuevoCambioEstado) {
        this.cambioEstado = nuevoCambioEstado;
    }
}