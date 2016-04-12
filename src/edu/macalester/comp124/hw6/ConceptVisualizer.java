package edu.macalester.comp124.hw6;

import acm.graphics.GDimension;
import acm.graphics.GImage;
import acm.graphics.GObject;
import acm.program.GraphicsProgram;
import org.wikapidia.core.lang.Language;
import org.wikapidia.core.model.LocalPage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Visualizes the most popular concepts in each language,
 * and the pages in other languages associated with the same concept.
 *
 * This class MUST be run as a Java application (ConceptVisualizer.main()).
 * This class MUST be run from the module directory.
 *
 * @author Shilad Sen (additions by Julia Romare)
 */
public class ConceptVisualizer extends GraphicsProgram {
    private static final int PAGES_PER_LANG = 30;

    // The three languages loaded in the database
    private static final Language SIMPLE = Language.getByLangCode("simple");
    private static final Language HINDI = Language.getByLangCode("hi");
    private static final Language LATIN = Language.getByLangCode("la");

    // The Wikapidia API object
    private WikAPIdiaWrapper wp;


    //A LocalPage of a reference of the last page being hovered over.
    private LocalPage lastPage;

    private LanguageBoxes simpleBoxes;
    private LanguageBoxes latinBoxes;
    private LanguageBoxes hindiBoxes;

    //A List of Arcs which represents the arcs that link between the page being hovered over to other pages.
    private List<Arcs> listArcs;

    // Descriptive label
    private FancyLabel label;

    /**
     * Lays out the graphic components of the widget.
     */
    public void init() {
        setSize(800, 400);
        wp = new WikAPIdiaWrapper();
        try {
            GImage bg = new GImage(ImageIO.read(getClass().getResource("/background.jpg")));
            bg.setSize(new GDimension(800, 400));
            add(bg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        label = new FancyLabel("Hover over a title to analyze it");
        label.setColor(ColorPallete.FONT_COLOR);

        add(label, 20, 20);
        simpleBoxes = makeBoxes(SIMPLE, ColorPallete.COLOR1, 150);
        hindiBoxes = makeBoxes(HINDI, ColorPallete.COLOR2, 225);
        latinBoxes = makeBoxes(LATIN, ColorPallete.COLOR3, 300);
        addMouseListeners();
        super.init();
    }

    public void run() {
        super.run();
        setSize(800, 400);
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        LocalPage hoverPage = getPageAt(e.getX(), e.getY());


        if(lastPage != null){
            lastPage = hoverPage;
            for(Arcs arc : listArcs){
                this.remove(arc);
            }
            listArcs = new ArrayList<>();

        }
        if (hoverPage == null) {
            simpleBoxes.unhighlight();
            latinBoxes.unhighlight();
            hindiBoxes.unhighlight();
            label.setText("Hover over a title to analyze it");

        } else {
            lastPage = hoverPage;
            List<LocalPage> pages = null;
            String description = "";

            System.out.println("You hovered over " + hoverPage);
            pages = wp.getInOtherLanguages(hoverPage);

            for(LocalPage pageInLang : pages){
                description = description + pageInLang.getLanguage().toString() + ": " + pageInLang.getTitle().toString() + "\n";

            }

            label.setText(description);

            simpleBoxes.highlightPages(pages);
            latinBoxes.highlightPages(pages);
            hindiBoxes.highlightPages(pages);

            this.drawArcs(hoverPage);
        }

    }


    /**
     * Draws arcs from the LocalPage that is being hovered over to pages it has links to.
     * @param hoverPage the LocalPage which is being hovered over.
     */
    private void drawArcs(LocalPage hoverPage){
        List<LocalPage> linkages = wp.getLocalPageLinks(hoverPage);
        LanguageBoxes langBoxes = findLangBoxes(hoverPage);
        List<LocalPageBox> linkPageBoxes = new ArrayList<LocalPageBox>();
        listArcs= new ArrayList<Arcs>();
        for(LocalPageBox localPageBox : langBoxes.getBoxes()){
            if(linkages.contains(localPageBox.getPage())){
                linkPageBoxes.add(localPageBox);
            }
        }

        LocalPageBox hoverPageBox = new LocalPageBox(Color.DARK_GRAY, hoverPage);

        for(LocalPageBox localPageBox : langBoxes.getBoxes()){
            if(hoverPage.equals(localPageBox.getPage())){
                hoverPageBox = localPageBox;
            }
        }


        for(LocalPageBox box : linkPageBoxes){
            if(box.getX()>hoverPageBox.getX()){
                if(hoverPage.getLanguage() == LATIN){
                    Arcs arc = new Arcs(hoverPageBox, box, 27, 260);
                    this.add(arc);
                    listArcs.add(arc);
                }
                if(hoverPage.getLanguage() == HINDI){
                    Arcs arc = new Arcs(hoverPageBox, box, 27, 186);
                    this.add(arc);
                    listArcs.add(arc);
                }
                if(hoverPage.getLanguage() == SIMPLE){
                    Arcs arc = new Arcs(hoverPageBox, box, 27, 110);
                    this.add(arc);
                    listArcs.add(arc);
                }

            }
            else{
                if(hoverPage.getLanguage() == SIMPLE){
                    Arcs arc = new Arcs(box, hoverPageBox, 27, 110);
                    this.add(arc);
                    listArcs.add(arc);
                }
                if(hoverPage.getLanguage() == HINDI){
                    Arcs arc = new Arcs(box, hoverPageBox, 27, 186);
                    this.add(arc);
                    listArcs.add(arc);
                }
                if(hoverPage.getLanguage() == LATIN){
                    Arcs arc = new Arcs(box, hoverPageBox, 27, 260);
                    this.add(arc);
                    listArcs.add(arc);
                }


            }
        }

    }



    /**
     * Finds the LanguageBoxes of the same language of a LocalPage's.
     * @param page a LocalPage which we want to get the linkages to.
     * @return LanguagesBoxes that have pages of the same language of the input.
     */
    private LanguageBoxes findLangBoxes(LocalPage page){
        if(page.getLanguage().equals(SIMPLE)){
            return simpleBoxes;
        }

        if(page.getLanguage().equals(HINDI)){
            return hindiBoxes;
        }

        if(page.getLanguage().equals(LATIN)){
            return latinBoxes;
        }

        return null;
    }

    /**
     * Creates boxes for a particular language.
     * @param language
     * @param color
     * @param y
     * @return
     */
    private LanguageBoxes makeBoxes(Language language, Color color, int y) {
        PopularArticleAnalyzer analyzer = new PopularArticleAnalyzer(wp);
        List<LocalPage> popular = analyzer.getMostPopular(language, PAGES_PER_LANG);
        LanguageBoxes boxes = new LanguageBoxes(color, language, popular);
        add(boxes, 20, y);
        return boxes;
    }

    /**
     * Returns the page at an x, y location
     * @param x a double of the x-coordinate of the location.
     * @param y a double of the y-coordinate of the location.
     * @return
     */
    private LocalPage getPageAt(double x, double y) {
        GObject o = getElementAt(x, y);
        if (o instanceof LanguageBoxes) {
            LanguageBoxes boxes = (LanguageBoxes)o;
            LocalPageBox box = boxes.getLocalBoxAt(x, y);
            if (box != null) {
                return box.getPage();
            }
        }
        return null;
    }

    public static void main(String args[]) {
        ConceptVisualizer visualizer = new ConceptVisualizer();
        visualizer.start(args);
    }
}
