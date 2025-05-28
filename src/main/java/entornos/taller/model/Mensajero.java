package entornos.taller.model;

import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "mensajero")
@PrimaryKeyJoinColumn(name = "id") /*la primary key de usuario seria la primary key de Mensajero (herencia)*/
public class Mensajero extends Usuario {

    private Boolean disponibilidad = true;

    public Boolean getDisponibilidad() { return disponibilidad; } 
    public void setDisponibilidad(Boolean disponibilidad) { this.disponibilidad = disponibilidad; }

    @Override
    public String toString() {
        return getNombreUsuario();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mensajero)) return false;
        Mensajero other = (Mensajero) o;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}

