


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WikiPage wp = WikiPage.getWikiPage("ocarina");
        
        System.out.println(wp.getPathToTarget());
    }

}
