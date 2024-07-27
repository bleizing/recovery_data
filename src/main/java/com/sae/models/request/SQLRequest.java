package com.sae.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class SQLRequest {
    private String userId;
    private String token;
    private String headers;
    @NotNull
    private Boolean isSaveToDB;
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must not contain special characters")
    private String fileName;
    private String fileLocation;
    @NotBlank
    private String regions;
    @NotBlank
    private String tables;
    @NotBlank
    private String columns;
    private List<SetValue> setValues;
    private List<SetConditions> conditions;
    private int conditionsPerQuery;
    private int totalRows;


    @Getter
    @Setter
    @Data
    public static class SetConditions {
        private String columns;
        private String comparative;
        private String values;
    }
    @Getter
    @Setter
    @Data
    public static class SetValue {
        private String columns;
        private String comparative;
        private String value;
    }
}
