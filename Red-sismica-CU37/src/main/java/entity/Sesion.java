package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones")
public class Sesion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructor sin argumentos para JPA
    public Sesion() {
    }

    public Sesion(Usuario usuario, LocalDateTime fechaInicio) {
        this.usuario = usuario;
        this.fechaInicio = fechaInicio;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void cerrarSesion() {
        this.fechaFin = LocalDateTime.now();
    }

    public Empleado obtenerEmpleadoLogueado() {
        Empleado empleado = usuario.obtenerEmpleado();
        if (empleado.getRol().esResponsableReparacion()) {
            return empleado;
        } else {
            throw new IllegalStateException("El usuario no es Responsable de Inspecciones.");
        }
    }
}
