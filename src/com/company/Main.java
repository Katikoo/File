package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    private final static String INPUT_FOLDER = "C:\\Program Files\\Java\\jdk1.8.0_161\\src.zip";
    private final static String OUTPUT_FOLDER = "F:\\tmp-java";
    private final static File RESOURCES = new File(OUTPUT_FOLDER);
    private final static String ANNOTATION = "@FunctionalInterface";
    private static List<File> javaFiles = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("Enter input and output folders:");
        unpackZip(INPUT_FOLDER, OUTPUT_FOLDER);

        countJavaFiles(RESOURCES);
        System.out.printf("jdk1.8.0_161 contains %d java files.\n", javaFiles.size());

        System.out.println("Java files that contain the annotation <<@FunctionalInterface>> :");
        searchJavaFilesWithAnnotation(javaFiles, ANNOTATION);
    }

    private static void unpackZip(String zipFile, String outputFile) {
        byte[] bytes = new byte[512];

        try {
            File file = new File(outputFile);
            if (!file.exists()) {
                file.mkdir();
            }
            ZipInputStream inputStream = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry = inputStream.getNextEntry();

            while (zipEntry != null) {
                String name = zipEntry.getName();
                File newFile = new File(outputFile + File.separator + name);
                new File(newFile.getParent()).mkdirs();

                FileOutputStream output = new FileOutputStream(newFile);
                int length;
                while ((length = inputStream.read(bytes)) > 0) {
                    output.write(bytes, 0, length);
                }
                output.close();
                zipEntry = inputStream.getNextEntry();
            }
            inputStream.closeEntry();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void countJavaFiles(File file) {
        for (File tmpCount : Objects.requireNonNull(file.listFiles())) {
            if (tmpCount.isDirectory()) {
                countJavaFiles(tmpCount);
            } else {
                if (tmpCount.toString().endsWith(".java")) {
                    javaFiles.add(tmpCount);
                }
            }
        }
    }

    private static void searchJavaFilesWithAnnotation(List<File> javaFiles, String annotation) {
        for (File file : javaFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String string;
                while ((string = br.readLine()) != null) {
                    if (string.equals(annotation)) {
                        System.out.println(file);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}