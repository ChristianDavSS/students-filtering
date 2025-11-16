package com.backend.demo.repository.entity;

import com.backend.demo.component.helpers.NameInterface;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Modality")
public class Modality implements NameInterface {
    @Id
    private String id;
    private String name;
}
