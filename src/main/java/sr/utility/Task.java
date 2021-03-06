package sr.utility;

public enum Task {
    START_VLC("custom vlc for playing all vedeo from folder  and add them in different  folder and move these categorize vedeo whereever you wanted"),
    SHOW_ALL_FOLDER("for managing my google photos back up specifically, it will show all folder with count(how many time it is repeated) in my google photos backup"),
    GET_ALL_LINK_FROM_FILE("Extract all link from html document."),
    ADD_VALUE_IN_SET("Normal set implementation for removing duplicate values (currently it is taking input from file)"),
    MOVE_ALL_FILES_TO_FOLDER("Created for gcam potrait photo where it is creating folder for each photo.this program Move all files from one folder to another folder.(so it will move all files from folder that means if that source folder have multiple folder inside folder still it will move only file from them"),
    DEVIDE_DATA_FOR_DVD("For creating dvd you can create your own custom size dvd"),
    DVD_PART_TO_FOLDER("For Converting back DVD part into original folder"),
    GENERATE_SERIESE("generate seriese"),
    SHOW_SIZE_OF_FOLDERS("to view size of folders"),
    SHOW_ALL_TYPE_OF_FILE_FROM_FOLDER("Display all different file type from folder"),
    SEPARATE_VIDEO_AND_PHOTO("It will store vedeo in vedeo folder you have to provide all vedeo format for that i.e. .MP4"),
    LIST_SPECIFIC_TYPE_FILES("It will file with all matching extension i.e. .MP4"),
    COMPARE_FILE_BY_NAME_AND_EXTENSION("It will compare file by name even directory structure is different it will just compare the file names, Just fYi if same name file are there in multiple folder it will still consider them as same file but you will get list of file count from both folder to identify this error")
    ;


    private String description;

    private Task(String description){
        this.description=description;
    }
    public String getDescription(){
        return this.description;
    }

}
