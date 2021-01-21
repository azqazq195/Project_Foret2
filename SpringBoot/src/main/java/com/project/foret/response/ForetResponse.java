package com.project.foret.response;

import com.project.foret.model.Foret;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ForetResponse {
    private int total;
    private List<Foret> forets;
}
