package com.sae.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class SQLQueryRequest {
    //headers
    private Map<String, String> headers;
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
//    private int conditionsPerQuery;


    @Getter
    @Setter
    @Data
    public static class SetConditions {
        @NotBlank
        private String columns;
        @NotBlank
        private String comparative;
        @NotBlank
        private String values;
    }
    @Getter
    @Setter
    @Data
    public static class SetValue {
        private String columns;
        private String value;
    }
}
