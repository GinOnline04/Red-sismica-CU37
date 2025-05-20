package entity;

//Rol.java
public class Rol {
 private String nombre;
 private String descripcionRol;

 public Rol(String nombre, String descripcionRol) {
     this.nombre = nombre;
     this.descripcionRol = descripcionRol;
 }

 public String getNombreRol() {
     return nombre;
 }

 public String getDescripcionRol() {
     return descripcionRol;
 }

 public boolean esresponsableInspeccion() {
	    return "ResponsableInspeccion".equals(this.nombre);
	}

}
