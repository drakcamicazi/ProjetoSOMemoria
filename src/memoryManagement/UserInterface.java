package memoryManagement;

import process.PageTable;

public class UserInterface {

    public static void main(String args[]){
        PageTable page = new PageTable(32);
        page.setValid(50);
    }



}
