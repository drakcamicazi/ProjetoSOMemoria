package process;

public class PageTable {

    private int numberOfPages;
    private int[][] pageTable;

    public PageTable(int numberOfPages){
        this.numberOfPages = numberOfPages;
        pageTable = new int[numberOfPages][2];
    }

    public PageTable(){
        this.numberOfPages = 32;
        pageTable = new int[numberOfPages][2];
    }

    /**
     * Returns 1 if page is valid; 0 if not
     * **/
    public int isValid(int pageNumber){

        return pageTable[pageNumber][0];
    }

    public void setValid(int pageNumber){
        try{
            pageTable[pageNumber][1] = 1;
        }catch (IndexOutOfBoundsException e){
            System.out.println("Invalid page number!");
        }
    }

    public int getFrameNumber(int pageNumber){
        if(this.isValid(pageNumber) == 1) {
            return this.pageTable[pageNumber][1];
        }else{
            System.out.println("A pagina " + pageNumber + " nao corresponde a uma entrada valida na tabela de paginas do processo.\n");
            return -1;
        }
    }


    public int getNumberOfPages() {
        return numberOfPages;
    }
}
