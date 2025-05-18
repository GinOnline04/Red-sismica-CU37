package entity;

//Usuario.java
public class Usuario {
	 private String nombreUsuario;
	 private String contrasena;

	 private Empleado empleadoAsociado;
	
	 public Usuario(String nombreUsuario, String contrasena, Empleado empleadoAsociado) {
	     this.nombreUsuario = nombreUsuario;
	     this.contrasena = contrasena;
	     this.empleadoAsociado = empleadoAsociado;
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
