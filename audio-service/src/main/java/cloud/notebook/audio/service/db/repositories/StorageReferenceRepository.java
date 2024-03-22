package cloud.notebook.audio.service.db.repositories;

import cloud.notebook.audio.service.db.entries.StorageReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageReferenceRepository extends JpaRepository<StorageReference, String> {
}
