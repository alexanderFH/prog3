package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    public static final String APIKEY = "a8ad5f613fba4100904597e3ca1ae331";  //TODO add your api key

    public void process(String query, Category category) {
        System.out.println("Start process");

        //TODO implement Error handling
        NewsApi newsApi;

        if (category == null) {
            newsApi = new NewsApiBuilder()
                    .setApiKey(APIKEY)
                    .setQ(query)
                    .setEndPoint(Endpoint.TOP_HEADLINES)
                    .createNewsApi();
        } else {
            newsApi = new NewsApiBuilder()
                    .setApiKey(APIKEY)
                    .setQ(query)
                    .setEndPoint(Endpoint.TOP_HEADLINES)
                    .setSourceCountry(Country.at)
                    .setSourceCategory(category)
                    .createNewsApi();
        }
        NewsResponse newsResponse = null;

        try {
            newsResponse = newsApi.getNews();
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }

        if (newsResponse != null) {
            List<Article> articles = newsResponse.getArticles();
            System.out.println("Count of articles: " + articles.size());
            if (articles.isEmpty()) {
                System.out.println("No articles to analyze.");
                return;
            }

            String provider = articles.stream().collect(Collectors.groupingBy(article -> article.getSource()
                    .getName(), Collectors.counting())).entrySet().stream().max(Comparator.comparingInt(t -> t.getValue().
                    intValue())).get().getKey();
            if (provider != null)
                System.out.println("Provider: " + provider);

            String author = articles.stream().filter(article -> Objects.nonNull(article.getAuthor()))
                    .min(Comparator.comparingInt(article -> article.getAuthor().length()))
                    .get().getAuthor();
            if (author != null)
                System.out.println("Shortest author: " + author);

            List<Article> sortedArticles = articles.stream().sorted(Comparator.comparingInt(article -> article.getTitle()
                    .length())).sorted(Comparator.comparing(Article::getTitle)).collect(Collectors.toList());

            System.out.println("First article of sorted List: " + sortedArticles.get(0));

            for (Article article : articles) {
                try {
                    URL url = new URL(article.getUrl());
                    InputStream is = url.openStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    BufferedWriter wr =
                            new BufferedWriter(new FileWriter(article.getTitle().substring(0, 10) + ".html"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        wr.write(line);
                    }
                    br.close();
                    wr.close();

                } catch (Exception e) {
                    System.err.println("Failed to save webpage: " + e.getMessage());
                }
            }
        }

        System.out.println("End process");
    }


    public Object getData() {

        return null;
    }
}
