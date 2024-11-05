package com.clothes.dto;

import com.clothes.model.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltersDto {
    List<FilterSelectDto> groups;
    int minPrice = 0;
    int maxPrice = 3000000;
    int size = 24;
    int page = 0;

    public FiltersDto(List<Group> groups) {
        this.groups = groups.stream().map(group -> new FilterSelectDto(group.getId(), group.getName(), false)).toList();
    }
}


