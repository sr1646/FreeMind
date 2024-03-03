### capacity of 25gb blu-ray drive is: XX.X GB


## Bluray improvement
 
###completed
Want to create database where i can store temprary vedeo i.e. not realy moved anywhere but it will store file info.
So whenever I wanted to move them to blu-ray I can move them. i.e. i will copy these file to folder and then img burn will burn them.

##VLC meadia player improvement
when opening folder if we are not opening folder do not reset existing playing play list

## LATEST
Need to create DVD writer upgrade
1. Will move content of dvd to some folder. (done)
2. Verify content of DVD it is worth to put. (done)
3. If removed some content from DVD then re-put content. (done)
4. Once content created for DVD write it to DVD. (done)
5. Once content written to DVD copy it to back=up done folder in same folder architecture as it was in back-up remaining (done)
6. I should be able to create multiple DVD folder if i wanted. (done)

## added
Favourite button are sorted by path+name
Adding button on favourite button mark that new button as favourite
If adding existing  button then it will mark existing button as favourite

### Guide for DVD
#### Variable To edit
| usage                                                      | Variable Name                                   | File               |
|------------------------------------------------------------|-------------------------------------------------|--------------------|
| if you want to create initially 3 dvd then change variable | `ALLOW_TOTAL_DVD_DISK_AS_OF_NOW = 3`            | **DVDHelper.java** | 
| Define size of Disk in GB                                  | `DVD_MAX_CAPACITY_IN_GB=22.5f`                  | **DVDHelper.java** |
| How much free space in DVD is ok                           | `ALLOWED_FREE_SPACE_OF_DVD_IN_MB = 100`         | **DVDHelper.java** |
| Source Folder for DVD ( can be taken run time also)        | `NS_SOURCE_FOLDER = "N:\\Tester\\source"`       | **sr/utility/Main.java** |
| Destination Folder for DVD ( can be taken run time also)   | `NS_DESTINATION_FOLDER = "N:\\Tester\\output";` | **sr/utility/Main.java** |


| DVD size | Actual Data We can Store |
|----------|--------------------------|
| 50 GB    | `Verify and update here` | 
| 25 GB    | `22.5 GB`                |
| 4.7 GB   | `Verify and update here` |

  
1. First Devide data for dvd execute task **6 DEVIDE_DATA_FOR_DVD**.  
2. Now look into files of DVD if it looks ok then no issue. if you think you don't want some DVD files then Delete/move them to temp folder from all or some DVD 
3. Now execute **9 DVD_PART_OUTPUT_TO_SOURCE** to sent files back to original folder (here all file you have selected will goto original folder)
4. Now again DVD and perform step 2-3 repeatedly until you not find DVD for which you don't want to delete any files.
5. Once you find DVD that does not need any changes write them to actual Disk and move files to back-up done folder.
6. Again repeat step 3,2,3,4,5
6. BONUS: you can move not needed files from dvd removed in step 2 also to back up done as you have already looked these files and you don't want them.
7. Repeat above and you are cool to go.
