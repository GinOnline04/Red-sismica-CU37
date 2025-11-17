package boundary;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import control.GestorCerrarOrdenInspeccion;
import entity.MotivoTipo;
import entity.OrdenDeInspeccion;
import entity.Sesion;
import entity.Usuario;
import repository.UsuarioRepository;

/**
 * Interfaz gráfica mejorada para el caso de uso "Cerrar Orden de Inspección"
 * Implementa un diseño profesional con navegación por pasos y feedback visual
 * claro.
 * 
 * @author Sistema de Red Sísmica
 * @version 2.0 - Versión mejorada con UX profesional
 */
public class InterfazCerrarOrdenInspeccion extends JFrame {

    // ========== CONSTANTES DE DISEÑO ==========
    private static final Color COLOR_PRIMARY = new Color(41, 128, 185); // Azul corporativo
    private static final Color COLOR_PRIMARY_DARK = new Color(31, 97, 141); // Azul oscuro
    private static final Color COLOR_SUCCESS = new Color(39, 174, 96); // Verde
    private static final Color COLOR_BACKGROUND = new Color(236, 240, 241); // Gris claro
    private static final Color COLOR_CARD = Color.WHITE; // Blanco
    private static final Color COLOR_TEXT = new Color(44, 62, 80); // Gris oscuro
    private static final Color COLOR_TEXT_LIGHT = new Color(127, 140, 141); // Gris medio

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    // ========== COMPONENTES PRINCIPALES ==========
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private GestorCerrarOrdenInspeccion gestor;
    private InterfazNotificacionMail interfazNotificacionMail;
    private InterfazMonitorCCRS interfazMonitorCCRS;

    // ========== PANELES DE NAVEGACIÓN ==========
    private JPanel panelMenu;
    private JPanel panelSeleccionOrden;
    private JPanel panelObservacion;
    private JPanel panelMotivos;
    private JPanel panelConfirmacion;

    // ========== COMPONENTES DE DATOS ==========
    private JTable tablaOrdenes;
    private OrdenesTableModel modeloTabla;
    private JTextArea areaObservacion;
    private JList<String> listaMotivos;
    private DefaultListModel<String> modeloMotivos;
    private List<MotivoTipo> motivosDisponibles;
    private List<String> motivosSeleccionadosTexto;
    private List<String> comentariosIngresados;

    // ========== INDICADOR DE PROGRESO ==========
    private JLabel lblPaso;

    public InterfazCerrarOrdenInspeccion() {
        super("Sistema de Gestión de Red Sísmica");
        configurarVentana();
        inicializarComponentes();
        construirInterfaz();
    }

    // ========== CONFIGURACIÓN INICIAL ==========

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        // Configurar look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, continuar con el Look and Feel por defecto
        }
    }

    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.setBackground(COLOR_BACKGROUND);

        motivosSeleccionadosTexto = new ArrayList<>();
        comentariosIngresados = new ArrayList<>();

        interfazNotificacionMail = new InterfazNotificacionMail();
        interfazMonitorCCRS = new InterfazMonitorCCRS();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout());

        // Panel superior con título y progreso
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);

        // Panel principal con cards
        add(panelPrincipal, BorderLayout.CENTER);

        // Crear todas las pantallas
        crearPantallaMenu();
        crearPantallaSeleccionOrden();
        crearPantallaObservacion();
        crearPantallaMotivos();
        crearPantallaConfirmacion();

        // Mostrar menú inicial
        cardLayout.show(panelPrincipal, "MENU");
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARY);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Título
        JLabel lblTitulo = new JLabel("🌐 Sistema de Red Sísmica");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panel.add(lblTitulo, BorderLayout.WEST);

        // Indicador de paso
        lblPaso = new JLabel("");
        lblPaso.setFont(FONT_SMALL);
        lblPaso.setForeground(new Color(236, 240, 241));
        lblPaso.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblPaso, BorderLayout.EAST);

        return panel;
    }

    // ========== PANTALLAS DEL FLUJO ==========

    private void crearPantallaMenu() {
        panelMenu = new JPanel(new GridBagLayout());
        panelMenu.setBackground(COLOR_BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Card centrada
        JPanel card = crearCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Título
        JLabel titulo = new JLabel("Menú Principal");
        titulo.setFont(FONT_TITLE);
        titulo.setForeground(COLOR_TEXT);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titulo);

        card.add(Box.createVerticalStrut(10));

        // Subtítulo
        JLabel subtitulo = new JLabel("Seleccione una opción para continuar");
        subtitulo.setFont(FONT_NORMAL);
        subtitulo.setForeground(COLOR_TEXT_LIGHT);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitulo);

        card.add(Box.createVerticalStrut(40));

        // Botón principal
        JButton btnCerrarOrden = crearBotonPrimario("📋 Cerrar Orden de Inspección");
        btnCerrarOrden.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrarOrden.addActionListener(e -> {
            UsuarioRepository usuarioRepo = new UsuarioRepository();
            Usuario usuario = usuarioRepo.findByNombreUsuario("lucia.g")
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Sesion sesion = new Sesion(usuario, LocalDateTime.now());
            seleccionOpcionCerrarOrdenInspeccion(sesion);
        });
        card.add(btnCerrarOrden);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelMenu.add(card, gbc);

        panelPrincipal.add(panelMenu, "MENU");
    }

    private void crearPantallaSeleccionOrden() {
        panelSeleccionOrden = new JPanel(new BorderLayout(0, 20));
        panelSeleccionOrden.setBackground(COLOR_BACKGROUND);
        panelSeleccionOrden.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Título
        JPanel panelTitulo = crearPanelTitulo(
                "Seleccionar Orden de Inspección",
                "Seleccione la orden que desea cerrar");
        panelSeleccionOrden.add(panelTitulo, BorderLayout.NORTH);

        // Tabla de órdenes (se llenará dinámicamente)
        modeloTabla = new OrdenesTableModel();
        tablaOrdenes = new JTable(modeloTabla);
        configurarTabla(tablaOrdenes);

        JScrollPane scrollTabla = new JScrollPane(tablaOrdenes);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panelSeleccionOrden.add(scrollTabla, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(COLOR_BACKGROUND);

        JButton btnVolver = crearBotonSecundario("← Volver");
        btnVolver.addActionListener(e -> {
            cardLayout.show(panelPrincipal, "MENU");
            actualizarPaso("");
        });

        JButton btnSeleccionar = crearBotonPrimario("Seleccionar →");
        btnSeleccionar.addActionListener(e -> seleccionarOrden());

        panelBotones.add(btnVolver);
        panelBotones.add(btnSeleccionar);
        panelSeleccionOrden.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelSeleccionOrden, "SELECCION_ORDEN");
    }

    private void crearPantallaObservacion() {
        panelObservacion = new JPanel(new BorderLayout(0, 20));
        panelObservacion.setBackground(COLOR_BACKGROUND);
        panelObservacion.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Título
        JPanel panelTitulo = crearPanelTitulo(
                "Observaciones de Cierre",
                "Ingrese las observaciones finales de la orden de inspección");
        panelObservacion.add(panelTitulo, BorderLayout.NORTH);

        // Área de texto
        JPanel panelCentro = crearCard();
        panelCentro.setLayout(new BorderLayout(0, 10));
        panelCentro.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblInstruccion = new JLabel("Observaciones:");
        lblInstruccion.setFont(FONT_SUBTITLE);
        lblInstruccion.setForeground(COLOR_TEXT);
        panelCentro.add(lblInstruccion, BorderLayout.NORTH);

        areaObservacion = new JTextArea(8, 40);
        areaObservacion.setFont(FONT_NORMAL);
        areaObservacion.setLineWrap(true);
        areaObservacion.setWrapStyleWord(true);
        areaObservacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(10, 10, 10, 10)));

        JScrollPane scrollObservacion = new JScrollPane(areaObservacion);
        panelCentro.add(scrollObservacion, BorderLayout.CENTER);

        panelObservacion.add(panelCentro, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(COLOR_BACKGROUND);

        JButton btnVolver = crearBotonSecundario("← Volver");
        btnVolver.addActionListener(e -> {
            cardLayout.show(panelPrincipal, "SELECCION_ORDEN");
            actualizarPaso("Paso 1/4: Selección de Orden");
        });

        JButton btnContinuar = crearBotonPrimario("Continuar →");
        btnContinuar.addActionListener(e -> confirmarObservacion());

        panelBotones.add(btnVolver);
        panelBotones.add(btnContinuar);
        panelObservacion.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelObservacion, "OBSERVACION");
    }

    private void crearPantallaMotivos() {
        panelMotivos = new JPanel(new BorderLayout(0, 20));
        panelMotivos.setBackground(COLOR_BACKGROUND);
        panelMotivos.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Título
        JPanel panelTitulo = crearPanelTitulo(
                "Motivos de Fuera de Servicio",
                "Seleccione los motivos y agregue comentarios para cada uno");
        panelMotivos.add(panelTitulo, BorderLayout.NORTH);

        // Panel central dividido
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCentro.setBackground(COLOR_BACKGROUND);

        // Panel izquierdo: Lista de motivos
        JPanel panelIzq = crearCard();
        panelIzq.setLayout(new BorderLayout(0, 10));
        panelIzq.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblMotivos = new JLabel("Motivos Disponibles:");
        lblMotivos.setFont(FONT_SUBTITLE);
        lblMotivos.setForeground(COLOR_TEXT);
        panelIzq.add(lblMotivos, BorderLayout.NORTH);

        modeloMotivos = new DefaultListModel<>();
        listaMotivos = new JList<>(modeloMotivos);
        listaMotivos.setFont(FONT_NORMAL);
        listaMotivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollMotivos = new JScrollPane(listaMotivos);
        scrollMotivos.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panelIzq.add(scrollMotivos, BorderLayout.CENTER);

        JButton btnAgregarMotivo = crearBotonPrimario("+ Agregar Motivo");
        btnAgregarMotivo.addActionListener(e -> agregarMotivoConComentario());
        panelIzq.add(btnAgregarMotivo, BorderLayout.SOUTH);

        // Panel derecho: Motivos seleccionados
        JPanel panelDer = crearCard();
        panelDer.setLayout(new BorderLayout(0, 10));
        panelDer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblSeleccionados = new JLabel("Motivos Seleccionados:");
        lblSeleccionados.setFont(FONT_SUBTITLE);
        lblSeleccionados.setForeground(COLOR_TEXT);
        panelDer.add(lblSeleccionados, BorderLayout.NORTH);

        JTextArea areaSeleccionados = new JTextArea();
        areaSeleccionados.setEditable(false);
        areaSeleccionados.setFont(FONT_SMALL);
        areaSeleccionados.setLineWrap(true);
        areaSeleccionados.setWrapStyleWord(true);
        areaSeleccionados.setText("No hay motivos seleccionados aún.");
        areaSeleccionados.setForeground(COLOR_TEXT_LIGHT);

        JScrollPane scrollSeleccionados = new JScrollPane(areaSeleccionados);
        scrollSeleccionados.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panelDer.add(scrollSeleccionados, BorderLayout.CENTER);

        panelCentro.add(panelIzq);
        panelCentro.add(panelDer);
        panelMotivos.add(panelCentro, BorderLayout.CENTER);

        // Guardar referencia al área de seleccionados para actualizarla
        panelMotivos.putClientProperty("areaSeleccionados", areaSeleccionados);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(COLOR_BACKGROUND);

        JButton btnVolver = crearBotonSecundario("← Volver");
        btnVolver.addActionListener(e -> {
            cardLayout.show(panelPrincipal, "OBSERVACION");
            actualizarPaso("Paso 2/4: Observaciones");
        });

        JButton btnFinalizar = crearBotonPrimario("Finalizar Selección →");
        btnFinalizar.addActionListener(e -> finalizarSeleccionMotivos());

        panelBotones.add(btnVolver);
        panelBotones.add(btnFinalizar);
        panelMotivos.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelMotivos, "MOTIVOS");
    }

    private void crearPantallaConfirmacion() {
        panelConfirmacion = new JPanel(new BorderLayout(0, 20));
        panelConfirmacion.setBackground(COLOR_BACKGROUND);
        panelConfirmacion.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Título
        JPanel panelTitulo = crearPanelTitulo(
                "Confirmación de Cierre",
                "Revise la información antes de confirmar el cierre de la orden");
        panelConfirmacion.add(panelTitulo, BorderLayout.NORTH);

        // Área de resumen (se llenará dinámicamente)
        JPanel panelResumen = crearCard();
        panelResumen.setLayout(new BorderLayout());
        panelResumen.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextArea areaResumen = new JTextArea();
        areaResumen.setEditable(false);
        areaResumen.setFont(FONT_NORMAL);
        areaResumen.setLineWrap(true);
        areaResumen.setWrapStyleWord(true);

        JScrollPane scrollResumen = new JScrollPane(areaResumen);
        scrollResumen.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panelResumen.add(scrollResumen, BorderLayout.CENTER);

        panelConfirmacion.add(panelResumen, BorderLayout.CENTER);
        panelConfirmacion.putClientProperty("areaResumen", areaResumen);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(COLOR_BACKGROUND);

        JButton btnVolver = crearBotonSecundario("← Volver");
        btnVolver.addActionListener(e -> {
            cardLayout.show(panelPrincipal, "MOTIVOS");
            actualizarPaso("Paso 3/4: Motivos");
        });

        JButton btnCancelar = crearBotonSecundario("✕ Cancelar");
        btnCancelar.addActionListener(e -> cancelarCierre());

        JButton btnConfirmar = crearBotonExito("✓ Confirmar Cierre");
        btnConfirmar.addActionListener(e -> confirmarCierre());

        panelBotones.add(btnVolver);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnConfirmar);
        panelConfirmacion.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelConfirmacion, "CONFIRMACION");
    }

    // ========== MÉTODOS AUXILIARES DE UI ==========

    private JPanel crearCard() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(0, 0, 0, 0)));
        return panel;
    }

    private JPanel crearPanelTitulo(String titulo, String subtitulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BACKGROUND);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setForeground(COLOR_TEXT);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitulo);

        panel.add(Box.createVerticalStrut(5));

        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(FONT_NORMAL);
        lblSubtitulo.setForeground(COLOR_TEXT_LIGHT);
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblSubtitulo);

        return panel;
    }

    private JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FONT_NORMAL);
        boton.setBackground(COLOR_PRIMARY);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(200, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_PRIMARY_DARK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_PRIMARY);
            }
        });

        return boton;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FONT_NORMAL);
        boton.setBackground(new Color(149, 165, 166));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(150, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(127, 140, 141));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(149, 165, 166));
            }
        });

        return boton;
    }

    private JButton crearBotonExito(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FONT_NORMAL);
        boton.setBackground(COLOR_SUCCESS);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(200, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(30, 130, 76));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_SUCCESS);
            }
        });

        return boton;
    }

    private void configurarTabla(JTable tabla) {
        tabla.setFont(FONT_NORMAL);
        tabla.setRowHeight(35);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setGridColor(new Color(189, 195, 199));
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(1, 1));

        // Configurar header con renderer personalizado
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        // Renderer personalizado para el header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(52, 73, 94));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Alternar colores de filas
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setText(value != null ? value.toString() : "");

                if (isSelected) {
                    setBackground(new Color(52, 152, 219));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(236, 240, 241));
                    setForeground(COLOR_TEXT);
                }

                setBorder(new EmptyBorder(5, 10, 5, 10));
                return this;
            }
        };
        tabla.setDefaultRenderer(Object.class, renderer);
    }

    private void actualizarPaso(String paso) {
        lblPaso.setText(paso);
    }

    // ========== MÉTODOS DE ACCIÓN ==========

    private void seleccionarOrden() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una orden de inspección de la tabla.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String numeroOrden = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        tomarOrdenInspeccionSeleccionada(numeroOrden);
    }

    private void confirmarObservacion() {
        String observacion = areaObservacion.getText().trim();
        if (observacion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingrese las observaciones de cierre.",
                    "Observaciones requeridas",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        tomarObservacionCierreOrden(observacion);
    }

    private void agregarMotivoConComentario() {
        String motivoSeleccionado = listaMotivos.getSelectedValue();
        if (motivoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un motivo de la lista.",
                    "Motivo requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String comentario = JOptionPane.showInputDialog(this,
                "Ingrese un comentario para el motivo seleccionado:\n" + motivoSeleccionado,
                "Comentario del Motivo",
                JOptionPane.PLAIN_MESSAGE);

        if (comentario == null || comentario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El comentario no puede estar vacío.",
                    "Comentario requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Notificar al gestor
        gestor.tomarMotivoTipo(motivoSeleccionado, motivosDisponibles);
        gestor.tomarComentario(comentario.trim());

        // Actualizar lista visual
        motivosSeleccionadosTexto.add(motivoSeleccionado + ": " + comentario.trim());
        actualizarVistaMotivosSeleccionados();
    }

    private void actualizarVistaMotivosSeleccionados() {
        JTextArea area = (JTextArea) panelMotivos.getClientProperty("areaSeleccionados");
        if (area != null) {
            if (motivosSeleccionadosTexto.isEmpty()) {
                area.setText("No hay motivos seleccionados aún.");
                area.setForeground(COLOR_TEXT_LIGHT);
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < motivosSeleccionadosTexto.size(); i++) {
                    sb.append((i + 1)).append(". ").append(motivosSeleccionadosTexto.get(i)).append("\n\n");
                }
                area.setText(sb.toString());
                area.setForeground(COLOR_TEXT);
            }
        }
    }

    private void finalizarSeleccionMotivos() {
        if (motivosSeleccionadosTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar al menos un motivo antes de continuar.",
                    "Motivos requeridos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        gestor.pedirConfirmacionCierreOrden();
    }

    private void cancelarCierre() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cancelar el cierre de la orden?\n" +
                        "Se perderán todos los datos ingresados.",
                "Confirmar Cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            limpiarFormulario();
            cardLayout.show(panelPrincipal, "MENU");
            actualizarPaso("");
        }
    }

    private void confirmarCierre() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Confirma el cierre de la orden de inspección?\n" +
                        "Esta acción no se puede deshacer.",
                "Confirmar Cierre",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            tomarConfirmacionCierreOrden(true);
            mostrarExito();
        }
    }

    private void mostrarExito() {
        JOptionPane.showMessageDialog(this,
                "La orden de inspección ha sido cerrada exitosamente.\n" +
                        "Se han enviado las notificaciones correspondientes.",
                "Cierre Exitoso",
                JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();
        cardLayout.show(panelPrincipal, "MENU");
        actualizarPaso("");
    }

    private void limpiarFormulario() {
        if (areaObservacion != null) {
            areaObservacion.setText("");
        }
        motivosSeleccionadosTexto.clear();
        comentariosIngresados.clear();
        modeloTabla.limpiar();
        
        // Actualizar la vista de motivos seleccionados
        actualizarVistaMotivosSeleccionados();
    }

    // ========== MÉTODOS PÚBLICOS (INTERFAZ CON GESTOR) ==========

    public void seleccionOpcionCerrarOrdenInspeccion(Sesion sesion) {
        habilitarPantalla(sesion);
    }

    public void habilitarPantalla(Sesion sesion) {
        // Limpiar datos previos ANTES de crear el gestor
        limpiarFormulario();
        
        gestor = new GestorCerrarOrdenInspeccion(this, interfazNotificacionMail, interfazMonitorCCRS, sesion);
        gestor.iniciarCierreOrdenInspeccion();
    }

    public void pedirSeleccionOrdenInspeccion(List<OrdenDeInspeccion> ordenesCompletamenteRealizadas) {
        if (ordenesCompletamenteRealizadas == null || ordenesCompletamenteRealizadas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No tiene órdenes de inspección completamente realizadas.",
                    "Sin Órdenes Disponibles",
                    JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(panelPrincipal, "MENU");
            return;
        }

        // Cargar órdenes en la tabla
        modeloTabla.cargarOrdenes(ordenesCompletamenteRealizadas);

        // Mostrar pantalla de selección
        cardLayout.show(panelPrincipal, "SELECCION_ORDEN");
        actualizarPaso("Paso 1/4: Selección de Orden");
    }

    public void tomarOrdenInspeccionSeleccionada(String numeroOrden) {
        gestor.tomarOrdenInspeccionSeleccionada(numeroOrden);
    }

    public void pedirObservacionCierreOrden() {
        cardLayout.show(panelPrincipal, "OBSERVACION");
        actualizarPaso("Paso 2/4: Observaciones");
        areaObservacion.setText("");
        areaObservacion.requestFocus();
    }

    public void tomarObservacionCierreOrden(String texto) {
        System.out.println("OBSERVACION: " + texto);
        gestor.tomarObservacionCierreOrden(texto.trim());
    }

    public void pedirSeleccionMotivoTipoYComentario(List<MotivoTipo> motivosTipo) {
        this.motivosDisponibles = motivosTipo;

        // Cargar motivos en la lista
        modeloMotivos.clear();
        for (MotivoTipo motivo : motivosTipo) {
            modeloMotivos.addElement(motivo.toString());
        }

        // Mostrar pantalla de motivos
        cardLayout.show(panelPrincipal, "MOTIVOS");
        actualizarPaso("Paso 3/4: Motivos");
    }

    public void tomarMotivoTipo(String motivo, List<MotivoTipo> motivosTipo) {
        // Este método es llamado desde agregarMotivoConComentario()
        // No hace falta acción adicional aquí
    }

    public void tomarComentario() {
        // Este método es llamado desde agregarMotivoConComentario()
        // No hace falta acción adicional aquí
    }

    public void pedirConfirmacionCierreOrden() {
        // Generar resumen
        JTextArea areaResumen = (JTextArea) panelConfirmacion.getClientProperty("areaResumen");
        if (areaResumen != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════════\n");
            sb.append("         RESUMEN DE CIERRE DE ORDEN\n");
            sb.append("═══════════════════════════════════════════════════\n\n");

            sb.append("📋 OBSERVACIONES:\n");
            sb.append(areaObservacion.getText()).append("\n\n");

            sb.append("🔧 MOTIVOS SELECCIONADOS (").append(motivosSeleccionadosTexto.size()).append("):\n\n");
            for (int i = 0; i < motivosSeleccionadosTexto.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(motivosSeleccionadosTexto.get(i)).append("\n");
            }

            sb.append("\n═══════════════════════════════════════════════════\n");
            sb.append("Por favor, revise la información y confirme el cierre.\n");
            sb.append("═══════════════════════════════════════════════════");

            areaResumen.setText(sb.toString());
        }

        cardLayout.show(panelPrincipal, "CONFIRMACION");
        actualizarPaso("Paso 4/4: Confirmación");
    }

    public void tomarConfirmacionCierreOrden(boolean confirmacion) {
        gestor.tomarConfirmacionCierreOrden(confirmacion);
    }

    // ========== CLASE INTERNA: TABLE MODEL ==========

    private class OrdenesTableModel extends AbstractTableModel {
        private final String[] columnas = { "Nº Orden", "Fecha Inicio", "Fecha Fin", "Estación", "Estado" };
        private List<OrdenDeInspeccion> ordenes;
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        public OrdenesTableModel() {
            this.ordenes = new ArrayList<>();
        }

        public void cargarOrdenes(List<OrdenDeInspeccion> ordenes) {
            this.ordenes = new ArrayList<>(ordenes);
            fireTableDataChanged();
        }

        public void limpiar() {
            this.ordenes.clear();
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return ordenes.size();
        }

        @Override
        public int getColumnCount() {
            return columnas.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnas[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            OrdenDeInspeccion orden = ordenes.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return orden.getNumeroDeOrdenDeInspeccion();
                case 1:
                    return orden.getFechaHoraInicio() != null ? orden.getFechaHoraInicio().format(formatter) : "-";
                case 2:
                    return orden.getFechaFinalizacion() != null ? orden.getFechaFinalizacion().format(formatter) : "-";
                case 3:
                    return orden.getEstacion() != null ? orden.getEstacion().getNombreEstacion() : "-";
                case 4:
                    return orden.getEstado() != null ? orden.getEstado().getNombre() : "-";
                default:
                    return "";
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    // ========== MAIN PARA TESTING ==========

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazCerrarOrdenInspeccion().setVisible(true));
    }
}