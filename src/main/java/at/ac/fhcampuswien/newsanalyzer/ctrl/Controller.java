package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    public static final String APIKEY = "a8ad5f613fba4100904597e3ca1ae331";  //TODO add your api key

    public void process(String query, Category category) {
        System.out.println("Start process");

        //TODO implement Error handling

        NewsApi newsApi = new NewsApiBuilder()
                .setApiKey(APIKEY)
                .setQ(query)
                .setEndPoint(Endpoint.TOP_HEADLINES)
                .setSourceCountry(Country.at)
                .setSourceCategory(category)
                .createNewsApi();

        NewsResponse newsResponse = newsApi.getNews();
        if (newsResponse != null) {
            List<Article> articles = newsResponse.getArticles();
            System.out.println("Anzahl an Artikeln: " + articles.size());

            articles.stream().forEach(article -> System.out.println(article.getSource().getName()));

            System.out.println("Provider: "+ articles.stream().collect(Collectors.groupingBy(article -> article.getSource().getName(),Collectors.counting()))
                    .entrySet().stream().max(Comparator.comparingInt(t-> t.getValue().intValue())).get().getKey());

            System.out.println("Shortest author: " + articles.stream().filter(article -> Objects.nonNull(article.getAuthor()))
                    .min(Comparator.comparingInt(article -> article.getAuthor().length()))
                    .get().getAuthor());
        }

        System.out.println("End process");
    }


    public Object getData() {

        return null;
    }
}
