package com.recordroom.recordroom.closed.repo;

import com.recordroom.recordroom.closed.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer>
{
    @Query("SELECT DISTINCT s.description FROM Section s ORDER BY s.description")
    List<String> findDistinctCategories();

}
