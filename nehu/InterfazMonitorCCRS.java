package nehu;

import java.time.LocalDateTime;
import java.util.List;

public class InterfazMonitorCCRS {

    /**
     * Publica en el monitor del CCRS los detalles del cambio de estado del sismógrafo.
     *
     * @param idSismografo           identificador del sismógrafo
     * @param nombreEstado           nombre del nuevo estado (ej. "Fuera de Servicio")
     * @param fechaHoraRegistro      fecha y hora del cambio de estado
     * @param responsable            nombre completo del responsable (RI)
     * @param motivosYComentarios    lista de strings con los motivos y comentarios
     */
    public void publicarNotificacion(String idSismografo,
                                      String nombreEstado,
                                      LocalDateTime fechaHoraRegistro,
                                      String responsable,
                                      List<String> motivosYComentarios) {

        System.out.println("🖥️  Publicación en monitor CCRS:");
        System.out.println("=====================================================");
        System.out.println("🔧 Sismógrafo: " + idSismografo);
        System.out.println("📍 Nuevo estado: " + nombreEstado);
        System.out.println("🕒 Fecha y hora del cambio: " + fechaHoraRegistro);
        System.out.println("👤 Responsable del cierre: " + responsable);
        System.out.println("📋 Motivos asociados:");

        for (String motivo : motivosYComentarios) {
            System.out.println("   - " + motivo);
        }

        System.out.println("=====================================================\n");
    }
}
