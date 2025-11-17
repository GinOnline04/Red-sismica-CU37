package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "empleados")
public class Empleado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String apellido;
    
    @Column(nullable = false, unique = true)
    private String mail;
    
    @Column(nullable = false)
    private String nombre;
    
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    // Constructor sin argumentos para JPA
    public Empleado() {
    }

    public Empleado(String nombre, String apellido, String mail, String telefono, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.telefono = telefono;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public String getApellido() {
        return apellido;
    }

    public String getMail() {
        return mail;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean esResponsableReparacion() {
        return rol != null && rol.esResponsableReparacion();
    }

    public String obtenerMail() {
        return mail;
    }
}
