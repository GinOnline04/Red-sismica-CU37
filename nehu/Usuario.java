package nehu;

//Usuario.java
public class Usuario {
	 private String nombreUsuario;
	 private String contrase�a;
	 private Empleado empleadoAsociado;
	
	 public Usuario(String nombreUsuario, String contrase�a, Empleado empleadoAsociado) {
	     this.nombreUsuario = nombreUsuario;
	     this.contrase�a = contrase�a;
	     this.empleadoAsociado = empleadoAsociado;
	 }
	
	 public String getNombreUsuario() {
	     return nombreUsuario;
	 }
	
	 public boolean verificarContrase�a(String intento) {
	     return contrase�a.equals(intento);
	 }
	
	 public Empleado obtenerEmpleado() {
	     return empleadoAsociado;
	 }


}
