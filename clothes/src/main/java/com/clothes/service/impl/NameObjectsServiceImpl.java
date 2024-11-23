package com.clothes.service.impl;

import com.clothes.dto.KeyValue;
import com.clothes.model.NameObject;
import com.clothes.repository.NameObjectsRepository;
import com.clothes.service.NameObjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NameObjectsServiceImpl implements NameObjectsService {
    private static final String PREDICTION_MAPPING_KEY = "predictionMapping";
    @Autowired
    private NameObjectsRepository nameObjectsRepository;

    @Override
    public void savePredictionMapping(Map<String, String> predictionMapping) {
        var nameObject = nameObjectsRepository.findById(PREDICTION_MAPPING_KEY);
        // convert map to list key values
        var keyValues = predictionMapping.entrySet().stream()
                .map(entry -> new KeyValue<String>(entry.getKey(), entry.getValue()))
                .toList();
        if (nameObject.isPresent()) {
            nameObject.get().setValue(keyValues);
            nameObjectsRepository.save(nameObject.get());
        } else {
            var newObject = new NameObject();
            newObject.setId(PREDICTION_MAPPING_KEY);
            newObject.setValue(keyValues);
            nameObjectsRepository.save(newObject);
        }
    }

    @Override
    public Map<String, String> getPredictionMapping() {
        var nameObject = nameObjectsRepository.findById(PREDICTION_MAPPING_KEY);
        // convert value from object to list then to map
        return nameObject.map(object -> ((List<KeyValue>) object.getValue()).stream()
                .collect(Collectors.toMap(KeyValue<String>::getKey, KeyValue<String>::getValue))).orElseGet(Map::of);
    }

    @Override
    public List<String> getPredictionCategoryIds(List<String> categoryPredictions) {
        var predictionMapping = getPredictionMapping();
        var listCategoryIds = new ArrayList<String>();
        for (var categoryPrediction : categoryPredictions) {
            var categoryId = predictionMapping.get(categoryPrediction);
            if (categoryId != null) {
                listCategoryIds.add(categoryId);
            }
        }
        return listCategoryIds;
    }
}
