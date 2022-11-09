package com.company.musicstorecatalog.service;


import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.repository.LabelRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class LabelService {

    private LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public Label getLabelById(long id) {
        return labelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Label found with the id: " + id));
    }

    public Label createLabel(Label label) {
        //        code gather from SMU Challenge 5
        if (label == null) throw new IllegalArgumentException("No Label is passed! Label object is null!");

        return labelRepository.save(label);
    }

    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    public void updateLabelById(Label label) {
        //        code gather from SMU Challenge 5
        if (label == null) {
            throw new IllegalArgumentException("No Artist data is passed! Artist object is null!");
        }
        Label labelToBeEdited = labelRepository.findById(label.getId()).orElseThrow(() -> new IllegalArgumentException("No Label found!"));

        labelToBeEdited.setName(label.getName());
        labelToBeEdited.setWebsite(label.getWebsite());
        labelRepository.save(labelToBeEdited);
    }

    public void deleteLabelById(long id) {
        labelRepository.deleteById(id);
    }
}
