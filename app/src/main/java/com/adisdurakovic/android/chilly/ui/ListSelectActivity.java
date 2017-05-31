/*
 * Copyright (c) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adisdurakovic.android.chilly.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidanceStylist.Guidance;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.Toast;

import com.adisdurakovic.android.chilly.R;
import com.adisdurakovic.android.chilly.data.Chilly;
import com.adisdurakovic.android.chilly.data.ListElem;
import com.adisdurakovic.android.chilly.data.StreamGrabber;
import com.adisdurakovic.android.chilly.model.Video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Activity that showcases different aspects of GuidedStepFragments.
 */
public class ListSelectActivity extends Activity implements ListResponse {

    private static String displayList;

    private static String[] MOVIE_LISTS = {
            "Trending",
            "Popular",
            "Box Office",
            "Collection",
            "Watchlist"
    };

    private static List<ListElem> lists = new ArrayList<>();

    private static final String[] MOVIE_LISTS_INTENTVARS = {
            "movies_trending",
            "movies_popular",
            "movies_boxoffice",
            "Here's another thing you can do",
            "Here's one more thing you can do"
    };


    private static final int[] OPTION_DRAWABLES = {R.drawable.ic_guidedstep_option_a,
            R.drawable.ic_guidedstep_option_b, R.drawable.ic_guidedstep_option_c};
    private static final boolean[] OPTION_CHECKED = {true, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            ListElem elem = (ListElem) getIntent().getParcelableExtra("listElem");
            new ListTask(getApplicationContext(), this, elem).execute();
        }



    }



    private static void addAction(List<GuidedAction> actions, long id, String title, String desc) {
        actions.add(new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(desc)
                .build());
    }

    @Override
    public void onListGrab(List<ListElem> output) {
        lists = output;
        GuidedStepFragment.addAsRoot(this, new FirstStepFragment(), android.R.id.content);
    }



    public static class FirstStepFragment extends GuidedStepFragment {


        @Override
        public int onProvideTheme() {
            return R.style.Theme_Example_Leanback_GuidedStep_First;
        }

        @Override
        @NonNull
        public Guidance onCreateGuidance(@NonNull Bundle savedInstanceState) {
            String title = getString(R.string.movies_browse);
            String breadcrumb = "";
            String description = getString(R.string.guidedstep_first_description);
            Drawable icon = getActivity().getDrawable(R.drawable.ic_main_icon);
            return new Guidance(title, description, breadcrumb, icon);
        }

        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

//            for (int i = 0; i < MOVIE_LISTS.length; i++) {
//                addAction(actions, i, MOVIE_LISTS[i], "");
//            }
            long x = 0;
            for(Iterator<ListElem> i = lists.iterator(); i.hasNext();) {
                ListElem elem = i.next();
                addAction(actions, x, elem.title, "");
                x++;
            }

        }



        @Override
        public void onGuidedActionClicked(GuidedAction action) {

            Intent intent = new Intent(getActivity(), VerticalGridActivity.class);
            int intid = (int) action.getId();
            intent.putExtra("listElem", lists.get(intid));
            Bundle bundle =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                            .toBundle();
            startActivity(intent, bundle);

        }
    }


}

interface ListResponse {
    void onListGrab(List<ListElem> output);
}


class ListTask extends AsyncTask<String, String, String> {

    ListElem item;
    ListResponse delegate;
    List<ListElem> list = new ArrayList<ListElem>();
    Chilly chilly;

    public ListTask(Context context, ListResponse del, ListElem item) {
        this.item = item;
        this.delegate = del;
        chilly = new Chilly(context);
    }

    // Before starting background thread Show Progress Dialog
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // Checking login in background
    protected String doInBackground(String... params) {



        try {
            switch(item.slug) {
                case "genres":
                    list = chilly.getGenres(item.videoType);
                    break;
                case "trakt-public-list":
                    list = chilly.getPublicList(item.videoType);
                    break;
            }

        } catch (Exception e) {

        }
        return "";

    }

    // After completing background task Dismiss the progress dialog
    protected void onPostExecute(String somestring) {
        delegate.onListGrab(list);
    }
}
