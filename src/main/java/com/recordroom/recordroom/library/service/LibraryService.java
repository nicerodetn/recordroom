package com.recordroom.recordroom.library.service;

import com.recordroom.recordroom.library.entity.Library;
import com.recordroom.recordroom.library.repo.LibraryRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LibraryService  {
    private final LibraryRepository libraryRepository;

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
}
