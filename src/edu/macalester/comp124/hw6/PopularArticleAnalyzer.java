package edu.macalester.comp124.hw6;

import org.wikapidia.conf.ConfigurationException;
import org.wikapidia.core.dao.DaoException;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.model.LocalPage;

import java.util.*;

/**
 * Analyzes the overlap in popular concepts.
 * Experimental code for Shilad's intro Java course.
 * Note that you MUST correct WikAPIdiaWrapper.DATA_DIRECTORY before this works.
 *
 * @author Shilad Sen
 */
public class PopularArticleAnalyzer {
    private final WikAPIdiaWrapper wpApi;

    /**
     * Constructs a new analyzer.
     * @param wpApi
     */
    public PopularArticleAnalyzer(WikAPIdiaWrapper wpApi) {
        this.wpApi = wpApi;
    }


    /**
     * Returns the n most popular articles in the specified language.
     * @param language the Language we want to the articles from.
     * @param n an integer indicating the how many of the most popular articles we want.
     * @return an ArrayList with the n most popular articles in a language.
     */
    public List<LocalPage> getMostPopular(Language language, int n) {

        List<LocalPage> popularity = new ArrayList<LocalPage>();

        List<LocalPage> allLocalPages;
        List<LocalPagePopularity> allLocalPagePopularity = new ArrayList<LocalPagePopularity>();

        allLocalPages = wpApi.getLocalPages(language);

        for(LocalPage localPage : allLocalPages){
            int links = wpApi.getNumInLinks(localPage);
            LocalPagePopularity localPagePop = new LocalPagePopularity(localPage, links);
            allLocalPagePopularity.add(localPagePop);
        }

        Collections.sort(allLocalPagePopularity);
        Collections.reverse(allLocalPagePopularity);

        for(LocalPagePopularity locPagPop : allLocalPagePopularity.subList(0, n)) {
            popularity.add(locPagPop.getPage());

        }

        return popularity;
    }


    public static void main(String args[]) {
        Language simple = Language.getByLangCode("simple");

        WikAPIdiaWrapper wrapper = new WikAPIdiaWrapper();

        PopularArticleAnalyzer popArtAnalyzer = new PopularArticleAnalyzer(wrapper);
        List<LocalPage> popLocalPages = popArtAnalyzer.getMostPopular(Language.getByLangCode("simple"),20);
        for(LocalPage article: popLocalPages){
            System.out.println(article.getTitle());
        }
    }

}
