package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.service.interfaces.ExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {

    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    public ExportServiceImpl(ObjectMapper objectMapper, XmlMapper xmlMapper) {
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
        this.xmlMapper.writerWithDefaultPrettyPrinter();
    }

    @Override
    public byte[] exportRecipesToJson(List<RecipeDto> recipes) throws IOException {
        return objectMapper.writeValueAsBytes(recipes);
    }

    @Override
    public byte[] exportRecipesToXml(List<RecipeDto> recipes) throws IOException {
        return xmlMapper.writeValueAsBytes(recipes);
    }
}
