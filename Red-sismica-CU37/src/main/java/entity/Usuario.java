package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombreUsuario;
    
    @Column(nullable = false)
    private String contrasena;

    @OneToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleadoAsociado;

    // Constructor sin argumentos para JPA
    public Usuario() {
    }

    public Usuario(String nombreUsuario, String contrasena, Empleado empleadoAsociado) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.empleadoAsociado = empleadoAsociado;
    }

    public Long getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public boolean verificarContrasena(String intento) {
        return contrasena.equals(intento);
    }

    public Empleado obtenerEmpleado() {
        return empleadoAsociado;
    }
}
