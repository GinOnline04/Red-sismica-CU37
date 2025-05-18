package control;

import boundary.InterfazCerrarOrdenInspeccion;

import java.util.Arrays;
import java.util.List;

public class GestorCerrarOrdenInspeccion {

    private InterfazCerrarOrdenInspeccion interfaz;

    public GestorCerrarOrdenInspeccion(InterfazCerrarOrdenInspeccion interfaz) {
        this.interfaz = interfaz;
    }

    public void iniciarCierreOrdenInspeccion() {
        // 🔍 Simulamos búsqueda de órdenes completadas
        List<String> ordenes = Arrays.asList("Orden #101, 10/12/2003, Estacion Juan Pablo", "Orden #102", "Orden #103");

        // 📤 Pedimos a la interfaz que las muestre y permita seleccionar
        interfaz.pedirSeleccionOrdenInspeccion(ordenes);
    }
    public void pedirSeleccionarMotivosFueraServicio() {
        // Lista ficticia de motivos tipo
        List<String> motivosTipo = List.of(
                "Fallo técnico",
                "Mantenimiento preventivo",
                "Actualización de firmware",
                "Problemas de conectividad",
                "Inspección de rutina"
        );

        // Llamada a la interfaz
        interfaz.pedirSeleccionMotivoTipo(motivosTipo);
    }
    public void pedirConfirmacionCierreOrden() {
        // Continuar con el flujo
        System.out.println("Fin de selección de motivos");
        interfaz.pedirConfirmacionCierreOrden();
    }

    public void tomarConfirmacionCierreOrden(boolean confirmacion){
        System.out.println("confirmacion recibida y es: " + confirmacion);
}
}