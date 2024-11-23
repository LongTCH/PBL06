package com.clothes.service;

import java.util.List;
import java.util.Map;

public interface NameObjectsService {
    void savePredictionMapping(Map<String, String> predictionMapping);

    Map<String, String> getPredictionMapping();

    List<String> getPredictionCategoryIds(List<String> categoryPredictions);
}
