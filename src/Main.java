
import com.kbuzsaki.degreechecker.WikiPage;
import com.kbuzsaki.degreechecker.WikiPageFactory;




public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // previous method, relied on the static variable
        // WikiPage.target to provide the target
        WikiPage wikiPage = WikiPage.getWikiPage("ocarina");
        System.out.println(wikiPage.getPathToTarget());
        
        // new method, has you specify the target when you create the factory
        WikiPageFactory hitlerTargetedFactory = new WikiPageFactory("adolf_hitler");
        WikiPage ocarinaPage = hitlerTargetedFactory.getWikiPage("ocarina");
        System.out.println(ocarinaPage.getPathToTarget());
        
        // this lets you search for two separate targets
        WikiPageFactory rwbyTargetedFactory = new WikiPageFactory("RWBY");
        WikiPage kittensPage = rwbyTargetedFactory.getWikiPage("kittens");
        System.out.println(kittensPage.getPathToTarget());
        
WikiPage wp1 = hitlerTargetedFactory.getWikiPage("kittens");
WikiPage wp2 = hitlerTargetedFactory.getWikiPage("kittens");
        
    }

}
