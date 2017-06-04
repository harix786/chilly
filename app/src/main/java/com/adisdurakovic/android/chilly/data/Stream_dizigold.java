package com.adisdurakovic.android.chilly.data;

import com.adisdurakovic.android.chilly.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by add on 22/05/2017.
 */

public class Stream_dizigold extends StreamProvider {


    @Override
    public List<StreamSource> getStreamSources(Video video) throws IOException {


        List<StreamSource> list = new ArrayList<>();

        String title = video.episodeShow.title;

        String videotitle = title.toLowerCase().replaceAll("[^a-z0-9A-Z ]", "").replace(" ", "-");
        String search_url_episode = "http://www.dizigold.org/" + videotitle + "/" + video.seasonNumber + "-sezon/" + video.episodeNumber + "-bolum";

//        String search_title = movie.getTitle().replace(" ", "-").toLowerCase();

//        String url = base_url + "/movie/search/" + search_title;
        System.out.println(search_url_episode);
        request = new Request.Builder().url(search_url_episode).build();

        Pattern pattern = Pattern.compile("var view_id=\"([0-9]*)\"");
//        Pattern pattern = Pattern.compile("(https:.*?redirector.*?)[\\'\\\"]");
        Matcher matcher = pattern.matcher(client.newCall(request).execute().body().string());

        String view_id = "";

        while(matcher.find()) {
            view_id = matcher.group().replace("var view_id=", "").replace("\"", "");
            if(!view_id.equals("")) break;
        }

        String player_url = "http://player.dizigold.org/?id=" + view_id + "&s=1&dil=or";

        request = new Request.Builder().url(player_url).build();


//        HttpURLConnection urlConnection_1 = (HttpURLConnection) new java.net.URL(search_url_episode).openConnection();
//        urlConnection_1.setRequestMethod("GET");
//
//        String movie_list = HTTPGrabber.getContentFromURL(urlConnection_1);
//        Document doc = Jsoup.parse(movie_list);
//        Element link_1 = doc.select("a.ml-mask").last();
//
//        if(link_1 != null) {
//            player_url = "https://123movieshd.tv" + link_1.attr("href") + "/watching.html";
//        }
//
//
//        HttpURLConnection urlConnection_2 = (HttpURLConnection) new java.net.URL(player_url).openConnection();
//        urlConnection_2.setRequestMethod("GET");
//
//        String player_info = HTTPGrabber.getContentFromURL(urlConnection_2);
//
//        Document doc2 = Jsoup.parse(player_info);
//        Elements episode_links = doc2.select("div.les-content a");
//
//        Element link_2 = null;
//
//        for(Element episode_link : episode_links) {
//            if(episode_link.attr("episode-data").equals(video.episodeNumber)) {
//                link_2 = episode_link;
//                break;
//            }
//        }
//
//        String link_url = "";
//
//        if(link_2 != null) {
//            link_url = link_2.attr("player-data");
//        }
//
//        System.out.println(link_url);
//
//        HttpURLConnection urlConnection_3 = (HttpURLConnection) new java.net.URL(link_url).openConnection();
//        urlConnection_3.setRequestMethod("GET");
//
//        String stream_detail = HTTPGrabber.getContentFromURL(urlConnection_3);


//        Pattern pattern = Pattern.compile("sources:.*?file.*?[\\]]");
////        Pattern pattern = Pattern.compile("(https:.*?redirector.*?)[\\'\\\"]");
//        Matcher matcher = pattern.matcher(stream_detail);
//
//
//        while(matcher.find()) {
//            String jsonString = "{" + matcher.group().replace("sources:", "'sources':").replace("file:", "'file':").replace("label:", "'label':") + "}";
//            try {
//                JSONObject source_object = new JSONObject(jsonString);
//                JSONArray sources = source_object.getJSONArray("sources");
//
//                for(int i = 0; i < sources.length(); i++) {
//                    JSONObject source = sources.getJSONObject(i);
//                    StreamSource ss = new StreamSource();
//                    ss.quality = source.optString("label").replace(" P", "");
//                    ss.url = source.getString("file");
//                    ss.provider = "123MOVIESHD";
//                    ss.videosource = "GVIDEO";
//                    if(!ss.quality.equals("Auto")) {
//                        list.add(ss);
//                    }
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        return list;
    }


}