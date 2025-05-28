package entornos.taller.model;

import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Column(name = "telefono_recogida")
    @NotBlank(message = "El telefono de recogida es obligatorio")
    @Size(max = 15, message = "El teléfono no puede superar los 15 caracteres")
    private String telefonoRecogida;

    @Column(name = "direccion_recogida")
    @NotBlank(message = "La direccion de recogida es obligatoria")
    @Size(max = 255, message = "La direccion no puede superar los 255 caracteres")
    private String direccionRecogida;

    @Column(name = "telefono_entrega")
    @NotBlank(message = "El telefono de entregada es obligatorio")
    @Size(max = 15, message = "El teléfono no puede superar los 15 caracteres")
    private String telefonoEntrega;

    @Column(name = "direccion_entrega")
    @NotBlank(message = "La direccion de entregada es obligatoria")
    @Size(max = 255, message = "La direccion no puede superar los 255 caracteres")
    private String direccionEntrega;

    @Email(message = "Debe ser un email valido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "mensajeria_id", nullable = false)
    @NotNull(message = "La empresa es obligatoria")
    private EmpresaMensajeria empresaMensajeria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefonoRecogida() {
        return telefonoRecogida;
    }

    public void setTelefonoRecogida(String telefonoRecogida) {
        this.telefonoRecogida = telefonoRecogida;
    }

    public String getDireccionRecogida() {
        return direccionRecogida;
    }

    public void setDireccionRecogida(String direccionRecogida) {
        this.direccionRecogida = direccionRecogida;
    }

    public String getTelefonoEntrega() {
        return telefonoEntrega;
    }

    public void setTelefonoEntrega(String telefonoEntrega) {
        this.telefonoEntrega = telefonoEntrega;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmpresaMensajeria getEmpresaMensajeria() {
        return empresaMensajeria;
    }

    public void setEmpresaMensajeria(EmpresaMensajeria empresa) {
        this.empresaMensajeria = empresa;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente other = (Cliente) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
