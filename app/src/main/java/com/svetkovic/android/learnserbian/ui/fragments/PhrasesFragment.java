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

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    public PhrasesFragment() { }

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

        words.add(new Word("Where are you going?", "Gde ideš?", R.raw.phrase_where_go));
        words.add(new Word("What is your name?", "Kako se zoveš?", R.raw.phrase_name));
        words.add(new Word("My name is...", "Zovem se...", R.raw.phrase_name2));
        words.add(new Word("How are you feeling?", "Kako se osećaš?", R.raw.phrase_feeling));
        words.add(new Word("I'm feeling good.", "Osećam se dobro.", R.raw.phrase_feeling2));
        words.add(new Word("Are you coming?", "Da li dolaziš?", R.raw.phrase_coming));
        words.add(new Word("Yes, I'm coming.", "Da, dolazim.", R.raw.phrase_coming2));
        words.add(new Word("Let's go!", "Idemo!", R.raw.phrase_go));
        words.add(new Word("Come here.", "Dođi ovde.", R.raw.phrase_come));
        words.add(new Word("I'm coming.", "Dolazim.", R.raw.phrase_come2));
        // New 2021
        words.add(new Word("What do you do for a living?", "Čime se baviš?", R.raw.phrase_what_do_for_living));
        words.add(new Word("Where are you from?", "Odakle si poreklom?", R.raw.phrase_where_from));
        words.add(new Word("What are you doing in your free time?", "Šta radiš u slobodno vreme?", R.raw.phrase_free_time));
        words.add(new Word("What is your phone number?", "Koji je tvoj broj telefona?", R.raw.phrase_phone_no));
        words.add(new Word("Do you have Facebook?", "Da li imaš Fejsbuk?", R.raw.phrase_fb));
        words.add(new Word("Sorry, I'm still learning Serbian.", "Izvini, još uvek učim srpski.", R.raw.phrase_learning));
        words.add(new Word("Can you show me around the city?", "Možeš li mi pokazati zanimljivosti grada?", R.raw.phrase_city_tour));
        words.add(new Word("How much does this cost?", "Koliko košta ovo?", R.raw.phrase_price));
        words.add(new Word("Can you help me witch this?", "Možeš li mi pomoći sa ovim?", R.raw.phrase_help));
        words.add(new Word("Where is the nearest store?", "Gde je najbliža prodavnica?", R.raw.phrase_store));
        words.add(new Word("Where is the nearest bank?", "Gde je najbliža banka?", R.raw.phrase_bank));
        words.add(new Word("How can I pay the parking?", "Kako mogu da platim parking?", R.raw.phrase_parking));
        words.add(new Word("How do you call this meal?", "Kako zovete ovo jelo?", R.raw.phrase_meal));
        words.add(new Word("I have many things to do today.", "Danas imam mnogo obaveza.", R.raw.phrase_obligations));

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
