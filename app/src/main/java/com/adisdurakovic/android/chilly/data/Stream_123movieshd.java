package com.adisdurakovic.android.chilly.data;

import android.graphics.Movie;

import com.adisdurakovic.android.chilly.model.Video;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by add on 22/05/2017.
 */

public class Stream_123movieshd {


    public static String getMovieStreamURL(Video video) throws IOException {

        String stream_url = "";
        String videotitle = video.title.toLowerCase().replaceAll("[^a-z0-9A-Z ]", "").replace(" ", "-");
        String search_url_movie = "https://123movieshd.tv/movie/search/" + videotitle;

//        String search_title = movie.getTitle().replace(" ", "-").toLowerCase();

//        String url = base_url + "/movie/search/" + search_title;

        HttpURLConnection urlConnection_1 = (HttpURLConnection) new java.net.URL(search_url_movie).openConnection();
        urlConnection_1.setRequestMethod("GET");

        String movie_list = HTTPGrabber.getContentFromURL(urlConnection_1);
        Document doc = Jsoup.parse(movie_list);
        Element source = doc.select("a.ml-mask").first();

        String player_url = "https://123movieshd.tv" + source.attr("href") + "/watching.html";


        HttpURLConnection urlConnection_2 = (HttpURLConnection) new java.net.URL(player_url).openConnection();
        urlConnection_2.setRequestMethod("GET");

        String player_info = HTTPGrabber.getContentFromURL(urlConnection_2);

        Document doc2 = Jsoup.parse(player_info);
        Element link = doc2.select("div.les-content a").first();
        String link_url = link.attr("player-data");


        HttpURLConnection urlConnection_3 = (HttpURLConnection) new java.net.URL(link_url).openConnection();
        urlConnection_3.setRequestMethod("GET");

        String stream_detail = HTTPGrabber.getContentFromURL(urlConnection_3);


        Pattern pattern = Pattern.compile("(https:.*?redirector.*?)[\\'\\\"]");
        Matcher matcher = pattern.matcher(stream_detail);

        while(matcher.find()) {
//            System.out.println(matcher.group());
            stream_url = matcher.group().replace("'","");
        }

        return stream_url;


    }


}