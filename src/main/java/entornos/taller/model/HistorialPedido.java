package entornos.taller.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "historial_pedido")
public class HistorialPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @NotNull(message = "El pedido es obligatorio")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cambio", nullable = false)
    @NotNull(message = "El tipo de cambio es obligatorio")
    private TipoCambio tipoCambio;

    @Column(name = "valor_anterior", columnDefinition = "TEXT")
    private String valorAnterior;

    @Column(name = "valor_nuevo", columnDefinition = "TEXT")
    private String valorNuevo;


    @ManyToOne()
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "Debe haber un usuario asociado a este cambio")
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "fecha_cambio", updatable = false)
    private Instant fechaCambio;

    public HistorialPedido(Pedido pedido, TipoCambio tipoCambio, String valorAnterior, String valorNuevo, Usuario usuario) {
        this.pedido = pedido;
        this.tipoCambio = tipoCambio;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
        this.usuario = usuario;
    }

    public HistorialPedido() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public TipoCambio getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(TipoCambio tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    public void setValorNuevo(String valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    public Instant getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Instant fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

