package br.ufc.quixada.entity;

import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NamedQueries({
        @NamedQuery(name = "hospedePorCpf", query = "select h from Hospede h where h.cpf = :cpf"),
        @NamedQuery(name = "hospedePorId", query = "select h from Hospede h where h.id = :id")
})

@Document(collection = "hospedes")
@Entity
@Table(name = "hospedes") // Essa anotação será usada apenas pelo PostgreSQL
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Hospede {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String nome;
    private String fone;

    @OneToMany(mappedBy = "hospede", cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    @Override
    public String toString() {
        return "Hospede [id=" + id + ", cpf=" + cpf + ", nome=" + nome + "]";
    }

    public String toStringCompleto() {
        return "Hospede [id=" + id + ", cpf=" + cpf + ", nome=" + nome + ", fone=" + fone + "]";
    }

    // Método para adicionar reserva
    public void adicionarReserva(Reserva reserva) {
        if (reservas == null) {
            reservas = new ArrayList<>();
        }
        reservas.add(reserva);
        reserva.setHospede(this);
    }

    // Método para remover reserva
    public void removerReserva(Reserva reserva) {
        if (reservas != null) {
            reservas.remove(reserva);
            reserva.setHospede(null);
        }
    }

    // Método para gerar UUID como ID
    @PrePersist
    private void generateId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    public boolean temReservas() {
        return reservas != null && !reservas.isEmpty();
    }
}
