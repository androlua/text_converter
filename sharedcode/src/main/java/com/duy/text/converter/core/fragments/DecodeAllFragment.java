/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.text.converter.core.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.text.converter.R;
import com.duy.text.converter.core.adapters.DecodeResultAdapter;
import com.duy.text.converter.core.codec.Codec;
import com.duy.text.converter.core.codec.CodecMethod;

import java.util.ArrayList;

/**
 * Created by Duy on 11/13/2017.
 */

public class DecodeAllFragment extends Fragment {
    private static final String KEY_INPUT = "input";
    @Nullable
    private DecodeTask mDecodeTask;
    private ArrayList<Codec> mDecoders;
    private DecodeResultAdapter mDecodeResultAdapter;

    public static DecodeAllFragment newInstance(String input) {

        Bundle args = new Bundle();
        args.putString(KEY_INPUT, input);
        DecodeAllFragment fragment = new DecodeAllFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_decode_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initCodec();

        RecyclerView recyclerView = view.findViewById(R.id.list_decoded);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDecodeResultAdapter = new DecodeResultAdapter(getContext());
        recyclerView.setAdapter(mDecodeResultAdapter);

        String input = getArguments().getString(KEY_INPUT);
        generateResult(input);
    }

    private void generateResult(String input) {
        if (mDecodeTask != null) mDecodeTask.cancel(true);
        mDecodeTask = new DecodeTask();
        mDecodeTask.execute(input);
    }

    @Override
    public void onDestroyView() {
        if (mDecodeTask != null) mDecodeTask.cancel(true);
        super.onDestroyView();
    }

    private void initCodec() {
        mDecoders = new ArrayList<>();
        CodecMethod[] values = CodecMethod.values();
        for (CodecMethod value : values) mDecoders.add(value.getCodec());
    }


    private class DecodeTask extends AsyncTask<String, Object, Void> {
        DecodeTask() {
        }

        @Override
        protected Void doInBackground(String... strings) {
            String input = strings[0];
            for (int i = 0, mDecodersSize = mDecoders.size(); i < mDecodersSize; i++) {
                Codec mDecoder = mDecoders.get(i);
                if (isCancelled()) return null;
                String decode = mDecoder.decode(input);
                publishProgress(decode, i, mDecoder.getName(getContext()));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            String result = (String) values[0];
            int position = (int) values[1];
            String name = (String) values[2];
            if (name == null) {
                name = getContext().getResources().getStringArray(R.array.codec_methods)[position];
            }
            addToRecycleView(result, name);
        }

        private void addToRecycleView(String result, String name) {
            if (isCancelled()) return;
            mDecodeResultAdapter.add(new DecodeResultAdapter.DecodeItem(name, result));
        }
    }
}