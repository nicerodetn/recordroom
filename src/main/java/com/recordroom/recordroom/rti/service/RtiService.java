package com.recordroom.recordroom.rti.service;


import com.recordroom.recordroom.rti.entity.Rti;
import com.recordroom.recordroom.rti.repo.RtiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RtiService {

    @Autowired
    private RtiRepository rtiRepository;


    public List<Rti> getAllRti() {
        return rtiRepository.findAll();
    }

    public Optional<Rti> saveRti(Rti s) {
        return Optional.of(rtiRepository.save(s));
    }

    public void deleteSection(Integer id) {
        rtiRepository.deleteById(id);
    }
//test

}
