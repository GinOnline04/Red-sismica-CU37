package entity;

public enum MotivoTipo {
    AVERIA_VIBRACION("Avería por vibración"),
    DESGASTE_COMPONENTE("Desgaste de componente"),
    FALLO_REGISTRO("Fallo en el sistema de registro"),
    VANDALISMO("Vandalismo"),
    FALLO_ALIMENTACION("Fallo en fuente de alimentación"),
    OTRO("Otro motivo");

    private final String descripcion;

    MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
