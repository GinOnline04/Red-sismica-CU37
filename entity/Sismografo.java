package entity;

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

    public void fueraDeServicio(List<MotivoTipo> motivos, Empleado responsable) {
        estadoActual.fueraDeServicio(this, motivos, responsable);
    }

    public void actualizarEstado(CambioEstado nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }
}