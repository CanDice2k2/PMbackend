package com.datn.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.exception.LabelNotFoundException;
import com.datn.exception.UserException;
import com.datn.model.Label;
import com.datn.model.User;
import com.datn.repository.LabelRepository;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserService userService;

    @Override
    public Label createLabel(Label label, Long userId) {
        try {
            User creator = userService.findUserById(userId);
            label.setCreator(creator);
            label.setCreationDate(LocalDateTime.now());
            label.setUsageCount(0);
            label.setVisible(true);
            return labelRepository.save(label);
        } catch (UserException e) {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    @Override
    public Label getOrCreateLabel(String labelName, Long userId) {
        Optional<Label> existingLabel = labelRepository.findByName(labelName);

        if (existingLabel.isPresent()) {
            return existingLabel.get();
        } else {
            Label newLabel = new Label();
            newLabel.setName(labelName);
            return createLabel(newLabel, userId);
        }
    }

    @Override
    public Label getLabelById(Long labelId) throws LabelNotFoundException {
        return labelRepository.findById(labelId)
                .orElseThrow(() -> new LabelNotFoundException("Label not found with id: " + labelId));
    }

    @Override
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Override
    public List<Label> getLabelsCreatedByUser(Long userId) {
        try {
            User user = userService.findUserById(userId);
            return labelRepository.findByCreator(user);
        } catch (UserException e) {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    @Override
    public Label updateLabel(Long labelId, Label updatedLabel) throws LabelNotFoundException {
        Label existingLabel = getLabelById(labelId);

        // Update fields if provided
        if (updatedLabel.getName() != null) {
            existingLabel.setName(updatedLabel.getName());
        }

        if (updatedLabel.getColor() != null) {
            existingLabel.setColor(updatedLabel.getColor());
        }

        if (updatedLabel.getDescription() != null) {
            existingLabel.setDescription(updatedLabel.getDescription());
        }

        if (updatedLabel.isVisible() != existingLabel.isVisible()) {
            existingLabel.setVisible(updatedLabel.isVisible());
        }

        return labelRepository.save(existingLabel);
    }

    @Override
    public void deleteLabel(Long labelId) throws LabelNotFoundException {
        if (!labelRepository.existsById(labelId)) {
            throw new LabelNotFoundException("Label not found with id: " + labelId);
        }
        labelRepository.deleteById(labelId);
    }
}