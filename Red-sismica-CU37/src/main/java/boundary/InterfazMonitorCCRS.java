package boundary;

import entity.MotivoTipo;
import java.time.LocalDateTime;
import java.util.Map;

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
                                      Map<MotivoTipo, String> motivosYComentarios) {

        System.out.println("🖥️  Publicación en monitor CCRS:");
        System.out.println("=====================================================");
        System.out.println("🔧 Sismógrafo: " + idSismografo);
        System.out.println("📍 Nuevo estado: " + nombreEstado);
        System.out.println("🕒 Fecha y hora del cambio: " + fechaHoraRegistro);
        System.out.println("📋 Motivos asociados:");

        for (Map.Entry<MotivoTipo, String> entry : motivosYComentarios.entrySet()) {
            String motivo = entry.getKey().getDescripcion();
            String comentario = entry.getValue();
            System.out.println("- " + motivo + ": " + comentario);
        }

        System.out.println("=====================================================\n");
    }
}
