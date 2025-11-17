package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sismografos")
public class Sismografo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date fechaAdquisicion;

    @Column(nullable = false, unique = true)
    private String identificadorSismografo;

    @Column(unique = true)
    private int nroSerie;

    @ManyToOne
    @JoinColumn(name = "estacion_id")
    private EstacionSismologica estacionSismologica;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estado_actual_id")
    private CambioEstado estadoActual;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    // Constructor sin argumentos para JPA
    public Sismografo() {
    }

    public Sismografo(Date fechaAdquisicion, String identificadorSismografo, int nroSerie,
            EstacionSismologica estacionSismologica, CambioEstado estadoActual, Estado estado) {
        this.fechaAdquisicion = fechaAdquisicion;
        this.identificadorSismografo = identificadorSismografo;
        this.nroSerie = nroSerie;
        this.estacionSismologica = estacionSismologica;
        this.estadoActual = estadoActual;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getIdentificadorSismografo() {
        return identificadorSismografo;
    }

    public void fueraDeServicio(List<MotivoTipo> motivos, Empleado responsable, List<String> comentarios) {
        estadoActual.setFechaHoraFin(LocalDateTime.now());

        CambioEstado nuevoEstado = new CambioEstado(LocalDateTime.now(), null, responsable, estado);
        nuevoEstado.setNombreEstado(estado.getNombreEstado());

        nuevoEstado.setMotivos(motivos, comentarios);

        this.actualizarEstado(nuevoEstado);
    }

    public void actualizarEstado(CambioEstado nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }
}