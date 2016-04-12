package edu.macalester.comp124.hw6;

import org.wikapidia.core.model.LocalPage;

/**
 * @author Shilad Sen
 */
public class LocalPagePopularity implements Comparable<LocalPagePopularity> {
    private LocalPage page;
    private int numInLinks;

    public LocalPagePopularity(LocalPage page, int numInLinks) {
        this.page = page;
        this.numInLinks = numInLinks;
    }

    public LocalPage getPage() {
        return page;
    }

    public int getPopularity() {
        return numInLinks;
    }

    @Override
    public int compareTo(LocalPagePopularity localPagePopularity) {
        if(numInLinks < localPagePopularity.numInLinks){
            return -1;
        }
        else if(numInLinks > localPagePopularity.numInLinks){
            return 1;
        }
        else{
            return 0;
        }

    }
}
