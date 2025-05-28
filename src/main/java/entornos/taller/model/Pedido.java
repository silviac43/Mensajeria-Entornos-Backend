package entornos.taller.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDateTime;


import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "mensajeria_id", nullable = false)
    @NotNull(message = "La empresa es obligatoria")
    private EmpresaMensajeria empresaMensajeria;

    @Column(name = "direccion_recogida")
    @Size(max = 255, message = "La direccion de recogida no puede superar los 255 caracteres")
    private String direccionRecogida;

    @Column(name = "direccion_entrega")
    @Size(max = 255, message = "La direccion de entrega no puede superar los 255 caracteres")
    private String direccionEntrega;

    @Column(name = "telefono_recogida")
    @Size(max = 15, message = "El telefono de recogida no puede superar los 15 caracteres")
    private String telefonoRecogida;

    @Column(name = "telefono_entrega")
    @Size(max = 15, message = "El telefono de entrega no puede superar los 15 caracteres")
    private String telefonoEntrega;

    @Column(name = "tipo_paquete")
    @NotBlank(message = "El tipo de paquete es obligatorio")
    @Size(max = 100, message = "El tipo de paquete no puede superar los 100 caracteres")
    private String tipoPaquete;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "usuario_mensajero_id")
    private Mensajero mensajero;

    @Column(name = "fecha_creacion", updatable = false)
    @CreationTimestamp
    private Instant fechaCreacion;

    @Size(max = 500, message = "Las notas no pueden superar los 500 caracteres")
    private String notas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public EmpresaMensajeria getEmpresaMensajeria() {
        return empresaMensajeria;
    }

    public void setEmpresaMensajeria(EmpresaMensajeria empresa) {
        this.empresaMensajeria = empresa;
    }

    public String getDireccionRecogida() {
        return direccionRecogida;
    }

    public void setDireccionRecogida(String direccionRecogida) {
        this.direccionRecogida = direccionRecogida;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getTelefonoRecogida() {
        return telefonoRecogida;
    }

    public void setTelefonoRecogida(String telefonoRecogida) {
        this.telefonoRecogida = telefonoRecogida;
    }

    public String getTelefonoEntrega() {
        return telefonoEntrega;
    }

    public void setTelefonoEntrega(String telefonoEntrega) {
        this.telefonoEntrega = telefonoEntrega;
    }

    public String getTipoPaquete() {
        return tipoPaquete;
    }

    public void setTipoPaquete(String tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Mensajero getMensajero() {
        return mensajero;
    }

    public void setMensajero(Mensajero mensajero) {
        this.mensajero = mensajero;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
