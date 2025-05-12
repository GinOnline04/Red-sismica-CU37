package flujo1;

//Empleado.java
public class Empleado {
 private String apellido;
 private String mail;
 private String nombre;
 private String telefono;

 public Empleado(String nombre, String apellido, String mail, String telefono) {
     this.nombre = nombre;
     this.apellido = apellido;
     this.mail = mail;
     this.telefono = telefono;
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

 public boolean esResponsableReparacion() {
     throw new UnsupportedOperationException("Implementar verificación de rol de reparación");
 }

 public String obtenerMail() {
     return mail;
 }
}
