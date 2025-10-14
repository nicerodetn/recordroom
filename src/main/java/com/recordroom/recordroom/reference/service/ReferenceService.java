package com.recordroom.recordroom.reference.service;


import com.recordroom.recordroom.callbook.entity.CallBook;
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

    public Optional<Reference> updateRecord(Reference r)
    {
        return Optional.of(referenceRepo.save(r));
    }

    public Optional<Reference> search(Long r)
    {
        return referenceRepo.findByDrSerialNo(r);
    }

    public List<Reference> allListActive()
    {
        return referenceRepo.findAllActive();
    }

}
