package com.itkhanz.pojo.applications;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationsRoot {
    String appsDirectory;
    List<Applications> applications;
}
