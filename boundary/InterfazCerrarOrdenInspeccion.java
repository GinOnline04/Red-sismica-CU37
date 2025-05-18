package boundary;

import control.GestorCerrarOrdenInspeccion;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InterfazCerrarOrdenInspeccion extends JFrame {
    private JPanel panelPrincipal;
    private GestorCerrarOrdenInspeccion gestor;
    // private List<String> motivosDisponibles;
    // private List<Pair<String, String>> motivosSeleccionadosConComentarios;

    public InterfazCerrarOrdenInspeccion() {
        super("Menú Principal");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null); // Centrar la ventana

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        JButton botonCerrarOrden = new JButton("Cerrar Orden de Inspección");
        botonCerrarOrden.addActionListener(e -> seleccionOpcionCerrarOrdenInspeccion());

        JPanel panelBoton = new JPanel();
        panelBoton.add(botonCerrarOrden);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panelBoton, BorderLayout.NORTH);
        this.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
    }

    public void seleccionOpcionCerrarOrdenInspeccion() {
        habilitarPantalla();

    }

    public void habilitarPantalla() {
        panelPrincipal.removeAll(); //Limpia pantalla
        JLabel titulo = new JLabel("Esperando Ordenes de Inspección...", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(titulo, BorderLayout.CENTER);
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
        gestor = new GestorCerrarOrdenInspeccion(this);
        gestor.iniciarCierreOrdenInspeccion();
    }

    public void pedirSeleccionOrdenInspeccion(List<String> ordenes) {
        panelPrincipal.removeAll();

        JLabel titulo = new JLabel("Seleccionar Orden:", SwingConstants.CENTER);
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String orden : ordenes) {
            model.addElement(orden);
        }

        JList<String> listaOrdenes = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(listaOrdenes);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(e -> {
            String seleccionada = listaOrdenes.getSelectedValue();
            if (seleccionada != null) {
                tomarOrdenInspeccionSeleccionada(seleccionada);
            }
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnSeleccionar);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }
    public void tomarOrdenInspeccionSeleccionada(String numeroOrden) {
        //gestor.tomarOrdenInspeccionSeleccionada(numeroOrden);
        pedirObservacionCierreOrden();
    }
    public void pedirObservacionCierreOrden() {
        panelPrincipal.removeAll();

        JLabel titulo = new JLabel("Ingrese Observación de Cierre:", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JTextArea campoObservacion = new JTextArea(10, 50);
        JScrollPane scrollPane = new JScrollPane(campoObservacion);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        JButton botonConfirmar = new JButton("Confirmar");
        botonConfirmar.addActionListener(e -> {
            String texto = campoObservacion.getText();
            if (texto != null && !texto.trim().isEmpty()) {
                tomarObservacionCierreOrden(texto);
            } else {
                JOptionPane.showMessageDialog(this, "La observación no puede estar vacía.");
            }
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(botonConfirmar);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }
    public void tomarObservacionCierreOrden(String texto){
        //gestor.tomarObservacionCierreOrden(texto.trim())
        gestor.pedirSeleccionarMotivosFueraServicio();
    }

    public void pedirSeleccionMotivoTipo(List<String> motivosTipo) {
        panelPrincipal.removeAll();

        JLabel titulo = new JLabel("Seleccione un motivo para marcar fuera de servicio", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String motivo : motivosTipo) {
            model.addElement(motivo);
        }

        JList<String> listaMotivos = new JList<>(model);
        JScrollPane scrollMotivos = new JScrollPane(listaMotivos);
        panelPrincipal.add(scrollMotivos, BorderLayout.CENTER);

        JButton btnSeleccionar = new JButton("Seleccionar motivo");
        btnSeleccionar.addActionListener(e -> {
            String motivoSeleccionado = listaMotivos.getSelectedValue();
            if (motivoSeleccionado != null) {
                tomarMotivoTipo(motivoSeleccionado);
            } else {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un motivo.");
            }
        });

        JButton btnFinalizar = new JButton("Finalizar selección de motivos");
        btnFinalizar.addActionListener(e -> gestor.pedirConfirmacionCierreOrden());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnFinalizar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }
    public void tomarMotivoTipo(String motivo) {
        //gestor.tomarMotivoTipo(motivo); // notifica al gestor
        tomarComentario();    // habilita ingreso del comentario
    }

    public void tomarComentario() {
        panelPrincipal.removeAll();

        JLabel titulo = new JLabel("Ingrese comentario para el motivo seleccionado", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JTextArea campoComentario = new JTextArea(10, 50);
        JScrollPane scrollPane = new JScrollPane(campoComentario);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        JButton btnConfirmar = new JButton("Confirmar comentario");
        btnConfirmar.addActionListener(e -> {
            String comentario = campoComentario.getText().trim();
            if (comentario.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El comentario no puede estar vacío.");
            } else {
                //gestor.tomarComentario(comentario);
                // volvemos a mostrar la lista de motivos para seguir el ciclo
                gestor.pedirSeleccionarMotivosFueraServicio();
            }
        });

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnConfirmar);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }
    public void pedirConfirmacionCierreOrden() {
        panelPrincipal.removeAll();

        JLabel titulo = new JLabel("¿Desea confirmar el cierre de la orden?", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JButton btnConfirmar = new JButton("Confirmar cierre orden");
        btnConfirmar.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea confirmar el cierre de la orden?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                tomarConfirmacionCierreOrden(true);
            }
        });

        JButton btnNoConfirmar = new JButton("No confirmar");
        btnNoConfirmar.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea cancelar el cierre de la orden?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                tomarConfirmacionCierreOrden(false);
            }
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnNoConfirmar);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    public void tomarConfirmacionCierreOrden(boolean confirmacion){
        gestor.tomarConfirmacionCierreOrden(confirmacion);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazCerrarOrdenInspeccion().setVisible(true));
}}