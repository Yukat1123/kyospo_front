package com.example.kyoto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpotService {

    @Autowired
    private SpotRepository spotRepository;

    public List<Spot> findByTagIdContaining(String tagID) {
        return spotRepository.findByTagIdContaining(tagID);
    }
}
