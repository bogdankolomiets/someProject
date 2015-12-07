package com.example.root.douclient.fragments;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.douclient.R;

/**
 * Created by root on 04.12.15.
 */
public class ForumFragment extends ListFragment {


    public static ForumFragment getInstance() {
        ForumFragment forumFragment = new ForumFragment();
        Bundle args = new Bundle();
        forumFragment.setArguments(args);

        return forumFragment;

    }

}
