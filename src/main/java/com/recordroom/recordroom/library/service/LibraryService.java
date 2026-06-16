package com.recordroom.recordroom.library.service;

import com.recordroom.recordroom.dto.DataTableRequest;
import com.recordroom.recordroom.library.entity.Library;
import com.recordroom.recordroom.library.repo.LibraryRepository;
import com.recordroom.recordroom.library.repo.LibraryTransactionRepository;
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
    private LibraryTransactionRepository transactionRepository;

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

            // Exclude soft-deleted books
            predicates.add(cb.or(
                cb.equal(root.get("deleted"), false),
                cb.isNull(root.get("deleted"))
            ));

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

    public Page<Library> findDeletedByFilters(DataTableRequest request) {
        Specification<Library> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Include only soft-deleted books
            predicates.add(cb.equal(root.get("deleted"), true));

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

    @org.springframework.transaction.annotation.Transactional
    public void deleteLibrary(Integer id) {
        transactionRepository.deleteByLibraryId(id);
        libraryRepository.deleteById(id);
    }

    public void deleteLibrarySoft(Integer id) {
        Optional<Library> bookOpt = libraryRepository.findById(id);
        if (bookOpt.isPresent()) {
            Library book = bookOpt.get();
            book.setDeleted(true);
            book.setDeletedDate(java.time.LocalDate.now());
            libraryRepository.save(book);
        }
    }

    public void restoreLibrary(Integer id) {
        Optional<Library> bookOpt = libraryRepository.findById(id);
        if (bookOpt.isPresent()) {
            Library book = bookOpt.get();
            book.setDeleted(false);
            book.setDeletedDate(null);
            libraryRepository.save(book);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 0 * * ?") // Daily at midnight
    public void cleanExpiredDeletedBooks() {
        java.time.LocalDate cutoff = java.time.LocalDate.now().minusDays(7);
        List<Library> expiredBooks = libraryRepository.findByDeletedTrueAndDeletedDateLessThanEqual(cutoff);
        for (Library l : expiredBooks) {
            deleteLibrary(l.getId());
        }
    }

    public Optional<Library> findById(Integer id) {
        return libraryRepository.findById(id);
    }
}