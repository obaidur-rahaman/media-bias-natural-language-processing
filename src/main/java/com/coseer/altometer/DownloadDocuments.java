package com.nlu.altometer;

import java.io.File;
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

@CommonsLog
public class DownloadDocuments {

    //private static final String LINK_CSS = ".list8 li a"; //for times of India
    //private static final String LINK_CSS = ".headlines ul li a";
    private static final List<String> DOC_TYPES = new ArrayList<>(Arrays.asList("pdf", "docx", "xlsx", "pptx"));
    //private static final String BASE_DIR = PrivateProperties.getProperty("wolves.base.dir");
    public List<Uncompacted> Download(String[] urlCSS) {
        String url = urlCSS[0];
        String LINK_CSS = urlCSS[1];


        UrlValidator urlValidator = new UrlValidator();
        Set<String> urls = new HashSet<>();

        try {
            Document dom = Fetcher.fetchDom(url);
            if (dom != null && dom.select(LINK_CSS) != null) {
                urls = dom.select(LINK_CSS).stream().map(e -> getAbsUrl(e)).filter(v -> urlValidator.isValid(v))
                        .collect(Collectors.toSet());
            }
        } catch (Exception e) {
            // log.error("Error while downloading: " + url, e);

        }
        //MultiThreader<String, Uncompacted> threader = new MultiThreader<String, Uncompacted>(32, 10, new
        //        Uncompacted());
        //threader.setDefaultMultiThreadable(getFiles());
        //String FILE_NAME;
        //return threader.streamRun(urls).filter(u -> u.hasValid(FILE_NAME)).collect(Collectors.toList());

        //ReadTextFromURL r1 = new ReadTextFromURL();
        //r1.GetText("https://www.-veterans");


        int nbrArticle = 0;
        for(String urlItem : urls){
            //System.out.println(urlItem);

            if (nbrArticle < 31) {
                DownloadText t1 = new DownloadText();

                String[] CSSTITLEANDBODY = {urlItem,urlCSS[2],urlCSS[3],urlCSS[4]};
                t1.Download(CSSTITLEANDBODY);
            }
            nbrArticle++;
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
    /*
    private MultiThreader.MultiThreadable<String, Uncompacted> getFiles() {
        return u -> {
            try {
                String file = u.split("\\?fileId=")[0];
                String fileId = u.split("\\?fileId=")[1];
                if (DOC_TYPES.contains(FilenameUtils.getExtension(file))) {
                    if (!new File(BASE_DIR + file).exists()) {
                        Filer.download(u, BASE_DIR);
                    }
                    Uncompacted uncompacted = new Uncompacted();
                    uncompacted.set(WolvesFields.FILE_NAME, file.split("/")[file.split("/").length - 1]);
                    uncompacted.set(WolvesFields.ID, fileId);
                    uncompacted.set(WolvesFields.PASSWORD, "");
                    uncompacted.set(UncompactedObj.ID_KEY, new ObjectId().toString());
                    return uncompacted;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Uncompacted();
        };
    }
    */

}