package com.datn.service;

import java.util.List;
import java.util.Optional;

import com.datn.exception.LabelNotFoundException;
import com.datn.model.Label;

public interface LabelService {
    Label createLabel(Label label, Long userId);
    Label getOrCreateLabel(String labelName, Long userId);
    Label getLabelById(Long labelId) throws LabelNotFoundException;
    List<Label> getAllLabels();
    List<Label> getLabelsCreatedByUser(Long userId);
    Label updateLabel(Long labelId, Label updatedLabel) throws LabelNotFoundException;
    void deleteLabel(Long labelId) throws LabelNotFoundException;
}