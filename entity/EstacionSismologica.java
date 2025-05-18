package entity;

import java.util.Date;
import java.util.List;

public class EstacionSismologica {
    private int codigoEstacion;
    private String documentoCertificacionAdq;
    private Date fechaSolicitudCertificacion;
    private double latitud;
    private double longitud;
    private String nombreEstacion;
    private int nroCertificacionAdquisicion;
    private String identificadorSismografo;

    public EstacionSismologica(int codigoEstacion, String documentoCertificacionAdq, Date fechaSolicitudCertificacion, double latitud, double longitud, String nombreEstacion, int nroCertificacionAdquisicion, String identificadorSismografo) {
        this.codigoEstacion = codigoEstacion;
        this.documentoCertificacionAdq = documentoCertificacionAdq;
        this.fechaSolicitudCertificacion = fechaSolicitudCertificacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreEstacion = nombreEstacion;
        this.nroCertificacionAdquisicion = nroCertificacionAdquisicion;
        this.identificadorSismografo = identificadorSismografo;
    }

    public int getCodigoEstacion() {
        return codigoEstacion;
    }

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public String getIdentificadorSismografo() {
        return identificadorSismografo;
    }

    public void ponerSismografoFueraDeServicio(Sismografo sismografo, List<MotivoFueraServicio> motivos, Empleado responsable) {
        if (!sismografo.getIdentificadorSismografo().equals(this.identificadorSismografo)) {
            throw new IllegalArgumentException("El sismógrafo proporcionado no coincide con el asociado a esta estación.");
        }

        sismografo.fueraDeServicio(motivos, responsable);
    }
}