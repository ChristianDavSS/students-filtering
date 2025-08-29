package com.backend.demo.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @Column(length = 8)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String generation;

    @ManyToOne
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    @ManyToOne
    @JoinColumn(name = "degree_id", nullable = false)
    private Degree degree;
}
