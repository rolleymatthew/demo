package com.wy.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yunwang
 * @Date 2020-12-04
 */
public class FilesUtil {
    public static void writeFile(String filepath, String filename, String text) throws IOException {

        mkdirs(filepath);
        FileOutputStream fos = new FileOutputStream(filepath + "\\" + filename);

        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");

        BufferedWriter out = new BufferedWriter(osw);
        out.write(text);
        out.flush();

        osw.flush();

        fos.flush();
        out.close();
        osw.close();
        fos.close();

    }

    /**
     *
     * @Title: writeFile
     * @Description: 写文件
     * @param @param filePath 文件路径
     * @param @param fileContent    文件内容
     * @return void    返回类型
     * @throws
     */
    public static void writeFile(String filePath, String fileContent) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
        }
    }
    public static boolean existsAndIsFile(String filename) {
        File file = new File(filename);
        return file.exists() && file.isFile();
    }

    public static void mkdir(String filename) throws IOException {
        File file = new File(filename);
        if (!file.isDirectory()) {
            file.mkdir();
        }
    }

    public static void mkdirs(String filename) throws IOException {
        File file = new File(filename);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static List<String> getFilesOfDictory(String dir){
        List<String> fileNameList = new ArrayList<String>();
        File file = new File(dir);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        if (file.isFile()) {
            return fileNameList;
        }
        String[] fileNames=file.list();
        for (String fileName : fileNames) {
            fileNameList.add(fileName);
        }
        return fileNameList;
    }

    public static List<String> readFileAsListOfStrings(String filename, String charset) throws Exception {
        List<String> records = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename),charset));
        String line;
        while ((line = reader.readLine()) != null) {
            records.add(line);
        }
        reader.close();
        return records;
    }

}
