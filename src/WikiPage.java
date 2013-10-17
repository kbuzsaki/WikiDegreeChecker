
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Kyle
 */
public final class WikiPage {
    
    private static final String mediaWikiStarter = "http://en.wikipedia.org/w/"
            + "api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=";
    private static final int NOT_CALCULATED = -1;
    private static final Map<String, WikiPage> pageCache = new HashMap<>(); 
    
    private static final WikiPage target = getWikiPage("adolf_hitler");
    
    private String pageTitle;
    private URL pageUrl;
    private Set<WikiPage> linkedPages = null;
    private List<WikiPage> pathToTarget = null;
    
    private WikiPage parent = null;
    
    public static WikiPage getWikiPage(String pageTitle) {
        if(pageCache.containsKey(pageTitle)) {
            return pageCache.get(pageTitle);
        }
        else {
            WikiPage page = new WikiPage(pageTitle);
            pageCache.put(pageTitle, page);
            return page;
        }
    }
    
    private WikiPage(String pageTitle) {
        try {
            this.pageTitle = pageTitle;
            pageUrl = new URL(mediaWikiStarter + pageTitle);
        } 
        catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid Page Title: " + pageTitle, ex);
        }
    }
    
    public String getPageTitle() {
        return pageTitle;
    }
    public String getPageText() {
        StringBuilder pageText = new StringBuilder();
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(pageUrl.openStream()))) {
            String str;
            while((str = in.readLine()) != null) {
                pageText.append(str).append("\n");
            }
        }
        catch (IOException ex) {
            Logger.getLogger(WikiPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pageText.toString();
    }
    public Set<WikiPage> getLinkedPages() {
        if(linkedPages != null) {
            return linkedPages;
        }
        
        Set<WikiPage> tempLinkedPages = new HashSet<>();
        
        Set<String> pageTitles = getPageTitles(getPageText());
        for(String pageTitle : pageTitles) {
            tempLinkedPages.add(getWikiPage(pageTitle));
        }
        
        linkedPages = Collections.unmodifiableSet(tempLinkedPages);
        return linkedPages;
    }
    
    private WikiPage getParent() {
        return parent;
    }
    private void setParent(WikiPage parent) {
        this.parent = parent;
    }
    public List<WikiPage> getParentChain() {
        List<WikiPage> parentChain = new ArrayList<>();
        parentChain.add(this);
        
        if(parent != null) {
            parentChain.addAll(0, getParent().getParentChain());
        }
        
        return parentChain;
    }
    
    public boolean isAdjacentToTarget() {
        return getLinkedPages().contains(target);
    }
    
    public int getDistanceFromTarget() {
        return getPathToTarget().size();
    }
    public List<WikiPage> getPathToTarget() {
        if(!isPathCalculated()) {
            pathToTarget = calculatePathToTarget();
        }
        List<WikiPage> path = new ArrayList<>(pathToTarget);
        path.add(target);
        return path;
    }
    private boolean isPathCalculated() {
        return pathToTarget != null;
    }
    private List<WikiPage> calculatePathToTarget() {
        WikiPage currentClosest = null;
        Set<WikiPage> checkedPages = new HashSet<>();
        
        int depth = 0;
        Set<WikiPage> uncheckedPages = new HashSet<>();
        uncheckedPages.add(this);
        while(currentClosest == null || depth < currentClosest.getDistanceFromTarget()) {
            depth++;
            System.out.println("New Depth: " + depth);
            
            if(uncheckedPages.isEmpty()) {
                System.out.println("No more pages, terminating.");
                break;
            }
            
            for(WikiPage page : uncheckedPages) {
                System.out.println("Depth " + depth + ", Checking page: " + page.pageTitle);
                if(page.isAdjacentToTarget()) {
                    return page.getParentChain();
                }
                else if(page.isPathCalculated()) {
                    if(currentClosest == null || page.getDistanceFromTarget() < currentClosest.getDistanceFromTarget()) {
                        currentClosest = page;
                    }
                }
            }
            
            checkedPages.addAll(uncheckedPages);
            Set<WikiPage> linkedPages = new TreeSet<>(new Comparator<WikiPage>() {

                @Override
                public int compare(WikiPage o1, WikiPage o2) {
                    return o1.getPageTitle().compareToIgnoreCase(o2.getPageTitle());
                }
                
            });
            for(WikiPage page : uncheckedPages) {
                if(!page.isPathCalculated()) {
                    Set<WikiPage> pageLinkedPages = new HashSet<>(page.getLinkedPages());
                    pageLinkedPages.removeAll(checkedPages);
                    // overwrite the parent
                    for(WikiPage pageLinkedPage : pageLinkedPages) {
                        pageLinkedPage.setParent(page);
                    }
                    linkedPages.addAll(pageLinkedPages);
                }
            }
            
            uncheckedPages = linkedPages;
        }
        
        return currentClosest.getParentChain();
    }
    
    @Override
    public String toString() {
        return pageTitle;
    }
    
    private static final String[] truncators = {"#", "|"};
    private static final String[] invalidators = { ":" };
    public static Set<String> getPageTitles(String pageText) {
        Set<String> matches = new HashSet<>();
        
        Pattern pattern = Pattern.compile("\\[\\[[^]]+?\\]\\]");

        Matcher matcher = pattern.matcher(pageText);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        
        Set<String> processedPagetitles = new HashSet<>();
        for(String match : matches) {
            // removing junk
            String processedMatch = match.substring(2, match.length() - 2);
            for(String truncator : truncators) {
                if(processedMatch.contains(truncator)) {
                    processedMatch = processedMatch.substring(0, processedMatch.indexOf(truncator));
                }
            }
            
            // formatting 
            processedMatch = processedMatch.replace(" ", "_");
            processedMatch = processedMatch.toLowerCase();
            
            boolean valid = true;
            for(String invalidator : invalidators) {
                if(processedMatch.contains(invalidator)) {
                    valid = false;
                    break;
                }
            }
            if(valid) {
                processedPagetitles.add(processedMatch);
            }
        }
        
        return processedPagetitles;
    }
    
}
