package cloud.notebook.audio.service.db.entries;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "storage_reference")
public class StorageReference {

    @Id
    private String uuid;

    @Column(name = "s3_key")
    private String s3Key;
}
