package com.backend.demo.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "modality_id", nullable = false)
    private Modality modality;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "degree", cascade = CascadeType.REMOVE)
    private List<Student> students;

    @OneToMany(mappedBy = "degree", cascade = CascadeType.REMOVE)
    private List<DegreeTeacher> degreeTeachers;
}
