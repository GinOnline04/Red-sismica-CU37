package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(length = 500)
    private String descripcionRol;

    // Constructor sin argumentos para JPA
    public Rol() {
    }

    public Rol(String nombre, String descripcionRol) {
        this.nombre = nombre;
        this.descripcionRol = descripcionRol;
    }

    public Long getId() {
        return id;
    }

    public String getNombreRol() {
        return nombre;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public boolean esResponsableReparacion() {
        return "ResponsableReparacion".equals(this.nombre);
    }
}
