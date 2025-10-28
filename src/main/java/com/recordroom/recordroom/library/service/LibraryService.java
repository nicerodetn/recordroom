package com.recordroom.recordroom.library.service;

import com.recordroom.recordroom.dto.DataTableRequest;
import com.recordroom.recordroom.library.entity.Library;
import com.recordroom.recordroom.library.repo.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.Predicate;

@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;


    @Autowired
    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public Library saveLibrary(Library library) {
        return libraryRepository.save(library);
    }

    public List<Library> findAllLibraries() {
        return libraryRepository.findAll();
    }

    public Optional<Library> findByBookSerialNo(Integer bookSerialNo) {
        return libraryRepository.findByBook_serial_no(bookSerialNo);
    }


    public Page<Library> findByFilters(DataTableRequest request) {

        Specification<Library> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            String searchValue = request.getSearch().getValue();
            if (searchValue != null && !searchValue.isEmpty()) {
                try {

                    Integer searchSerialNo = Integer.parseInt(searchValue);
                    predicates.add(cb.equal(root.get("book_serial_no"), searchSerialNo));
                } catch (NumberFormatException e) {

                    predicates.add(cb.isFalse(cb.literal(true)));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };


        Pageable pageable = PageRequest.of(
                request.getStart() / request.getLength(),
                request.getLength(),
                Sort.unsorted()
        );

        return libraryRepository.findAll(spec, pageable);
    }
}