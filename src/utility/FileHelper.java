package utility;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static utility.Output.*;

public class FileHelper {
    private int counter;
    private static final int GIGABYTE = 1024;
    private final String encoding = "UTF-8";
    private List<String> skipDirectory=new ArrayList<>(Arrays.asList("Google Photos","Takeout"));
    private List<String> skipDirectoryStartWith=new ArrayList<>(Arrays.asList("takeout","Photos from"));
    private Map<String,Integer> folder;



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
        close(writer);
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), encoding));
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
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(reader);
        }
        return list;
    }

    private  void close(BufferedReader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            reader=null;
        }
    }

    private  void close(BufferedWriter writer) {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        folder=MapHelper.sortByKey(folder);

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
            e.printStackTrace();
        }

        dvdHelper.visitSkippedFile(dvdList);
        int part=1;
        decorate("*",30);
        boolean isMergeFromDVD=false;// that means we are deviding data for dvd
        for(DVDHelper DVD :dvdList){
            printLine(" "+part+" - "+DVD);
            moveFilesWithSameFolderStructureOnDestination(destinationFolderStr+"//"+part++, DVD.getFilesToMove(),sourceFolderStr,isMergeFromDVD);
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
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }
    private void moveFilesWithSameFolderStructureOnDestination(String destinationFolderStr, List<File> filesToMove, String sourceFolderStr,boolean isMergeFromDVD) {
        if(filesToMove==null ||filesToMove.isEmpty()){
            printLine("No file's Selected for Moving");
            return;
        }
        int totalFiles=filesToMove.size();
        printLine("Total "+totalFiles+" file/s will be moved");
        decorate("*",10);
        printLine("Progress: ");
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
            e.printStackTrace();
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
    public void showFileFolerWithSize(String sourceFolder){
        File dir = new File(sourceFolder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing == null) {
            return;
        }

        Map<String, Long> fileWithSize = new LinkedHashMap<String, Long>();
        for (File child : directoryListing) {
            fileWithSize.put(child.getName(), size(child.toPath()));
        }
        Map<String, Long> sortedMap = new MapSort().sortMapByValue(fileWithSize);
        int srNo = 1;
        for (Object entry : sortedMap.entrySet()) {
            Map.Entry entryv = (Map.Entry<String, Long>) entry;
            printLine((srNo++) + "\t" + entryv.getKey() + "\t" + getSizeInGB((Long) entryv.getValue()));
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
                e.printStackTrace();
            }
        }

        moveFilesWithSameFolderStructureOnDestination(destinationFolderStr,filesToMove ,sourceFolderStr,true);

        return true;
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
            e.printStackTrace();
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
    public void moveAllTypes(String sourceFolderStr, String destinationFolderStr, String fileType) {
        printLine("getting all types");
        startProgress();
        List<File> filesToMove=new ArrayList<>();
        try {
            Path sourceFolder = Paths.get(sourceFolderStr);
            Files.walk(sourceFolder).forEach(path -> addFileTypetoMove(path.toFile(),filesToMove,fileType));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopProgress();
        moveFilesWithSameFolderStructureOnDestination(destinationFolderStr,filesToMove ,sourceFolderStr,false);
    }
    private static void addFileTypetoMove(File file, List<File> fileToMove, String fileType) {
        if (file.isFile()) {
            String fileName=file.getAbsolutePath().trim().toLowerCase();
            String fileExtension=fileName.substring(fileName.lastIndexOf("."));
            if(fileExtension.isEmpty()){
                printLine("File without extension: "+fileName);
                return;
            }
            fileType=fileType.trim().toLowerCase();
            if(fileExtension.equalsIgnoreCase(fileType)){
                fileToMove.add(file);
            }
        }
    }

    /*
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    Only Public method End
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    ################################################################################################################################
    */
}
