package flujo1;

//Usuario.java
public class Usuario {
	 private String nombreUsuario;
	 private String contraseña;
	
	 private Empleado empleadoAsociado;
	
	 public Usuario(String nombreUsuario, String contraseña, Empleado empleadoAsociado) {
	     this.nombreUsuario = nombreUsuario;
	     this.contraseña = contraseña;
	     this.empleadoAsociado = empleadoAsociado;
	 }
	
	 public String getNombreUsuario() {
	     return nombreUsuario;
	 }
	
	 public boolean verificarContraseña(String intento) {
	     return contraseña.equals(intento);
	 }
	
	 public Empleado obtenerEmpleado() {
	     return empleadoAsociado;
	 }


}
