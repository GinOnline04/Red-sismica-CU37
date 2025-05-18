package control;

import boundary.*;
import entity.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestorTest {
    // Atributos
    private Empleado empleadoLogueado;
    private List<OrdenDeInspeccion> ordenesCompletamenteRealizadas;
    private List<OrdenDeInspeccion> todasLasOrdenes; // este atributo es solo para que no salte error, despues hay que cambiarlo cuando tengamos la BD.
    private Sesion sesionActual;
    private InterfazCerrarOrdenInspeccion interfaz;
    private OrdenDeInspeccion ordenInspeccionSeleccionada;
    private String observacionCierreOrdenInspeccion;

    public GestorTest(InterfazCerrarOrdenInspeccion interfaz) {
        this.interfaz = interfaz;
    }

    // Métodos

    public void iniciarCierreOrdenInspeccion() {
        this.empleadoLogueado = buscarEmpleadoLogueado();
        this.ordenesCompletamenteRealizadas = buscarOrdenesInspeccionDeRI(empleadoLogueado, todasLasOrdenes);
        ordenarPorFechaFinalizacion(this.ordenesCompletamenteRealizadas);
        // 📤 Pedimos a la interfaz que las muestre y permita seleccionar
        interfaz.pedirSeleccionOrdenInspeccion(this.ordenesCompletamenteRealizadas);
        this.pedirObservacionCierreOrden();
        interfaz.pedirSeleccionMotivoTipo();
    }

    private Empleado buscarEmpleadoLogueado() {
        return this.sesionActual.obtenerEmpleadoLogueado();
    }

    private List<OrdenDeInspeccion> buscarOrdenesInspeccionDeRI(Empleado empleado, List<OrdenDeInspeccion> todasLasOrdenes) {
        List<OrdenDeInspeccion> ordenesFiltradas = new ArrayList<>();

        for (OrdenDeInspeccion orden : todasLasOrdenes) {
            if (orden.sosDeEmpleado(empleado) && orden.sosCompletamenteRealizada()) {
                ordenesFiltradas.add(orden.obtenerDatosOrdenInspeccion());
            }
        }

        return ordenesFiltradas;
    }

    private void ordenarPorFechaFinalizacion(List<OrdenDeInspeccion> ordenesFiltradas) {
        ordenesFiltradas.sort(Comparator.comparing(OrdenDeInspeccion::getFechaFinalizacion));
    }

    public void tomarOrdenInspeccionSeleccionada(int numeroOrden) {
        for (OrdenDeInspeccion orden : ordenesCompletamenteRealizadas) {
            if (orden.getNumeroDeOrdenDeInspeccion() == numeroOrden) {
                this.ordenInspeccionSeleccionada = orden;
                return;
            }
        }
    }

    public void tomarObservacionCierreOrden(String observacionCierreOrden) {
        this.observacionCierreOrdenInspeccion = observacionCierreOrden;
    }

    private void pedirObservacionCierreOrden(){
       interfaz.pedirObservacionCierreOrden();
    }
}
