package com.recordroom.recordroom.Service.rti;


import com.recordroom.recordroom.Entity.Section;
import com.recordroom.recordroom.Entity.closed.FileRecord;
import com.recordroom.recordroom.Entity.rti.Rti;
import com.recordroom.recordroom.Repository.rti.RtiRepository;
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


}
