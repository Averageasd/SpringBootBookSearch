package com.example.demo.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
public class BindingErrorMessagesUtil {

    public List<String> getBindingErrorMessages(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
    }
}
