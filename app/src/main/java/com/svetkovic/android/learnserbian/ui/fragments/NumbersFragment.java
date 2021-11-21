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

public class NumbersFragment extends Fragment {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    public NumbersFragment() { }

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }
                }
            };

    private final MediaPlayer.OnCompletionListener mCompletionListener = mp -> releaseMediaPlayer();

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

        words.add(new Word("one", "jedan", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "dva", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tri", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "četiri", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "pet", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "šest", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "sedam", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "osam", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "devet", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "deset", R.drawable.number_ten, R.raw.number_ten));


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