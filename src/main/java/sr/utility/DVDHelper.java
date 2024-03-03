package sr.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static sr.utility.Output.printLine;

public class DVDHelper {
    public static final String STORED_DVD = "storedDvdLog";
    public static final String  STORED_DVD_PROGRESS_FILE = "_progress.log";
    public static final String  STORED_DVD_DETAIL_FILE = "_detail.log";
    public static final Integer STORED_DVD_INITIAL_PROGRESS = -1;
    public static final Integer STORED_DVD_SOURCE_FOLDER_INDEX = 0;
    public static final Integer STORED_DVD_DESTINATION_FOLDER_INDEX = 1;
    public static final Integer STORED_DVD_DATA_INDEX = 2;
    public static final Integer STORED_DVD_CURRENT_PROGRESS_INDEX = 3;
    public static final Integer STORED_DVD_FILE_NAME_INDEX = 4;
    public static final Integer STORED_DVD_DATA_SIZE = 4;
    private static final long GIGABYTE = 1024;
    //******************************************************************************************
    //************************        EDIT BELOW VARIABLES AS NEEDED        ********************
    //******************************************************************************************
    private static final float DVD_MAX_CAPACITY_IN_GB = 2f;
//    private static final long DVD_MAX_CAPACITY_IN_MB = (long) (DVD_MAX_CAPACITY_IN_GB*GIGABYTE);
//    private static final long ALLOWED_FREE_SPACE_OF_DVD_IN_MB = 80;
    private static final long DVD_MAX_CAPACITY_IN_MB = 20;
    private static final long ALLOWED_FREE_SPACE_OF_DVD_IN_MB = 1;
    private static int currentDVD = 5;
    private static final long ALLOW_TOTAL_DVD_DISK_AS_OF_NOW = 1;
    //******************************************************************************************
    //************************      EDIT BELOW VARIABLES AS NEEDED END      ********************
    //******************************************************************************************


    private static boolean MAX_DVD_ADDED = false;
    private static final long MAX_SIZE_IN_MB = DVD_MAX_CAPACITY_IN_MB * GIGABYTE * GIGABYTE;


    private static List<File> fileBiggerThenDVD = new ArrayList<>();
    private static Queue<File> skippedFile = new LinkedList<>();

    private static int skippedFileRound = 1;

    private List<File> filesToMove = new ArrayList<>();
    private long size = 0;
    private FileHelper fileHelper = new FileHelper();

    public DVDHelper() {
    }


    private long getFreeMB() {
        return DVD_MAX_CAPACITY_IN_MB - FileHelper.getSizeInMB(size);
    }

    private boolean addFile(File file, List<DVDHelper> dvdList) {
        long fileSize = 0;
        if (file.isFile()) {
            fileSize = fileHelper.getFileFolderSizeInByte(file.toPath());
            size += fileSize;
            if (size > MAX_SIZE_IN_MB) {
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
        if(MAX_DVD_ADDED){
            return;
        }
        dvdList.get(currentDVD).addFile(file, dvdList);
    }

    private void addNewDVD(List<DVDHelper> dvdList) {
        // this method will only add dvd if it have free space less then allowed free space
        if (dvdList.get(currentDVD).getFreeMB() < ALLOWED_FREE_SPACE_OF_DVD_IN_MB) {
            //add skipped list queue first
            if(skippedFile.size()>0){
                visitSkippedFile(dvdList);
            }else{
                addDVD(dvdList);
            }
        }
    }

    private void addDVD(List<DVDHelper> dvdList) {
        if(currentDVD< ALLOW_TOTAL_DVD_DISK_AS_OF_NOW){
            printLine("new dvd container added for regular dvd making recursive call: for index: " + (currentDVD++));
            dvdList.add(new DVDHelper());
        }else{
            //stop the operation we have already reached allowed dvd
            printLine("MAXIMUM DVD Created Can not create new DVD" + (currentDVD));
            MAX_DVD_ADDED=true;
        }
    }

    //    @Override
//    public String toString() {
//        return "DVD Details {" +
//                "total filesToMove=" + filesToMove.size() +
//                ", size=" + getSizeInMB() + "MB" +
//                '}';
//    }
    public String dvdInfo(){
        return  "DVD Details {" +
                "total filesToMove=" + filesToMove.size() +
                ", size=" + FileHelper.getSizeInGB(size) +
                '}';
    }
    @Override
    public String toString() {
        return filesToMove.toString();
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
            addDVD(dvdList);
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
