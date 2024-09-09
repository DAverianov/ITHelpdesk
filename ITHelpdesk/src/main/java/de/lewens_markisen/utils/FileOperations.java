package de.lewens_markisen.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class FileOperations {
    @Autowired
    private Logger logger;

    public File getFileFromResources(String fileName) {
        FileOperationsClassLoader fo = new FileOperationsClassLoader();
        return fo.getFileFromResources(fileName);
    }

    public List<String> readFileToList(String fileName) {
        File file = getFileFromResources(fileName);
        List<String> list = new ArrayList<>();
        try {
            list = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("An error occurred while reading file! " + fileName);
        }
        return list;
    }

    public String readFileToSting(String fileName) {
        List<String> list = readFileToList(fileName);
        return list.stream().collect(Collectors.joining("\n"));
    }
}
