package entity;

public enum MotivoTipo {
    FALLO_TECNICO("Fallo técnico"),
    MANTENIMIENTO("Mantenimiento preventivo"),
    FIRMWARE("Actualización de firmware"),
    CONECTIVIDAD("Problemas de conectividad"),
    RUTINA("Inspección de rutina");

    private final String descripcion;

    MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
