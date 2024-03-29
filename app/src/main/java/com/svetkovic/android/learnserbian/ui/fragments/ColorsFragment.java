package com.svetkovic.android.learnserbian.ui.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import com.svetkovic.android.learnserbian.R;
import com.svetkovic.android.learnserbian.ui.main.Word;
import com.svetkovic.android.learnserbian.ui.main.WordAdapter;

import java.util.ArrayList;

public class ColorsFragment extends Fragment {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    public ColorsFragment() {}

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange (int focusChange){
                    if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    }else if (focusChange == AudioManager.AUDIOFOCUS_GAIN){
                        mMediaPlayer.start();
                    }else if (focusChange == AudioManager.AUDIOFOCUS_LOSS){
                        releaseMediaPlayer();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener = mp -> releaseMediaPlayer();

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("black", "crna", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "bela", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("yellow", "žuta", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        words.add(new Word("green", "zelena", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("red", "crvena", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("blue", "plava", R.drawable.color_blue, R.raw.color_blue));
        words.add(new Word("gray", "siva", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("orange", "narandžasta", R.drawable.color_orange, R.raw.color_orange));
        words.add(new Word("pink", "roza", R.drawable.color_pink, R.raw.color_pink));
        words.add(new Word("brown", "smeđa", R.drawable.color_brown, R.raw.color_brown));


        WordAdapter adapter = new WordAdapter(getActivity(), words);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            releaseMediaPlayer();

            Word word = words.get(position);

            int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseMediaPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
