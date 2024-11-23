package com.clothes;

import com.clothes.dto.KeyValue;
import com.clothes.service.NameObjectsService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ClothesApplicationTests {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ClothesApplicationTests.class);
    @Autowired
    private NameObjectsService nameObjectsService;
    @Test
    void initPredictionMapping() {
        List<KeyValue<String>> keyValueList = new ArrayList<>();
        keyValueList.add(new KeyValue<>("ao_2_day", "67277e39649242ee8ce78b2a"));
        keyValueList.add(new KeyValue<>("ao_ba_lo", "67277e39649242ee8ce78b2b"));
        keyValueList.add(new KeyValue<>("ao_da", "67277e39649242ee8ce78b2c"));
        keyValueList.add(new KeyValue<>("ao_dai", "67277e39649242ee8ce78b2d"));
        keyValueList.add(new KeyValue<>("ao_khoac_the_thao", "67277e39649242ee8ce78b2e"));
        keyValueList.add(new KeyValue<>("ao_len", "67277e39649242ee8ce78b2f"));
        keyValueList.add(new KeyValue<>("ao_mangto", "67277e39649242ee8ce78b30"));
        keyValueList.add(new KeyValue<>("ao_so_mi", "67277e39649242ee8ce78b31"));
        keyValueList.add(new KeyValue<>("ao_thun", "67277e39649242ee8ce78b32"));
        keyValueList.add(new KeyValue<>("ao_tre_vai", "67277e39649242ee8ce78b33"));
        keyValueList.add(new KeyValue<>("ao_vest", "67277e39649242ee8ce78b34"));
        keyValueList.add(new KeyValue<>("chan_vay_dai", "67277e39649242ee8ce78b36"));
        keyValueList.add(new KeyValue<>("chan_vay_ngan", "67277e39649242ee8ce78b37"));
        keyValueList.add(new KeyValue<>("dam_maxi", "67277e39649242ee8ce78b38"));
        keyValueList.add(new KeyValue<>("dam_om", "67277e39649242ee8ce78b39"));
        keyValueList.add(new KeyValue<>("do_ngu_do_mac_nha", "67277e39649242ee8ce78b35"));
        keyValueList.add(new KeyValue<>("quan_dai", "67277e39649242ee8ce78b3a"));
        keyValueList.add(new KeyValue<>("quan_jean", "67277e39649242ee8ce78b3b"));
        keyValueList.add(new KeyValue<>("quan_short", "67277e39649242ee8ce78b3c"));
        Map<String, String> predictionMapping = keyValueList.stream()
                .collect(java.util.stream.Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
        nameObjectsService.savePredictionMapping(predictionMapping);
    }

    @Test
    void getPredictionMapping() {
        Map<String, String> predictionMapping = nameObjectsService.getPredictionMapping();
        predictionMapping.forEach((key, value) -> logger.info("Key: {}, Value: {}", key, value));
    }
}
