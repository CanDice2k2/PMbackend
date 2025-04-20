package com.datn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.datn.exception.LabelNotFoundException;
import com.datn.model.Label;
import com.datn.service.LabelService;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @PostMapping
    public ResponseEntity<Label> createLabel(@RequestBody Label label, @RequestParam Long userId) {
        Label createdLabel = labelService.createLabel(label, userId);
        return new ResponseEntity<>(createdLabel, HttpStatus.CREATED);
    }

    @GetMapping("/{labelId}")
    public ResponseEntity<Label> getLabelById(@PathVariable Long labelId) throws LabelNotFoundException {
        Label label = labelService.getLabelById(labelId);
        return ResponseEntity.ok(label);
    }

    @GetMapping
    public ResponseEntity<List<Label>> getAllLabels() {
        List<Label> labels = labelService.getAllLabels();
        return ResponseEntity.ok(labels);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Label>> getLabelsByUser(@PathVariable Long userId) {
        List<Label> labels = labelService.getLabelsCreatedByUser(userId);
        return ResponseEntity.ok(labels);
    }

    @PutMapping("/{labelId}")
    public ResponseEntity<Label> updateLabel(@PathVariable Long labelId, @RequestBody Label updatedLabel)
            throws LabelNotFoundException {
        Label label = labelService.updateLabel(labelId, updatedLabel);
        return ResponseEntity.ok(label);
    }

    @DeleteMapping("/{labelId}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long labelId) throws LabelNotFoundException {
        labelService.deleteLabel(labelId);
        return ResponseEntity.noContent().build();
    }
}