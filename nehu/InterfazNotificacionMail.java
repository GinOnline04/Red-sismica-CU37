package nehu;

public class InterfazNotificacionMail {

    public void enviarNotificacion(String destinatario, String asunto, String cuerpo) {
        System.out.println("📧 Simulación de envío de correo:");
        System.out.println("-------------------------------------------------");
        System.out.println("Para: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Cuerpo:");
        System.out.println(cuerpo);
        System.out.println("-------------------------------------------------");
        System.out.println();
    }
    
}