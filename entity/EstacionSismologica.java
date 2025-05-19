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
    private Sismografo sismografo;

    public EstacionSismologica(int codigoEstacion, String documentoCertificacionAdq, Date fechaSolicitudCertificacion, double latitud, double longitud, String nombreEstacion, int nroCertificacionAdquisicion, Sismografo sismografo) {
        this.codigoEstacion = codigoEstacion;
        this.documentoCertificacionAdq = documentoCertificacionAdq;
        this.fechaSolicitudCertificacion = fechaSolicitudCertificacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreEstacion = nombreEstacion;
        this.nroCertificacionAdquisicion = nroCertificacionAdquisicion;
        this.sismografo = sismografo;
    }

    public int getCodigoEstacion() {
        return codigoEstacion;
    }

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public String getIdentificadorSismografo() {
        return sismografo.getIdentificadorSismografo();
    }

    public void ponerSismografoFueraDeServicio(List<Sismografo> sismografos, List<MotivoTipo> motivos, Empleado responsable, List<String> comentarios) {
        for (Sismografo sismografo : sismografos) {
            if (sismografo.getIdentificadorSismografo().equals(this.getIdentificadorSismografo())) {
                sismografo.fueraDeServicio(motivos, responsable, comentarios);
                return; // Se asume que el identificador es único, por eso se puede salir del loop
            }
        }

        System.out.println("No se encontró un sismógrafo con el identificador: " + this.getIdentificadorSismografo());
    }

    public void setSismografo(Sismografo sismografo) {
        this.sismografo = sismografo;
    }
}