package sr.utility;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static sr.utility.Output.*;

public class FileHelper {
    private int counter;
    private static final int GIGABYTE = 1024;
    private final String encoding = "UTF-8";
    private List<String> skipDirectory=new ArrayList<>(Arrays.asList("Google Photos","Takeout"));
    private List<String> skipDirectoryStartWith=new ArrayList<>(Arrays.asList("takeout","Photos from"));
    private Map<String,Integer> folder;
    public static Set<String> VEDEO_FILES_EXTENSION=new HashSet<>(Arrays.asList(new String[]{
            "mpeg", "es", "ps", "ts", "pva", "avi", "asf", "wmv", "wma", "mp4", "mov", "3gp", "ogg", "ogm", "annodex", "axv", "mkv", "real", "flv", "mxf", "nut", "dat"
    }));

    /*
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    Only Private method Start
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################

     */
    public long getSizeInMB(long size) {
        long mb = ((size) / GIGABYTE / GIGABYTE);
        return mb;
    }
    private String getSizeInGB(long size) {
        long totalMB=getSizeInMB(size);
        long gb = (totalMB/GIGABYTE);
        int roundGb=Math.round(gb);
        int remainingMB= (int) (totalMB-(roundGb*GIGABYTE));
        return ""+roundGb+"GB, "+remainingMB+"MB";
    }
    private  void showFile(File file) {
        if (file.isDirectory()) {
            String folderName=file.getName();

            for(String lskipDirectory:skipDirectory){
                if(lskipDirectory.equalsIgnoreCase(folderName)){
                    return;
                }
            }
            for(String skipDirectory:skipDirectoryStartWith){
                if(folderName.startsWith(skipDirectory)){
                    return;
                }
            }
            if(folder.containsKey(folderName)){
                folder.put(folderName,folder.get(folderName)+1);
            }else{
                folder.put(folderName,1);
            }
            System.out.println("Reading Directory: "+ (++counter)+": "+ file.getAbsolutePath());
        } else {
//            System.out.println("File: " + file.getAbsolutePath());
        }
    }

    private  void getMoveFileList(File file, List<File> filesToMove) {
        if (file.isFile()) {
            filesToMove.add(file);
        }
    }
    /**
     * Attempts to calculate the size of a file or directory.
     *
     * <p>
     * Since the operation is non-atomic, the returned value may be inaccurate.
     * However, this method is quick and does its best.
     */
    public long size(Path path) {

        final AtomicLong size = new AtomicLong(0);

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                    size.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {

                    System.out.println("skipped: " + file + " (" + exc + ")");
                    // Skip folders that can't be traversed
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

                    if (exc != null)
                        System.out.println("had trouble traversing: " + dir + " (" + exc + ")");
                    // Ignore errors traversing a folder
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
        }

        return size.get();
    }
        /*
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    Only Private method End
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################

     */

    /*
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    Only Public method Start
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################

     */
    public  boolean writeFile(String line,String fileName){

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName,true), encoding));
            writer.write(line);
            writer.newLine();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(writer);
        }
        return true;
    }
    public  List<String> readFile(String fileName){
        BufferedReader reader = null;
        ArrayList<String> list=new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encoding));
            int count = 0;
            for (String line; (line = reader.readLine()) != null;) {
                list.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            Output.exception(e);
        } catch (FileNotFoundException e) {
            Output.exception(e);
        } catch (IOException e) {
            Output.exception(e);
        } finally {
            close(reader);
        }
        return list;
    }

    private  void close(BufferedReader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            Output.exception(e);
            reader=null;
        }
    }

    private  void close(BufferedWriter writer) {
        try {
            writer.close();
        } catch (IOException e) {
            Output.exception(e);
            writer=null;
        }
    }


    public  void showAllFolder(String pathToIterate) {
        Path dir = Paths.get(pathToIterate);
        counter=0;
        folder=new HashMap<String,Integer>();
        try {
            Files.walk(dir).forEach(path -> showFile(path.toFile()));
        } catch (IOException e) {
            Output.exception(e);
        }
        folder= CollectionHelper.sortByKey(folder);

        for(Map.Entry<String,Integer> entry:folder.entrySet()){
            System.out.println(entry.getKey()+" ( "+entry.getValue()+" )");
        }

    }
    public  boolean devideFilesForDVD(String sourceFolderStr, String destinationFolderStr) {
        if(StringUtil.isEmpty(sourceFolderStr) || StringUtil.isEmpty(destinationFolderStr)){
            printLine("The source and destination path must not be empty");
            return false;
        }

        printLine("Preparing File List");
        DVDHelper dvdHelper=new DVDHelper();
        List<DVDHelper> dvdList = new ArrayList<>();
        dvdList.add(new DVDHelper());

        try {
                Path sourceFolder = Paths.get(sourceFolderStr);
                Files.walk(sourceFolder).forEach(path -> dvdHelper.addFileToDVD(path.toFile(),dvdList));
        } catch (IOException e) {
            Output.exception(e);
        }

        dvdHelper.visitSkippedFile(dvdList);
        int part=1;
        decorate("*",30);
        for(DVDHelper DVD :dvdList){
            printLine(" "+part+" - "+DVD);
            moveFilesWithSameFolderStructureOnDestination(sourceFolderStr,destinationFolderStr+"//"+part++, DVD.getFilesToMove());
        }
        decorate("*",30);
        printLine("\n");
        decorate("#",30);
        dvdHelper.showBiggerThanDvdFile();
        decorate("#",30);
        return true;
    }



    public  boolean moveFiles(String source,String destination){
        if(StringUtil.isEmpty(source) || StringUtil.isEmpty(destination)){
            printLine("The source and destination path must not be empty");
            return false;
        }
        Path dir = Paths.get(source);
        Path destinationPath=Paths.get(destination);
        List<File> filesToMove=new ArrayList<>();
        printLine("Preparing File List");
        try {
            Files.walk(dir).forEach(path -> getMoveFileList(path.toFile(),filesToMove));
        } catch (IOException e) {
            Output.exception(e);
        }
        moveFiles(destinationPath, filesToMove);
        printLine("\n\n Total files moved: "+filesToMove.size());
        return true;
    }

    private void moveFiles(Path destinationPath, List<File> filesToMove) {
        for(File file: filesToMove){
            printLine("Moving File: " + file.getAbsolutePath());
            try {
                Path sourcePath=file.toPath();
                Path targetPath= destinationPath.resolve(sourcePath.getFileName());
                FileTime creationTime  = (FileTime) Files.readAttributes(sourcePath, "creationTime").get("creationTime");
                Files.move(sourcePath,targetPath);
                Files.setAttribute(targetPath, "creationTime", creationTime);

            } catch (IOException e) {
                Output.exception(e);
            }
        }
    }
    public void moveFilesWithSameFolderStructureOnDestination(String sourceFolderStr,String destinationFolderStr, List<File> filesToMove) {
        moveFilesWithSameFolderStructureOnDestination(destinationFolderStr,filesToMove ,sourceFolderStr,false);
    }
    private void moveFilesWithSameFolderStructureOnDestination(String destinationFolderStr, List<File> filesToMove, String sourceFolderStr,boolean isMergeFromDVD) {
        if(filesToMove==null ||filesToMove.isEmpty()){
            printLine("No file's Selected for Moving");
            return;
        }
        int totalFiles=filesToMove.size();
        debug("Total "+totalFiles+" file/s will be moved");
        decorate("*",10);
        debug("In Progress: ");
        int onePercent=totalFiles/100;
        if(onePercent==0){
            onePercent=1;
        }
        int counter=0;
        int fileMoved=0;
        for(File file: filesToMove){
            debug("Moving File: " + file.getAbsolutePath());
            String destinationFolderInSource=file.getParent();
            String relativeDestinationFolder=destinationFolderInSource.substring(sourceFolderStr.length()).trim();
            debug("relativeDestination: "+relativeDestinationFolder);
            if(isMergeFromDVD){
                int relativePathStartFrom=relativeDestinationFolder.indexOf(File.separator,1);
                if(relativePathStartFrom>0){
                    relativeDestinationFolder=relativeDestinationFolder.substring(relativePathStartFrom);
                }else{
                    relativeDestinationFolder="";
                }
                debug("relativeDestination For Merging DVD: '"+relativeDestinationFolder+"'");
            }

            moveFile(destinationFolderStr, file, relativeDestinationFolder);
            counter++;
            fileMoved++;
            if(counter==onePercent){
                counter=0;
                drawProgressBar(fileMoved,totalFiles);
            }
        }
        printLine("100% Files Moved Successfully\r");
    }

    private void moveFile(String destinationFolderStr, File file, String relativeDestinationFolder) {
        try {
            File destinationFolder;
            if("".equals(relativeDestinationFolder)){
                destinationFolder= createFolderBasedOnStrPath(destinationFolderStr);
            }else{
                destinationFolder= createFolderBasedOnStrPath(destinationFolderStr +"//"+ relativeDestinationFolder);
            }
            Path destinationPath=destinationFolder.toPath();

            Path sourcePath= file.toPath();
            Path targetPath= destinationPath.resolve(sourcePath.getFileName());
            FileTime creationTime  = (FileTime) Files.readAttributes(sourcePath, "creationTime").get("creationTime");
            Files.move(sourcePath,targetPath);
            Files.setAttribute(targetPath, "creationTime", creationTime);

        } catch (IOException e) {
            Output.exception(e);
        }
    }

    private File createFolderBasedOnStrPath(String destinationFolderStr) {
        File destinationFolder;
        destinationFolder   =new File(destinationFolderStr);
        if (!destinationFolder.exists()){
            destinationFolder.mkdirs();
        }
        return destinationFolder;
    }
    public void showFileFolerWithSize(File dir){
        decorate("*",20);
        printLine("On your Mark promethius going to : "+dir.getAbsolutePath());
        decorate("*",20);
        File[] directoryListing = dir.listFiles();
        if (directoryListing == null) {
            return;
        }

        Map<String, Long> fileWithSize = new LinkedHashMap<String, Long>();
        Map<String, File> folderList = new LinkedHashMap<String, File>();
        for (File child : directoryListing) {
            fileWithSize.put(child.getName(), size(child.toPath()));
            if(child.isDirectory()){
                folderList.put(child.getName(),child);
            }
        }
        Map<String, Long> sortedMap = new MapSort().sortMapByValue(fileWithSize);
        int srNo = 1;
        printLine("0 to go back");
        String[] fileName=new String[sortedMap.size()];
        for (Object entry : sortedMap.entrySet()) {
            Map.Entry entryv = (Map.Entry<String, Long>) entry;
            fileName[srNo-1]= String.valueOf(entryv.getKey());
            printLine((srNo++) + "\t" + entryv.getKey() + "\t" + getSizeInGB((Long) entryv.getValue()));

        }
        decorate("-",20);
        printLine("Pleaase choose folder to go");
        Scanner stdIn=new Scanner(System.in);
        int choice=stdIn.nextInt();
        if(choice==0){
            showFileFolerWithSize(dir.getParentFile());
        }else{
            if(choice<=sortedMap.size()){
                showFileFolerWithSize(folderList.get(fileName[--choice]));
            }else{
                printLine("Invallid choice: exiting  prograam");
            }
        }
    }

    public boolean DVDFilesToFolder(String sourceFolderStr, String destinationFolderStr) {
        if(StringUtil.isEmpty(sourceFolderStr) || StringUtil.isEmpty(destinationFolderStr)){
            printLine("The source and destination path must not be empty");
            return false;
        }
        printLine("Preparing File List");
        File dir = new File(sourceFolderStr);
        File[] directoryListing = dir.listFiles();
        if (directoryListing == null) {
            return false;
        }
        List<File> filesToMove = new ArrayList<>();
        for (File child : directoryListing) {
            try {
                Files.walk(child.toPath()).forEach(path -> addFromDVDToFolder(path.toFile(),filesToMove));
            } catch (IOException e) {
                Output.exception(e);
            }
        }

        moveFilesWithSameFolderStructureOnDestination(destinationFolderStr,filesToMove ,sourceFolderStr,true);

        return true;
    }
    public void deleteDirectory(File file) {
        if(file.isDirectory()){
            String[] childFiles = file.list();
            if(childFiles.length==0) {
                //Directory is empty. Proceed for deletion
                file.delete();
            }
            else {
                decorate("-");
                printLine(file.getAbsolutePath()+"\tDirectory is not empty so not deleting considering data loss chances");
                decorate("*");
                printLine("Files: "+Arrays.asList(childFiles));
                decorate("-");
//                //Directory has other files.
//                //Need to delete them first
//                for (String childFilePath :  childFiles) {
//                    //recursive delete the files
//                    deleteDirectory(childFilePath);
//                }
            }

        }
        else {
            //it is a simple file. Proceed for deletion
            file.delete();
        }

    }
    private void addFromDVDToFolder(File file, List<File> filesToMove) {
        if (file.isFile()) {
            filesToMove.add(file);
        }
    }
    public List<String> getAllTypes(String sourceFolderStr) {
        printLine("getting all types");
        startProgress();
        Set<String> fileType=new HashSet<>();
        try {
            Path sourceFolder = Paths.get(sourceFolderStr);
            Files.walk(sourceFolder).forEach(path -> addFileType(path.toFile(),fileType));
        } catch (IOException e) {
            Output.exception(e);
        }
        stopProgress();
        return new ArrayList<>(fileType);
    }

    private static void addFileType(File file, Set<String> fileType) {
        if (file.isFile()) {
            String fileName=file.getAbsolutePath().trim().toLowerCase();
            String fileExtension=fileName.substring(fileName.lastIndexOf("."));
            if(fileExtension.isEmpty()){
                printLine("File without extension: "+fileName);
            }
            fileType.add(fileExtension);
        }
    }
    public void moveAllTypes(String sourceFolderStr, String destinationFolderStr, Set<String> fileType) {
        printLine("getting all types");
        List<File> filesToMove = getSpecificTypeFiles(sourceFolderStr, fileType);
        moveFilesWithSameFolderStructureOnDestination(sourceFolderStr,destinationFolderStr,filesToMove);
    }

    public static List<File> getSpecificTypeFiles(String sourceFolderStr, Set<String> fileType) {
        Path sourceFolder = Paths.get(sourceFolderStr);
        List<File> filesToMove = getSpecificTypeFiles(sourceFolder, fileType);
        return filesToMove;
    }
    public static List<File> getSpecificTypeFiles(Path sourceFolder, Set<String> fileType) {
        startProgress();
        List<File> filesToMove=new ArrayList<>();
        try {
            Files.walk(sourceFolder).forEach(path -> addFileTypeToMove(path.toFile(),filesToMove, fileType));
        } catch (IOException e) {
            Output.exception(e);
        }
        stopProgress();
        if(CollectionHelper.isEmpty(filesToMove)){
            printLine("No Matching file found");
        }
        return filesToMove;
    }


    private static void addFileTypeToMove(File file, List<File> fileToMove, Set<String> fileType) {
        if (file.isFile()) {
            String fileName=file.getAbsolutePath().trim().toLowerCase();
            int beginIndex = fileName.lastIndexOf(".");
            if(beginIndex<0){
                debug(". not found when looking for extension");
             return;
            }
            beginIndex++;
            if(beginIndex>=fileName.length()){
                debug(".(dot) found at end of the file there is no extention after .(dot)");
                return;
            }

            String fileExtension=fileName.substring(beginIndex);
            if(fileExtension.isEmpty()){
                printLine("File without extension: "+fileName);
                return;
            }

                if(fileType.contains(fileExtension)){
                    fileToMove.add(file);
                }
        }
    }

    public static void setAllFilesNameAndRepeatedFile(String sourceFolderStr, Set<String> allUniqueFileName, Map<String, Integer> repeatedFile) {
        Path sourceFolder = Paths.get(sourceFolderStr);
        startProgress();
        try {
            Files.walk(sourceFolder).forEach(path -> addFileNameToSet(path.toFile(),allUniqueFileName,repeatedFile));
        } catch (IOException e) {
            Output.exception(e);
        }
        stopProgress();
        if(CollectionHelper.isEmpty(allUniqueFileName)){
            printLine("No Matching file found");
        }
        return;
    }
    private static void addFileNameToSet(File file, Set<String> allUniqueFileName, Map<String, Integer> repeatedFile) {
        if (file.isFile()) {
            String fileName=file.getName().trim().toLowerCase();
            if(allUniqueFileName.contains(fileName)){
                Integer numberOfTimeFileRepeated=repeatedFile.get(fileName);
                if(numberOfTimeFileRepeated==null){
                    repeatedFile.put(fileName,2);
                }else{
                    repeatedFile.put(fileName,++numberOfTimeFileRepeated);
                }
            }else{
                allUniqueFileName.add(fileName);
            }

        }
    }
}
