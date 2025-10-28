package com.recordroom.recordroom.callbook.service;

import com.recordroom.recordroom.callbook.entity.CallBook;
import com.recordroom.recordroom.callbook.repo.CallBookRepository;
import com.recordroom.recordroom.dto.DataTableRequest;
import jakarta.persistence.criteria.Predicate;
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

@Service
public class CallBookService {

    @Autowired
    private CallBookRepository callBookRepository;

    public Optional<CallBook> saveRecord(CallBook r) {

        return Optional.of(callBookRepository.save(r));
    }

    public Optional<CallBook> updateRecord(CallBook r) {
        return Optional.of(callBookRepository.save(r));
    }

    public Optional<CallBook> findBydrSerialNoAndYear(Integer new_drSerialNo, Integer new_dr_year) {
        return callBookRepository.findByDrSerialNoAndYear(new_drSerialNo, new_dr_year);
    }

    public Optional<CallBook> findForDuplicate(Integer new_drSerialNo, Integer new_dr_year) {
        return callBookRepository.findForDuplicate(new_drSerialNo, new_dr_year);
    }

    public Optional<CallBook> findBydrSerialNoAndYearInward(Integer new_drSerialNo, Integer new_dr_year) {
        return callBookRepository.findByDrSerialNoAndYearForINward(new_drSerialNo, new_dr_year);
    }
    public List<CallBook> findByUniqueKey(Integer unique_key) {
        return callBookRepository.findByUniqueKey(unique_key);
    }


    public Optional<CallBook> findById(Integer id) {
        return callBookRepository.findById(id);
    }


    public List<CallBook> findByActive() {
        return callBookRepository.findByActive();
    }


    public Page<CallBook> findByFilters(DataTableRequest request) {

        Specification<CallBook> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            predicates.add(cb.isTrue(root.get("is_current")));


            String searchValue = request.getSearch().getValue();
            if (searchValue != null && !searchValue.isEmpty()) {
                try {

                    Integer searchSerialNo = Integer.parseInt(searchValue);
                    predicates.add(cb.equal(root.get("new_drSerialNo"), searchSerialNo));
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

        return callBookRepository.findAll(spec, pageable);
    }
}