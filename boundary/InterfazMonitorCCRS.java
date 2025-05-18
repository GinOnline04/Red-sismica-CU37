package boundary;

import entity.MotivoTipo;

import java.time.LocalDateTime;
import java.util.List;

public class InterfazMonitorCCRS {

    /**
     * Publica en el monitor del CCRS los detalles del cambio de estado del sismógrafo.
     *
     * @param idSismografo           identificador del sismógrafo
     * @param nombreEstado           nombre del nuevo estado (ej. "Fuera de Servicio")
     * @param fechaHoraRegistro      fecha y hora del cambio de estado
     * @param motivosYComentarios    lista de strings con los motivos y comentarios
     */
    public void publicarNotificacion(String idSismografo,
                                      String nombreEstado,
                                      LocalDateTime fechaHoraRegistro,
                                      List<MotivoTipo> motivosYComentarios) {

        System.out.println("🖥️  Publicación en monitor CCRS:");
        System.out.println("=====================================================");
        System.out.println("🔧 Sismógrafo: " + idSismografo);
        System.out.println("📍 Nuevo estado: " + nombreEstado);
        System.out.println("🕒 Fecha y hora del cambio: " + fechaHoraRegistro);
        System.out.println("📋 Motivos asociados:");

        for (MotivoTipo motivo : motivosYComentarios) {
            System.out.println("   - " + motivo.getDescripcion());
        }

        System.out.println("=====================================================\n");
    }
}
