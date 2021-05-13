package com.usedcar.admin.web.dto;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

	@Override
	public void serialize(Errors errors, JsonGenerator generator, SerializerProvider provider) throws IOException {
		generator.writeStartArray();
		errors.getFieldErrors().stream().forEach(e -> {
			try {
				generator.writeStartObject();
				generator.writeStringField("field", e.getField());
				generator.writeStringField("objectName", e.getObjectName());
				generator.writeStringField("code", e.getCode());
				generator.writeStringField("defaultMessage", e.getDefaultMessage());
				Object rejectedValue = e.getRejectedValue();
				if (rejectedValue != null) {
					generator.writeStringField("rejectedValue", e.getRejectedValue().toString());
				}
				generator.writeEndObject();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		errors.getGlobalErrors().stream().forEach(e -> {
			try {
				generator.writeStartObject();
				generator.writeStringField("objectName", e.getObjectName());
				generator.writeStringField("code", e.getCode());
				generator.writeStringField("defaultMessage", e.getDefaultMessage());
				generator.writeEndObject();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		generator.writeEndArray();
	}

}
