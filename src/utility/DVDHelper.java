package utility;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static utility.Output.printLine;

public class DVDHelper {
    private static final long GIGABYTE = 1024;
    private static final long DVD_MAX_CAPACITY_IN_GB = 45;
    private static final long DVD_MAX_CAPACITY_IN_MB = DVD_MAX_CAPACITY_IN_GB*GIGABYTE;
    private static final long MAX_SIZE_IN_MB = DVD_MAX_CAPACITY_IN_MB * GIGABYTE * GIGABYTE;
    private static final long ALLOWED_FREE_SPACE_OF_DVD_IN_MB = 150;

    private static List<File> fileBiggerThenDVD = new ArrayList<>();
    private static Queue<File> skippedFile = new LinkedList<>();
    private static int currentDVD = 0;
    private static int skippedFileRound = 1;

    private List<File> filesToMove = new ArrayList<>();
    private long size = 0;
    private FileHelper fileHelper = new FileHelper();

    public DVDHelper() {

    }
    private long getSizeInMB() {
        long mb = ((size) / GIGABYTE / GIGABYTE);
        return mb;
    }

    private long getFreeMB() {
        return DVD_MAX_CAPACITY_IN_MB - getSizeInMB();
    }

    private boolean addFile(File file, List<DVDHelper> dvdList) {

        long fileSize = 0;
        if (file.isFile()) {
            fileSize = fileHelper.size(Path.of(file.getPath()));
            size += fileSize;
            if (size > MAX_SIZE_IN_MB) {
                long dvdSize=fileHelper.getSizeInMB(size);
                long maxSize=fileHelper.getSizeInMB(MAX_SIZE_IN_MB);
                size -= fileSize;
                //skip this file temporarily we will try to add this file first whenever new dvd added so add it in queue
                //also check like if file size is bigger than current max size then add them in max limit exceed file add it in list
                if (fileSize > MAX_SIZE_IN_MB) {
                    fileBiggerThenDVD.add(file);
                }else {
                    skippedFile.add(file);
                }
                addNewDVD(dvdList);// this method will only add dvd if it have free space less then allowed free space
                return false;
            }
            filesToMove.add(file);
        }
        return true;
    }

    public List<File> getFilesToMove() {
        return filesToMove;
    }


    public void addFileToDVD(File file, List<DVDHelper> dvdList) {
        dvdList.get(currentDVD).addFile(file, dvdList);
    }

    private void addNewDVD(List<DVDHelper> dvdList) {
        // this method will only add dvd if it have free space less then allowed free space
        if (dvdList.get(currentDVD).getFreeMB() < ALLOWED_FREE_SPACE_OF_DVD_IN_MB) {


            //add skipped list queue first
            if(skippedFile.size()>0){
                visitSkippedFile(dvdList);
            }else{
                printLine("new dvd container added for regular dvd making recursive call: for index: " + (++currentDVD));
                dvdList.add(new DVDHelper());
            }
        }
    }

    @Override
    public String toString() {
        return "DVD Details {" +
                "total filesToMove=" + filesToMove.size() +
                ", size=" + getSizeInMB() + "MB" +
                '}';
    }

    public void visitSkippedFile(List<DVDHelper> dvdList) {

        while (skippedFile.size()>0){
            File[] copySkippedFile=new File[skippedFile.size()];
            int i=0;
            do{
                File file=skippedFile.poll();
                if(file==null){
                    break;
                }
                copySkippedFile[i++]=file;
            }while(true);

            printLine("Full Queue copied: so size should zero: skippedFile.size() = "+skippedFile.size());
            printLine("\nTaking care of Skipped file round: "+skippedFileRound+++"\n");
            dvdList.add(new DVDHelper());
            printLine("new dvd container added for Skipped file DVD making recursive call: for index: " + (++currentDVD));
            //add skipped list queue first
            for(File file:copySkippedFile){
                 addFileToDVD(file, dvdList);
            }
        }
    }

    public void showBiggerThanDvdFile() {
        if(fileBiggerThenDVD.size()>0){
            printLine("The below list of file are bigger than dvd so can not be moved: "+fileBiggerThenDVD.toString().replace('[','\n').replaceAll(", ","\n"));
        }else {
            printLine("No file found that are bigger than dvd");
        }
    }
}
