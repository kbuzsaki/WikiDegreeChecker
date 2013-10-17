package com.kbuzsaki.degreechecker;


import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Kyle
 */
public class WikiPageFactory {

    private final Map<String, WikiPage> pageCache;
    private final WikiPage target;
    
    public WikiPageFactory(String targetPageTitle) {
        pageCache = new HashMap<>();
        target = getWikiPage(targetPageTitle);
    }
    
    public WikiPage getWikiPage(String pageTitle) {
        if(pageCache.containsKey(pageTitle)) {
            return pageCache.get(pageTitle);
        }
        else {
            WikiPage page = new WikiPage(this, pageTitle);
            pageCache.put(pageTitle, page);
            return page;
        }
    }
    
    public WikiPage getTarget() {
        return target;
    }
}
