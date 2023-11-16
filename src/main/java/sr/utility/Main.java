package sr.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sr.basic.Series;
import uk.co.caprica.vlcjplayer.VlcjPlayer;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static sr.utility.Output.print;
import static sr.utility.Output.printLine;

public class Main {

    public static final String NS_SOURCE_FOLDER = "N:\\Tester\\source";
    public static final String NS_DESTINATION_FOLDER = "N:\\Tester\\output";

    private void showMenu(){
        for(Task task:Task.values()){
            printLine(task.ordinal()+" "+task);
            printLine("\t"+task.getDescription()+"\n");
        }
        printLine("Enter your choice");
    }
    public void driver(){
        showMenu();
        Task task = getTaskFromStandardInput();
//        Task task=Task.START_VLC ;
        print("Selected Task: "+task);

        switch (task){
            case SHOW_ALL_FOLDER:
                new FileHelper().showAllFolder("E:\\google_photos\\do_experiments\\4gb\\test1");
                break;
            case GET_ALL_LINK_FROM_FILE:
                    getAllLink();
                break;
            case ADD_VALUE_IN_SET:
                addValueInSet();
                break;
            case MOVE_ALL_FILES_TO_FOLDER:
                moveFiles(true);
                break;
            case COPY_ALL_FILES_TO_FOLDER:
                moveFiles(false);
                break;
            case DEVIDE_DATA_FOR_DVD:
                devideDvdCompatible();
                break;
            case PROCESS_STORED_DVD_DATA:
                processDvdData();
                break;
            case DVD_PART_TO_FOLDER:
                DVDDataToFolder();
                break;
            case DVD_PART_OUTPUT_TO_SOURCE:
                DVDDataToFolderOutputToSource();
                break;
            case DVD_PART_OUTPUT_TO_BACKUP_DONE_FOLDER:
                DVDDataToFolderOutputToBackUpDoneFolder();
                break;
            case GENERATE_SERIESE:
                System.out.println(new Series().getSeriese());
                break;
            case SHOW_SIZE_OF_FOLDERS:
                showFolderSize(NS_SOURCE_FOLDER);
                break;
            case SHOW_ALL_TYPE_OF_FILE_FROM_FOLDER:
                showTypesFromFolder(NS_SOURCE_FOLDER);
                break;
            case SEPARATE_VIDEO_AND_PHOTO:
                moveSpecificTypeFile(NS_SOURCE_FOLDER,NS_DESTINATION_FOLDER,FileHelper.VEDEO_FILES_EXTENSION);
                break;
            case LIST_SPECIFIC_TYPE_FILES:
                showFilesMatchingWithType(NS_SOURCE_FOLDER,FileHelper.VEDEO_FILES_EXTENSION);
                break;
            case COMPARE_FILE_BY_NAME_AND_EXTENSION:
//                compareFolder("C:\\as\\experiment\\original","C:\\as\\experiment\\test");
                compareFolder(NS_SOURCE_FOLDER,NS_DESTINATION_FOLDER);
                break;
            case START_VLC:
                runVlc();
                break;
            default :
                throw new IllegalStateException("Unexpected value: " + task);
        }
    }

    private void processDvdData() {
        List<String> dvdData = getSelectedStoreDVDData();
        if (dvdData == null) return;

        String sourceFolder=dvdData.get(DVDHelper.STORED_DVD_SOURCE_FOLDER_INDEX);
        String destincationFolder=dvdData.get(DVDHelper.STORED_DVD_DESTINATION_FOLDER_INDEX);
        Integer currentProgress = getCurrentProgress(dvdData);

        if (currentProgress == null) return;
        List<DVDHelper> dvdList = getDVDList(dvdData);
        if(currentProgress>dvdList.size()){
            Output.printLine("ALL DVD already return please reset progress to start it again");
            return;
        }

        DVDHelper dvdToWrite=dvdList.get(++currentProgress);
        Output.decorate("*");
        Output.printLine("Currently writing DVD: "+currentProgress);
        Output.decorate("*");
        FileHelper fileHelper = new FileHelper();
        fileHelper.deleteDirectory(new File(destincationFolder));
        fileHelper.setFileOpernation(FileHelper.FileOpernation.COPY);
        fileHelper.setDataForDVD(sourceFolder,destincationFolder,currentProgress,dvdToWrite);

        String dvdStoreFile=dvdData.get(DVDHelper.STORED_DVD_FILE_NAME_INDEX);

        FileHelper.updateProgress(currentProgress,dvdStoreFile);
    }

    private Integer getCurrentProgress(List<String> dvdData) {
        int currentProgress=0;
        String strCurrentProgress="";
        try{
            strCurrentProgress= dvdData.get(DVDHelper.STORED_DVD_CURRENT_PROGRESS_INDEX);
            currentProgress=Integer.valueOf(strCurrentProgress);
        }catch (RuntimeException e){
            Output.exception(e);
            Output.printLine("Current progress not readed correctly input number: "+strCurrentProgress);
            return null;
        }
        return currentProgress;
    }

    private List<DVDHelper> getDVDList(List<String> dvdData) {
        String dvdDataJson= dvdData.get(DVDHelper.STORED_DVD_DATA_INDEX);
        ObjectMapper mapper = new ObjectMapper();
        List<DVDHelper> dvdList=null;
        try {
            dvdList = Arrays.asList(mapper.readValue(dvdDataJson,DVDHelper[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return dvdList;
    }

    private List<String> getSelectedStoreDVDData() {
        List<File> allFiles = FileHelper.getAllFiles(DVDHelper.STORED_DVD);
        int counter=0;
        int choice=0;
        Iterator<File> iterator=allFiles.iterator();
        while(iterator.hasNext()){
            if(iterator.next().toString().contains(DVDHelper.STORED_DVD_PROGRESS_FILE) || iterator.next().toString().contains(DVDHelper.STORED_DVD_DETAIL_FILE)){
                iterator.remove();
            }
        }

        if(allFiles.size()>1){
            for(File file:allFiles){
                System.out.println((counter++)+" : "+file);
            }
            choice = getSelectedDVDStore(allFiles);
        }
        String dvdStoreFile=allFiles.get(choice).getAbsolutePath();
        List<String> dvdData=FileHelper.readFile(dvdStoreFile);
        List<String> dvdProgress=FileHelper.readFile(dvdStoreFile+DVDHelper.STORED_DVD_PROGRESS_FILE);
        dvdData.addAll(dvdProgress);
        if(dvdData.size()!=DVDHelper.STORED_DVD_DATA_SIZE){
            Output.printLine("invalid DVD Store File there should be exact 4 line");
            return null;
        }
        dvdData.add(dvdStoreFile);
        return dvdData;
    }

    private int getSelectedDVDStore(List<File> allFiles) {
        Scanner stdIn=new Scanner(System.in);
        int choice=-1;
        boolean isInvalidChoice=false;
        do{
            if(isInvalidChoice){
                System.out.println("Invalid choice please try again");
            }
            isInvalidChoice=true;
            System.out.println("Enter index of file you want to continue processing of dvd: ");
            try{
                choice=Integer.valueOf(stdIn.nextLine());
            }catch (RuntimeException e){
                Output.exception(e);
            }
        }while (choice<0 || choice>= allFiles.size());
        return choice;
    }

    private void runVlc() {
        try {
            VlcjPlayer.startVlc();
        } catch (InterruptedException e) {
            Output.exception(e);
        }
    }

    private void compareFolder(String source, String destination) {
        Set<String> sourceFileName=new HashSet<>();
        Map<String,Integer> sourceRepeatedFile=new HashMap<>();
        FileHelper.setAllFilesNameAndRepeatedFile(source,sourceFileName,sourceRepeatedFile);

        Set<String> destinationFileName=new HashSet<>();
        Map<String,Integer> destinationRepeatedFile=new HashMap<>();
        FileHelper.setAllFilesNameAndRepeatedFile(destination,destinationFileName,destinationRepeatedFile);
        compareTwoFolderFileName(sourceFileName, destinationFileName);
        if(sourceRepeatedFile.equals(destinationRepeatedFile)){
              printLine("Both folder have same repeated File");
        }else{
            printRepeatedFile(source,sourceRepeatedFile);
            printRepeatedFile(destination,destinationRepeatedFile);
        }


    }

    private void compareTwoFolderFileName(Set<String> firstSet, Set<String> secondSet) {
        Set one = new HashSet<>(firstSet);
        Set two = new HashSet<>(secondSet);
        one.removeAll(secondSet);
        two.removeAll(firstSet);
        boolean bothFolderHaveSameFile=true;
        if(CollectionHelper.isNotEmpty(one)){
            bothFolderHaveSameFile=false;
            System.out.println("These are the extra file in Source Folder: [Total File: "+one.size()+" ]: "+one);
        }
        if(CollectionHelper.isNotEmpty(two)){
            bothFolderHaveSameFile=false;
            System.out.println("These are the extra file in destination Folder: [Total File: "+two.size()+" ]: "+two);
        }
        if(bothFolderHaveSameFile){
            System.out.println("Both folder have same file name");
        }
    }

    private void printRepeatedFile(String source, Map<String, Integer> repeatedFile) {
        if(CollectionHelper.isNotEmpty(repeatedFile)){
            printLine("\n\n"+source+" folder repeated file list: [Total File Repeated: "+repeatedFile.size()+" ]");
            int totalRepeatedFile = repeatedFile.values().stream().mapToInt(Integer::intValue).sum();
            printLine("Sum of all repeatedFile: "+totalRepeatedFile);
            String fileDetail=repeatedFile.entrySet().stream()
                    .map(e -> e.getKey() + " Repeated " + e.getValue()+" Times, ")
                    .collect(Collectors.joining(""));
            print(fileDetail);
        }else{
            printLine("\n\n"+source+" folder non of the file name repeated");
        }
    }

    private void showFilesMatchingWithType(String sourceFolderStr, Set<String> fileType) {
        List<File> fileList =FileHelper.getSpecificTypeFiles(sourceFolderStr,fileType);
        for(File file:fileList){
            printLine(file.getAbsolutePath());
        }
    }

    private void moveSpecificTypeFile(String sourceFolder, String destinationFolderStr, Set<String> typeOfFile) {
        new FileHelper().moveAllTypes(sourceFolder, destinationFolderStr, typeOfFile);
    }

    private void showTypesFromFolder(String sourceFolderStr) {
        printLine(new FileHelper().getAllTypes(sourceFolderStr));
    }


    private void DVDDataToFolder() {
        String sourcePath=NS_SOURCE_FOLDER
                ,destinationPath=NS_DESTINATION_FOLDER;
//        Scanner standardInput=new Scanner(System.in);
//        print("Enter source folder:");
//        sourcePath=standardInput.nextLine();
//        print("Enter destination folder:");
//        destinationPath=standardInput.nextLine();
        new FileHelper().DVDFilesToFolder(sourcePath,destinationPath);
    }
    private void DVDDataToFolderOutputToSource() {
        String sourcePath=NS_SOURCE_FOLDER
                ,destinationPath=NS_DESTINATION_FOLDER;
//        Scanner standardInput=new Scanner(System.in);
//        print("Enter source folder:");
//        sourcePath=standardInput.nextLine();
//        print("Enter destination folder:");
//        destinationPath=standardInput.nextLine();
        new FileHelper().DVDFilesToFolder(destinationPath,sourcePath);
    }private void DVDDataToFolderOutputToBackUpDoneFolder() {
        String sourcePath=NS_DESTINATION_FOLDER
                ,destinationPath="N:\\Tester\\backup_done";
//        Scanner standardInput=new Scanner(System.in);
//        print("Enter source folder:");
//        sourcePath=standardInput.nextLine();
//        print("Enter destination folder:");
//        destinationPath=standardInput.nextLine();
        new FileHelper().DVDFilesToFolder(sourcePath,destinationPath);
    }

    private void showFolderSize(String sourceFolde) {
        File dir = new File(sourceFolde);
        new FileHelper().showFileFolerWithSize(dir);
    }

    private void devideDvdCompatible() {

        String sourcePath= NS_SOURCE_FOLDER
                ,destinationPath= NS_DESTINATION_FOLDER;
//        Scanner standardInput=new Scanner(System.in);
//        print("Enter source folder:");
//        sourcePath=standardInput.nextLine();
//        print("Enter destination folder:");
//        destinationPath=standardInput.nextLine();
       new FileHelper().devideFilesForDVD(sourcePath,destinationPath);
    }

    private void moveFiles(boolean isMove) {
        Scanner standardInput=new Scanner(System.in);
        String sourcePath,destinationPath;
        printLine("Enter source folder:");
        sourcePath=standardInput.nextLine();
        printLine("Enter destination folder:");
        destinationPath=standardInput.nextLine();
        FileHelper fileHelper=new FileHelper();
        if(isMove){
            fileHelper.setFileOpernation(FileHelper.FileOpernation.MOVE);
        }else {
            fileHelper.setFileOpernation(FileHelper.FileOpernation.COPY);
        }
        fileHelper.moveFiles(sourcePath,destinationPath);
    }

    private Task getTaskFromStandardInput() {
        Scanner standardInput=new Scanner(System.in);
//        int choice=-1;
        int choice=standardInput.nextInt();

        if(choice< 0 || choice>=Task.values().length){
            printLine("Invalid choice: "+choice+", no such task exist");
            System.exit(1);
        }
        Task task=Task.values()[choice];
        return task;
    }

    private void addValueInSet() {
        Scanner standardInput=new Scanner(System.in);
        int set=1;
        Set<String> unique = addValueInSetFirstTime(standardInput, set++);
        nextSetCompare(standardInput, set++,unique);

    }

    private Set<String> addValueInSetFirstTime(Scanner standardInput, int set) {
        InputUtil choice = getInputFromConsoleOrFile(standardInput, "Do you want to provide input for set "+ set +" from MasterMind.txt?");

        Set<String> unique=new LinkedHashSet<>();
        switch (choice){
            case READ_FROM_FILE:
                List<String> fileData = new FileHelper().readFile("MasterMind.txt");
                for (String line : fileData) {
                    unique.add(line);
                }
                break;

            case READ_FROM_CONSOLE:
                final String terminator="astop";
                printLine("Enter "+terminator+" anywhere to stop inputting values");
                do{
                    String input= standardInput.nextLine().trim();
                    if(terminator.equalsIgnoreCase(input)){
                        break;
                    }
                    unique.add(input);
                }while(true);
                break;
        }
        return unique;
    }
    private Set<String> nextSetCompare(Scanner standardInput, int set, Set<String> unique) {
        printLine("Success fully added values in set");
        final int DISPLAY_IN_SORTED_ORDER=1;
        final int COMPARE_WITH_OTHER_SET=2;
        printLine("\n1 For display value in sorted order");
        printLine("2 For compare this set with another set i.e. program will say value exist or not as well as provide new values list separately");

        int nextOperation=Integer.valueOf(standardInput.nextInt());

        switch (nextOperation){
            case DISPLAY_IN_SORTED_ORDER:
                List<String> sortedList=new ArrayList( Arrays.asList(unique));
                Collections.sort(sortedList);
                printLine(sortedList);
                break;
            case COMPARE_WITH_OTHER_SET:
                break;
            default:
                printLine("Invalid Input so displaying data in sorted order");
                List<String> sortedList1=new ArrayList( Arrays.asList(unique));
                Collections.sort(sortedList1);
                printLine(sortedList1);
        }

        InputUtil choice = getInputFromConsoleOrFile(standardInput, "Do you want to provide input for set "+ set +" from MasterMind.txt?");

        switch (choice){
            case READ_FROM_FILE:
                List<String> fileData = new FileHelper().readFile("MasterMind.txt");
                for (String line : fileData) {
                    unique.add(line);
                }
                break;

            case READ_FROM_CONSOLE:
                final String terminator="astop";
                printLine("Enter "+terminator+" anywhere to stop inputting values");
                do{
                    String input= standardInput.nextLine().trim();
                    if(terminator.equalsIgnoreCase(input)){
                        break;
                    }
                    unique.add(input);
                }while(true);
                break;
        }
        return unique;
    }
    private InputUtil getInputFromConsoleOrFile(Scanner standardInput, String message) {
        int choice=-1;
        boolean wrongChoice=false;
        do{
            if (wrongChoice){
                printLine("\nInvalid choice, Please try again.");
            }
            printLine(message);
            for(InputUtil iu:InputUtil.values()){
                printLine(iu.ordinal()+" "+iu);
            }
            choice= Integer.valueOf(standardInput.nextLine());
            wrongChoice=true;
        }while(choice<0 || choice>=InputUtil.values().length);
        return InputUtil.values()[choice];
    }

    private void getAllLink() {
        List<String> fileData=new FileHelper().readFile("MasterMind.txt");
        Pattern p = Pattern.compile("(\"http).*\"");
        List<String> allLinks = new ArrayList<>(100);
        for(String line:fileData){
            Matcher m = p.matcher(line);
            allLinks.addAll(getAllMatch(m));
        }
        printLine("total links: "+allLinks.size());
        printLine(allLinks);
    }

    public List<String> getAllMatch(Matcher matcher) {
        List<String> result = new ArrayList<String>();
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }



    private static void mapExample() {
        Map<String,Long> map=new HashMap<String,Long>();
        map.put("3",1522L);
        map.put("4",1523L);
        map.put("1",153L);
        map.put("5",15123L);
        map.put("2",152L);
        map.put("6",1153L);

        Map<String,Long> sortedMap= new MapSort().sortMapByValue(map);

        for (Object entry : sortedMap.entrySet()){
            Map.Entry  entryv=(Map.Entry<String,Long>)entry;
            System.out.println("Key = " + entryv.getKey() +
                    ", Value = " + entryv.getValue());
        }
    }

    private static void sortArray(int[] array) {
        int minValue,minIndex;
        for(int i = 0; i< array.length; i++){
            minValue= array[i];
            minIndex=i;
            for(int j = i+1; j< array.length; j++){
                if(minValue> array[j]){
                    minIndex=j;
                    minValue= array[j];
                }
            }
            if(minIndex==i){
                //do nothing
            }else{
                array[minIndex]= array[i];
                array[i]=minValue;
            }
        }

    }
    public static void main(String[] args)
    {
        new Main().driver();
    }

}
