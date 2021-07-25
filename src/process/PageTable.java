package process;

public class PageTable {

    private int numberOfPages;
    private int[][] pageTable;

    public PageTable(int numberOfPages){
        this.numberOfPages = numberOfPages;
        pageTable = new int[numberOfPages][3];
    }

    /**
     * Returns 1 if page is valid; 0 if not
     * **/
    public int isValid(int pageNumber){
        return pageTable[pageNumber][1];
    }

    public void setValid(int pageNumber){
        try{
            pageTable[pageNumber][1] = 1;
        }catch (IndexOutOfBoundsException e){
            System.out.println("Invalid page number!");
        }
    }


}
