package entity;

//Sesion.java
import java.time.LocalDateTime;

public class Sesion {
 private LocalDateTime fechaInicio;
 private LocalDateTime fechaFin;

 private Usuario usuario;

 public Sesion(Usuario usuario, LocalDateTime fechaInicio) {
     this.usuario = usuario;
     this.fechaInicio = fechaInicio;
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
