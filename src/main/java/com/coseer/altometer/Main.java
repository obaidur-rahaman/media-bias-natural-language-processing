package com.nlu.altometer;

import com.nlu.core.nlp.CoseerTagger;
import com.nlu.core.nlp.CoseerTaggerFlags;
import com.nlu.core.nlp.DocSentenceExtractor;
import com.nlu.core.nlp.PosTag;
import com.nlu.core.nlp.TaggerOutput;
import com.nlu.core.tools.analyzers.ReaderTextAnalyzer;
import com.nlu.core.tools.analyzers.ShingleAnalyzer;
import com.nlu.core.tools.analyzers.TextAnalyzer;
import com.nlu.core.tools.types.JaccardSet;
import com.nlu.core.tools.types.ValuePair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.lucene.util.Version;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Main {
    //Please rename accordingly or delete Class
    public static void main(String... args) throws IOException {
        //System.out.println("Hello");
        String[][] urlAndCss = new String[][]{
                // Some conservative journals

                {"http://www.foxnews.com/politics.html", ".title a", ".headline.head1", ".article-body","foxnews"},

                {"http://www.nationalreview.com/",".homepage_item.article a",".ap-0.article-header",".white-bg-lg",
                        "nationalreview"},

                {"https://www.newsmax.com/",".nmNewsfrontHead a","h1.article",""
                        + ".artPgMnStryWrapper",
                        "newsmax"},

                {"https://townhall.com/", ".story__text a", "h1.headline", ".col-xs-8","townhall"},

                //Now some liberal journals
                {"https://www.huffingtonpost.com/section/politics", ".card__headlines a", ".headline__title", ""
                        + ".entry__text","huffington"},
                {"https://www.theatlantic.com/most-popular/", ".article.blog-article a", ""
                        + ".article-cover-content", ""
                        + ".article-body","atlantic"},

                {"https://washingtonmonthly.com/politics/",".h2.entry-title a","h1.single-title",""
                        + ".entry-content.cf",
                        "washington"},
                {"https://washingtonmonthly.com/politics/page/2/",".h2.entry-title a","h1.single-title",""
                        + ".entry-content.cf",
                        "washington"},

                {"https://www.nytimes.com/section/politics", "a.story-link", ""
                        + "h1#headline.headline", ""
                        + ".story-body-text.story-content","nytimes"},

        };
        //Now extract the texts

        for(String[] item : urlAndCss) {
            System.out.println(item[4]);
            DownloadDocuments d1 = new DownloadDocuments();
            d1.Download(item);
        }


        // Let's use Jaccard Set to find out similar articles
        float maxDev = 0.23f;
        JaccardSet j1 = new JaccardSet(JaccardSet.SimilarityMethod.SHINGLE_SET,maxDev);
        Set<String> stop = new HashSet<>(Arrays.asList("a","the","an","as","to"));
        TextAnalyzer ana1 = new TextAnalyzer(Version.LATEST,stop);

        //Now compare the texts
        //first open the files and read the text

        File folder = new File("/home/obaidur/IdeaProjects/altometer/articles");
        File[] listOfFiles = folder.listFiles();
        int TotNbrOfPairs = 5000;
        // We need to cluster the articles that have the same topic. In order to do this
        // let's define a table with TotNbrOfTopics rows and 3 columns. The first two
        // columns have the article numbers of the pairs that are similar and the third
        // column stores the topic number.
        int[][] matchTable = new int[TotNbrOfPairs][3];
        int topicNumber = 0;
        int nbrOfPairs = 0;
        for (int i=1; i < listOfFiles.length; i++) {
            Boolean newTopicFound = Boolean.FALSE;
            for (int j=i+1; j < listOfFiles.length; j++) {

                // Check similarity only when the articles belong to different journals

                String[] parts1 = listOfFiles[i].getName().split("-");
                String[] parts2 = listOfFiles[j].getName().split("-");
                String sourceName1 = parts1[0];
                String sourceName2 = parts2[0];


                String str1 = FileUtils.readFileToString(listOfFiles[i]);
                String str2 = FileUtils.readFileToString(listOfFiles[j]);

                Boolean fromDiffJournals = !( new String(sourceName1).equals(sourceName2));

                if ((fromDiffJournals)&&(j1.areSimilar(str1,str2,ana1,maxDev))) {
                    //System.out.println("Journals compared =" + sourceName1 + " and " + sourceName2);
                    newTopicFound = Boolean.TRUE;
                    nbrOfPairs++;
                    topicNumber++;
                    for (int t=1 ; t < TotNbrOfPairs; t++) {
                        //System.out.println("t = " + t + " " + matchTable[t][0] + " " + matchTable[t][1] + " " +
                        //        matchTable[t][2]);
                        // Check from the table if this topic already exists
                        if ((i == matchTable[t][0])||(i == matchTable[t][1])||(j == matchTable[t][0])||(j ==
                                matchTable[t][1])) {

                            topicNumber = matchTable[t][2];
                        }
                    }

                    // Record the match in the table

                    matchTable[nbrOfPairs][0] = i;
                    matchTable[nbrOfPairs][1] = j;
                    matchTable[nbrOfPairs][2] = topicNumber;


                    //System.out.println("---- i = " + i + " j = " + j);
                    //System.out.println(listOfFiles[i].getName());
                    //System.out.println(listOfFiles[j].getName());
                    String directory = "./topics/" + topicNumber + "/";
                    new File(directory).mkdirs();
                    String fileName1 = directory + listOfFiles[i].getName();
                    String fileName2 = directory + listOfFiles[j].getName();
                    BufferedWriter writer1 = new BufferedWriter(new FileWriter(fileName1));
                    BufferedWriter writer2 = new BufferedWriter(new FileWriter(fileName2));
                    writer1.append(str1);
                    writer2.append(str2);
                    writer1.close();
                    writer2.close();


                }
            }
        }

        // Now let's build a predictive model for liberal/conservative classification

        File folder = new File("/home/obaidur/IdeaProjects/altometer/topics/1/");
        File[] listOfFiles = folder.listFiles();

        for (int i=1; i < listOfFiles.length; i++) {
            String str = FileUtils.readFileToString(listOfFiles[i]);

            DocSentenceExtractor docex = new DocSentenceExtractor(str);
            List<String> allSent = docex.allSentences();

            for (String sentence : allSent){
                //System.out.println(sent);
/*
                TaggerOutput output = CoseerTagger.getStaticInstance().getOutputWithNounPhrases(sentence,
                        CoseerTaggerFlags.DEFAULT_TAGGER_FLAGS);
                List<ValuePair<String, Integer>> tokens = output.nounTokenize();
                System.out.println(tokens);
                */
                List<String> phrases = new ArrayList<>();
                try {
                    TaggerOutput output = CoseerTagger.getStaticInstance()
                            .getOutputWithNounPhrases(sentence, CoseerTaggerFlags.DEFAULT_TAGGER_FLAGS);
                    for (ValuePair<Integer, Integer> token : output.getDistinctNounPhraseIndex()) {
                        phrases.add(output
                                .getOriginalPhrase(output.getTokens(), token.getKey(), token.getValue()));
                    }
                    phrases.forEach(p -> {
                        //System.out.println(p);
                        //Assert.assertTrue(sentence.contains(p));
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println("---");
            }

            //String cleanstr = CoseerTagger.cleanSentence(str);
            //List<String> token = CoseerTagger.tokenizeWithMinLengthTokens(cleanstr, 10);
            //System.out.println(token);
            System.out.println("#######################");


        }


        }

}
