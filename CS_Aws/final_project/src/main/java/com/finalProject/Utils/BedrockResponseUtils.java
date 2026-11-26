package com.finalProject.Utils;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;

public final class BedrockResponseUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(BedrockResponseUtils.class);

    private BedrockResponseUtils() {}

    public static void logResponseDetails(ConverseResponse response, String modelId, String responseText) {
        var usage = response.usage();
        if(usage != null) {
            LOGGER.info("Bedrock model {} usage: inputTokens={}, outputTokens={}, stopReason={}",
                modelId, usage.inputTokens(), usage.outputTokens(), response.stopReasonAsString());
        }
        LOGGER.debug("Bedrock model {} raw response: {}", modelId, responseText);
    }

    public static <T> List<T> parseJsonArray(ObjectMapper objectMapper, String response, Class<T[]> responseType) {
        JsonProcessingException lastError = null;
        for(int start = response.indexOf('['); start >= 0; start = response.indexOf('[', start + 1)) {
            int end = findArrayEnd(response, start);
            if(end < 0) {
                continue;
            }

            try {
                T[] parsed = objectMapper.readValue(response.substring(start, end + 1), responseType);
                if(parsed != null) {
                    return Arrays.asList(parsed);
                }
            } catch(JsonProcessingException e) {
                lastError = e;
            }
        }

        throw new IllegalArgumentException("Model response did not contain a valid JSON array", lastError);
    }

    private static int findArrayEnd(String text, int start) {
        int depth = 0;
        boolean inString = false;
        boolean escaped = false;

        for(int index = start; index < text.length(); index++) {
            char current = text.charAt(index);
            if(inString) {
                if(escaped) {
                    escaped = false;
                } else if(current == '\\') {
                    escaped = true;
                } else if(current == '"') {
                    inString = false;
                }
            } else if(current == '"') {
                inString = true;
            } else if(current == '[') {
                depth++;
            } else if(current == ']') {
                depth--;
                if(depth == 0) {
                    return index;
                }
            }
        }

        return -1;
    }
}
