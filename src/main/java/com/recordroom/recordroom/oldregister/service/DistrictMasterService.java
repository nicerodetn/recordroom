package com.recordroom.recordroom.oldregister.service;

import com.recordroom.recordroom.oldregister.repo.DistrictMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictMasterService {

    @Autowired
    private DistrictMasterRepository repository;

    public List<String> getAllTaluks() {
        return repository.findDistinctTalukNames();
    }

    public List<String> getVillagesByTaluk(String taluk) {
        return repository.findVillagesByTaluk(taluk);
    }
}