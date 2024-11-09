package org.studysync.studysync.swagger;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;
import lombok.Getter;//ExampleHolder

@Getter
@Builder
public class ExampleHolder {
    private Example holder;
    private String name;
    private int code;
}