package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "estados")
public class Estado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String ambito;
    
    @Column(nullable = false)
    private String nombreEstado;

    // Constructor sin argumentos para JPA
    public Estado() {
    }

    public Estado(String ambito, String nombreEstado) {
        this.ambito = ambito;
        this.nombreEstado = nombreEstado;
    }

    public Long getId() {
        return id;
    }

    public String getAmbito() {
        return ambito;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public boolean sosCompletamenteRealizada() {
        return "Completamente Realizada".equalsIgnoreCase(nombreEstado);
    }

    public boolean sosCerrada() {
        return "Cerrada".equalsIgnoreCase(nombreEstado);
    }

    public boolean sosFueraDeServicio() {
        return "Fuera de Servicio".equalsIgnoreCase(nombreEstado);
    }

    public boolean sosAmbitoOrdenInspeccion() {
        return "OrdenInspeccion".equalsIgnoreCase(ambito);
    }

    public boolean sosAmbitoSismografo() {
        return "Sismografo".equalsIgnoreCase(ambito);
    }
}