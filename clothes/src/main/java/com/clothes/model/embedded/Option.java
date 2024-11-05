package com.clothes.model.embedded;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class Option implements Serializable {
    private String name;
    private int position;
    private List<String> values;

}
