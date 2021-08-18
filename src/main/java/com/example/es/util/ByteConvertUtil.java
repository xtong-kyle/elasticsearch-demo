package com.example.es.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author: Kyle Tong
 * @Date: 2021/8/6
 */
public class ByteConvertUtil {

    private static final Logger logger = LoggerFactory.getLogger(ByteConvertUtil.class);

    public static String loadAsString(String path) {
        try {
            final File source = new ClassPathResource(path).getFile();
            return new String(Files.readAllBytes(source.toPath()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
