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

public class FamilyFragment extends Fragment {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    public FamilyFragment() { }

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

        words.add(new Word("father", "otac", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "majka", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "sin", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "ćerka", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "stariji brat", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "mlađi brat", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "starija sestra", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "mlađa sestra", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother", "baka", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "deda", R.drawable.family_grandfather, R.raw.family_grandfather));


        WordAdapter adapter = new WordAdapter(getActivity(), words);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            releaseMediaPlayer();

            Word word = words.get(position);

            int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
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
