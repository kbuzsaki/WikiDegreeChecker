
import com.kbuzsaki.degreechecker.WikiPage;
import com.kbuzsaki.degreechecker.WikiPageFactory;
import java.util.List;




public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Usage Guidlines:
        
        // Create a factory with the title of the target page as an argument
        WikiPageFactory hitlerTargetedFactory = new WikiPageFactory("adolf_hitler");
        
        // Create a WikiPage with the title of the desired start page by
        // using the static factory
        WikiPage ocarinaPage = hitlerTargetedFactory.getWikiPage("ocarina");
        
        // Query the wiki page for its closest path to the target.
        List<WikiPage> pathToTarget = ocarinaPage.getPathToTarget();
        
        // Display the path
        System.out.println(pathToTarget);
    }

}
