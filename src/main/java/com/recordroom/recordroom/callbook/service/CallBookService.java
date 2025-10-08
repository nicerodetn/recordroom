package com.recordroom.recordroom.callbook.service;

import com.recordroom.recordroom.callbook.entity.CallBook;
import com.recordroom.recordroom.callbook.repo.CallBookRepository;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CallBookService {

    @Autowired
    private CallBookRepository callBookRepository;

    public Optional<CallBook> saveRecord(CallBook r) {
        if (callBookRepository.findByDrSerialNoAndYear(r.getNew_drSerialNo(), r.getNew_dr_year()).isPresent()) {
            return Optional.empty();
        } else {
            return Optional.of(callBookRepository.save(r));
        }
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
}
