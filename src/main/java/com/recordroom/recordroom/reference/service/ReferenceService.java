package com.recordroom.recordroom.reference.service;

import com.recordroom.recordroom.dto.DataTableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;

import com.recordroom.recordroom.reference.entity.Reference;
import com.recordroom.recordroom.reference.repo.ReferenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReferenceService {

    @Autowired
    private ReferenceRepo referenceRepo;

    public Optional<Reference> saveRecord(Reference r) {
        return Optional.of(referenceRepo.save(r));
    }

    public Optional<Reference> updateRecord(Reference r) {
        return Optional.of(referenceRepo.save(r));
    }

    public Optional<Reference> search(Long r) {
        return referenceRepo.findByDrSerialNo(r);
    }

    public List<Reference> allListActive() {
        return referenceRepo.findAllActive();
    }

    /**
     * Finds references using server-side pagination with file type filter and serial number search.
     */
    public Page<Reference> findByFilters(DataTableRequest request, Integer fileType) {

        Specification<Reference> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // A. Filter by File Type (from dropdown)
            if (fileType != null) {
                predicates.add(cb.equal(root.get("type"), fileType));
            }

            // B. Filter by Global Search (Only on serialNo)
            String searchValue = request.getSearch().getValue();
            if (searchValue != null && !searchValue.isEmpty()) {
                try {

                    Long searchSerialNo = Long.parseLong(searchValue);
                    predicates.add(cb.equal(root.get("serialNo"), searchSerialNo));
                } catch (NumberFormatException e) {

                    predicates.add(cb.isFalse(cb.literal(true)));
                }
            }

            // C. Filter by active status
            predicates.add(cb.isTrue(root.get("is_active")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };


        Pageable pageable = PageRequest.of(
                request.getStart() / request.getLength(),
                request.getLength(),
                Sort.unsorted()
        );

        return referenceRepo.findAll(spec, pageable);
    }
}