package com.company.musicstorecatalog.controller;


import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.service.ArtistService;
import com.company.musicstorecatalog.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "label")
public class LabelController {


    @Autowired
    private LabelService labelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Label createLabel(@RequestBody @Valid Label label) {
        return labelService.createLabel(label);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Label getById(@PathVariable long id) {
        return labelService.getLabelById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Label> getAllLabelS() {
        return labelService.getAllLabels();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabel(@RequestBody @Valid Label label) {
        labelService.updateLabelById(label);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable("id") long id) {
        labelService.deleteLabelById(id);
    }

}
