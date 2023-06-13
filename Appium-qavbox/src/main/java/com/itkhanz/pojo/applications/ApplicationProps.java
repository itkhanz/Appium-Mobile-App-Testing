package com.itkhanz.pojo.applications;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationProps {
    String appUrl;
    String appPackage;
    String appActivity;
    String bundleId;
    String appWaitActivity;
}
