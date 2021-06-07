package at.ac.fhcampuswien.newsapi;

import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.util.List;

public class NewsAPIExample {

    public static final String APIKEY = "a8ad5f613fba4100904597e3ca1ae331";    //TODO add your api key

    public static void main(String[] args) {
        try {
        NewsApi newsApi = new NewsApiBuilder()
                .setApiKey(APIKEY)
                .setQ("corona")
                .setEndPoint(Endpoint.TOP_HEADLINES)// example of how to use enums
                .setSourceCountry(Country.at)       // example of how to use enums
                .setSourceCategory(Category.health) // example of how to use enums
                .createNewsApi();

        NewsResponse newsResponse = newsApi.getNews();
        if (newsResponse != null) {
            List<Article> articles = newsResponse.getArticles();
            articles.stream().forEach(article -> System.out.println(article.toString()));
        }

        newsApi = new NewsApiBuilder()
                .setApiKey(APIKEY)
                .setQ("corona")
                .setEndPoint(Endpoint.EVERYTHING)
                .setFrom("2020-03-20")
                .setExcludeDomains("Lifehacker.com")
                .createNewsApi();

        newsResponse = newsApi.getNews();


            if (newsResponse != null) {
                List<Article> articles = newsResponse.getArticles();
                articles.stream().forEach(article -> System.out.println(article.toString()));
            }

        } catch (Exception e) {
            System.out.println("ERROR");
        }

    }
}
