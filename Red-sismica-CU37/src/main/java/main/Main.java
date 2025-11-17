package main;

import boundary.InterfazCerrarOrdenInspeccion;
import entity.*;
import repository.*;

import javax.swing.*;
import java.time.LocalDateTime;

/**
 * Clase principal para iniciar la aplicación de gestión de red sísmica.
 * Carga datos desde BD H2 y muestra la interfaz gráfica.
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            // Cargar datos iniciales en la base de datos
            System.out.println("Cargando datos iniciales...");
            DataLoader.loadInitialData();
            
            // Iniciar la aplicación en el hilo de eventos de Swing
            SwingUtilities.invokeLater(() -> {
                try {
                    // Simular login - Buscar usuario y crear sesión
                    UsuarioRepository usuarioRepo = new UsuarioRepository();
                    Usuario usuario = usuarioRepo.findByNombreUsuario("lucia.g")
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    
                    // Crear sesión para el usuario logueado
                    Sesion sesion = new Sesion(usuario, LocalDateTime.now());
                    
                    // Crear y mostrar la interfaz principal
                    InterfazCerrarOrdenInspeccion interfaz = new InterfazCerrarOrdenInspeccion();
                    interfaz.setVisible(true);
                    
                    // Iniciar el flujo con la sesión
                    interfaz.seleccionOpcionCerrarOrdenInspeccion(sesion);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error al iniciar la aplicación: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error cargando datos iniciales: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
