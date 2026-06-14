package com.spring_ai.learn_spring_ai.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TravellingTools {

    @Tool(
            name = "weathertool",
            description = "this is the tool which return the weather condition in a city"
    )
    public String getWeather(
            @ToolParam(description = "the city for which weather is returned for ") String city)
    {
        return switch (city) {
            case "Delhi" -> "Sunny 25 degree";
            case "New York" -> "Cloudyy 3 degree";
            default -> "invalid city";
        };
    }
}
