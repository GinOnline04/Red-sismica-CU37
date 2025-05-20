package entity;

public class MotivoFueraServicio {
    private String comentario;
    private MotivoTipo motivo;

    public MotivoFueraServicio(String comentario, MotivoTipo motivo) {
        this.comentario = comentario;
        this.motivo = motivo;
    }
}
