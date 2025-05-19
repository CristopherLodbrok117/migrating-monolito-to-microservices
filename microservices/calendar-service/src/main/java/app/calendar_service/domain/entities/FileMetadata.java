package app.calendar_service.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FileMetadata {

    private Long id;

    private short tag;

    private String name;

    private String path;

    private String fileType;

    private long size;

    private LocalDateTime uploadedAt;

    private Long uploaderId;

    private Long groupId;

}
