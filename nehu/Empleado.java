package nehu;

public class Empleado {
    private String apellido;
    private String mail;
    private String nombre;
    private String telefono;
    private Rol rol;

    public Empleado(String nombre, String apellido, String mail, String telefono, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.telefono = telefono;
        this.rol = rol;
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
