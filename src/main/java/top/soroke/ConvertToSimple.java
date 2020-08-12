package top.soroke;

import com.github.houbb.opencc4j.util.ZhConverterUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConvertToSimple {
    //执行当前jar包所在目录下所有文件的简繁转换
    public static void main(String[] ages) throws IOException {
        //获取当前执行目录
        String rootPath = System.getProperty("user.dir");

        convert("C:\\Users\\123\\Desktop\\testuncode");
        System.out.println("转换完成");
    }

    /**
     * 读取文件内容(按行读取文件内容)
     * @param fileName 文件路径
     * @return
     */
    public static List<String> readByBufferedReader(String fileName,String charset) {

        List<String> fileContent = new ArrayList<String>();
        try {
            File file = new File(fileName);
            // 读取文件，并且以utf-8的形式写出去
            BufferedReader bufread;
            String read;
            bufread = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            while ((read = bufread.readLine()) != null) {
//                System.out.println(read);
                fileContent.add(read);
            }
            bufread.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fileContent;
    }

    /**
     * 递归转换文件(一级一级往下找文件)
     * @param filderPath
     */
    public static void convert(String filderPath) throws IOException {
        File fidler = new File(filderPath);
        File[] files = fidler.listFiles();
        for (File file:files) {
            if (file.isDirectory()) {
                convert(file.getPath());
            } else {
                //判断非jar包\且文件类型为ini\txt\lua等脚本时才做转换
                String[] fileName = file.getName().split("\\.");
//                System.out.println(fileName[fileName.length-1]);
                if (!fileName[fileName.length-1].equals("jar") && (fileName[fileName.length-1].equals("ini") || fileName[fileName.length-1].equals("txt")|| fileName[fileName.length-1].equals("lua"))) {
                    String charset = CpdetectorUtils.getFileEncode(file.getPath());
                    List<String> fileContent = readByBufferedReader(file.getPath(),charset);
                    writeByBufferedReader(file.getPath(),fileContent,charset);
                }
            }
        }
    }

    /**
     * 执行文件内容转换(繁体字转换为简体字)
     * @param filePath
     * @param fileContent
     */
    public static void writeByBufferedReader(String filePath,List<String> fileContent,String charset) {
        try {

            //String content = "This is the content to write into file";
            File file = new File(filePath);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            //FileWriter fw = new FileWriter(file, false);
            FileOutputStream fos = new FileOutputStream(file,false);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, charset));
            for (String content:fileContent) {
                bw.write(ZhConverterUtil.convertToSimple(content) + "\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
