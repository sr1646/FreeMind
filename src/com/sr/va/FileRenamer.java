package com.sr.va;

import java.io.File;

public class FileRenamer {
    String decorator="\n***************************************************";
    private int totalRenamed=0;
    public  void renameFileByRemovingSpecialChar(String folderPath){
        renameFileWithoutSpecialCharacter(folderPath);
        OutputWriter.write(decorator);
        if(totalRenamed>0){
            OutputWriter.write("\nTotal file renamed are: "+totalRenamed);
        }else{
            OutputWriter.write("\nEvery file name are correct  no need to change");
        }

        OutputWriter.printLogFileNameToConsole();
    }
    private void renameFileWithoutSpecialCharacter(String folderPath){
            // Path of folder where files are located

            // creating new folder
            File directory = new File(folderPath);
            for (File file : directory.listFiles())
            {
                if (file.isDirectory()) {
                    OutputWriter.write("\nRenaming file inside folder: "+file.getPath());
                    renameFileWithoutSpecialCharacter(file.getPath());
                }
                renameFile(file);
            }

        }

    private File renameFile(File file) {
        String fileName = file.getName();
        String new_file_name = fileName.replaceAll("[^a-zA-Z0-9.\\(\\)\\- ]", "");
        if (!new_file_name.equalsIgnoreCase(fileName)) {
            OutputWriter.write(decorator);
            
            OutputWriter.write("\nOld file name: " + fileName);
            OutputWriter.write("\nNew file name: " + new_file_name);
            file.renameTo(new File(new StringBuilder().append(file.getParent()).append(File.separator).append(new_file_name).toString()));
            totalRenamed++;
        }

        return file;
    }
}
