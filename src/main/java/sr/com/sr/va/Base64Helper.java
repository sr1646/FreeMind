package sr.com.sr.va;

import sr.utility.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

public class Base64Helper {
    public void convert(){
        String filePath = "."+ File.separator;
        
        String newFileName = "2020_12_chetavani.pdf";
        String originalFileName = "input_base64.txt";

        String sourceFile = filePath + originalFileName;
//        String encodedString = readBinaryFileInBase64String(sourceFile);
        /*
        // use below to in chrome console to download pdf base 64
        console.log(document.getElementById('PdfString').value);
        // press enter and there will be option for copy at the end of output string  paste it into input base64 with notepad++
         */
        String encodedString = readFile(sourceFile);
//        System.out.println(encodedString);
        String destinationFile = "D:\\AS\\CLASSIFIED\\moksmarg\\chetavani\\2018"+ File.separator + newFileName;

        saveBase64StringToFile(destinationFile, encodedString);
    }

    private  void saveBase64StringToFile(String fileName, String encodedString) {
//        byte[] decodedBytes = Base64.getDecoder().decode(encodedString.getBytes());
        byte[] decodedBytes = Base64.getMimeDecoder().decode(encodedString.getBytes()); // work with moksmarg.com pdf
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            fos.write(decodedBytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private  String readFile(String file){
        List<String> fileData = new FileHelper().readFile(file);
        int capacity = 1000;
        StringBuilder strFile=new StringBuilder(capacity);
        for (String line : fileData) {
            strFile.append(line).append("\n");
        }
        return strFile.toString();
    }
    private  String readBinaryFileInBase64String(String fileName) {
        byte[] input_file = new byte[0];
        try {
            input_file = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] encodedBytes = Base64.getEncoder().encode(input_file);
        String encodedString =  new String(encodedBytes);
        return encodedString;
    }
}
