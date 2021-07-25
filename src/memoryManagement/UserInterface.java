package memoryManagement;

import process.PageTable;
import process.Process;

public class UserInterface {

    public static void main(String args[]){
        Process process = new Process("program1.txt");
        System.out.println(process.getProcessName() + " " + process.getTextSize() + " " + process.getFixedDataSize());
    }



}
