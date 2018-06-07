package com.nlu.altometer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

import com.nlu.core.tools.PrivateProperties;
import com.nlu.platform.browser.core.Fetcher;
import com.nlu.platform.browser.core.MultiThreader;
import com.nlu.platform.tools.Filer;
import com.nlu.platform.tools.types.Uncompacted;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.UrlValidator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
public class DownloadText {
    //private static final String LINK_CSSTITLE = ".entry-header";
    //private static final String LINK_CSSBODY = ".para.para_body";



    //private static final String LINK_CSS = ".para.para_body.char.char_$ID/[No_character_style]";
    private static final List<String> DOC_TYPES = new ArrayList<>(Arrays.asList("pdf", "docx", "xlsx", "pptx"));
    //private static final String BASE_DIR = PrivateProperties.getProperty("wolves.base.dir");
    public List<Uncompacted> Download(String[] urlAndCssTitleBody) {
        UrlValidator urlValidator = new UrlValidator();
        Set<String> urls = new HashSet<>();

        String url1 = urlAndCssTitleBody[0];
        String LINK_CSSTITLE = urlAndCssTitleBody[1];
        String LINK_CSSBODY = urlAndCssTitleBody[2];

        try {
            Document dom = Fetcher.fetchDom(url1);
            if (dom != null && dom.select(LINK_CSSBODY) != null) {
                //System.out.println(dom.select(LINK_CSS));
                String title = dom.select(LINK_CSSTITLE).text();
                String fileName = "./articles/" + urlAndCssTitleBody[3] + "-" + title + ".txt";
                //System.out.println(fileName);
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
                writer.append(dom.select(LINK_CSSTITLE).text());
                writer.append("\n\n\n\n");
                writer.append(dom.select(LINK_CSSBODY).text().toLowerCase());
                writer.append("\n\n\n\n--------------------------------------\n\n\n\n");
                writer.close();
            }
        } catch (Exception e) {
            // log.error("Error while downloading: " + url, e);

        }


        return null;
    }

    //download("https://timesofindia.indiatimes.com/");

    /**
     * fetches the absolute url defined in href of a dom element
     *
     * @param e
     */

    private String getAbsUrl(Element e) {
        String url = null;
        try {
            url = e.absUrl("href");
        } catch (Exception exc) {
            return "";
        }
        return url;
    }
}
