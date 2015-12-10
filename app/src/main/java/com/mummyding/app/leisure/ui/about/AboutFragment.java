/*
 *  Copyright (C) 2015 MummyDing
 *
 *  This file is part of Leisure( <https://github.com/MummyDing/Leisure> )
 *
 *  Leisure is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *                             ｀
 *  Leisure is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Leisure.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mummyding.app.leisure.ui.about;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;

import com.mummyding.app.leisure.R;
import com.mummyding.app.leisure.support.CONSTANT;
import com.mummyding.app.leisure.support.HttpUtil;
import com.mummyding.app.leisure.support.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by mummyding on 15-12-10.
 * GitHub: https://github.com/MummyDing/
 * Blog: http://blog.csdn.net/mummyding
 */
public class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    private Preference mCheckUpdate;
    private Preference mStarProject;
    private Preference mShare;
    private Preference mBlog;
    private Preference mGitHub;
    private Preference mEmail;


    private final String CHECK_UPDATE = "check_update";
    private final String START_PROJECT = "star_project";
    private final String SHARE = "share";
    private final String BLOG = "blog";
    private final String GITHUB = "github";
    private final String EMAIL = "email";


    private ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);


        mCheckUpdate = findPreference(CHECK_UPDATE);
        mStarProject = findPreference(START_PROJECT);
        mShare = findPreference(SHARE);
        mBlog = findPreference(BLOG);
        mGitHub = findPreference(GITHUB);
        mEmail = findPreference(EMAIL);

        mCheckUpdate.setOnPreferenceClickListener(this);
        mStarProject.setOnPreferenceClickListener(this);
        mShare.setOnPreferenceClickListener(this);
        mBlog.setOnPreferenceClickListener(this);
        mGitHub.setOnPreferenceClickListener(this);
        mEmail.setOnPreferenceClickListener(this);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(mCheckUpdate == preference){
            progressBar.setVisibility(View.VISIBLE);

            Request.Builder builder = new Request.Builder();
            builder.url(CONSTANT.VERSION_URL);
            Request request = builder.build();
            HttpUtil.enqueue(request, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Snackbar.make(getView(),"Fail to get version info,please check your network setting",Snackbar.LENGTH_SHORT).show();
                    handle.sendEmptyMessage(1);
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    String latestVersion = response.body().string();
                    if(CONSTANT.CURRENT_VERSION.equals(latestVersion.trim())){
                        Snackbar.make(getView(), getString(R.string.notify_current_is_latest),Snackbar.LENGTH_SHORT).show();
                    }else {
                        Snackbar.make(getView(), getString(R.string.notify_find_new_version)+latestVersion,Snackbar.LENGTH_SHORT).show();
                    }
                    handle.sendEmptyMessage(1);
                }
            });

        }else if(mStarProject == preference){
            Utils.copyToClipboard(getView(), getString(R.string.project_url));
        }else if(mShare == preference){
            Utils.copyToClipboard(getView(), getString(R.string.text_share_info));
        }else if(mBlog == preference){
            Utils.copyToClipboard(getView(),getString(R.string.author_blog));
        }else if(mGitHub == preference){
            Utils.copyToClipboard(getView(),getString(R.string.author_github));
        }else if(mEmail == preference){
            Utils.copyToClipboard(getView(),getString(R.string.author_email));
        }
        return false;
    }

    private Handler handle = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            progressBar.setVisibility(View.GONE);
            return false;
        }
    });
}