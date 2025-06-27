package com.xyzwps.libs.demo.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "flow_info")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowInfo extends PanacheEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String dedupKey;
    private String description;
    private String data;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
