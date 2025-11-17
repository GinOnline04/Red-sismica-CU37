package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "motivos_fuera_servicio")
public class MotivoFueraServicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 1000)
    private String comentario;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoTipo motivo;

    // Constructor sin argumentos para JPA
    public MotivoFueraServicio() {
    }

    public MotivoFueraServicio(String comentario, MotivoTipo motivo) {
        this.comentario = comentario;
        this.motivo = motivo;
    }

    public Long getId() {
        return id;
    }

    public String getComentario() {
        return comentario;
    }

    public MotivoTipo getMotivo() {
        return motivo;
    }
}
