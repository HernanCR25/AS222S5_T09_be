package pe.edu.vallegrande.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("consultas")
public class ChatGptModel {

    @Id
    private Long id;

    @Column("consulta")
    private String consulta;

    @Column("respuesta")
    private String respuesta;

    @Column("hora")
    private LocalDateTime hora;

    @Column("estado")
    private String estado;
}
