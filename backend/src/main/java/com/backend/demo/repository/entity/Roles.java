package com.backend.demo.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 35, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "roles")
    private List<DegreeTeacher> degreeTeachers;
}
