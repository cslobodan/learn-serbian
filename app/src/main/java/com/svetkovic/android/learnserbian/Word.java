package com.svetkovic.android.learnserbian;

/**
 * {@link Word} represents a vocabulary word that the user wants to learn.
 * It contains an English translation and a Serbian translation for that word.
 */

public class Word {

    //English translation for the word
    private String mEnglishTranslation;

    //Serbian translation for the word
    private String mSerbianTranslation;

    //Image resource ID for the word
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    private static final int NO_IMAGE_PROVIDED = -1;

    private int mAudioResourceId;

    /**
     * Create a new Word object.
     * @param englishTranslation is the word in language that the user is already fimiliar with (English)
     * @param serbianTranslation is the word in Serbian language
     * @param audioResourceId is the sound in Serbian language
     */
    public Word(String englishTranslation, String serbianTranslation, int audioResourceId){
        mEnglishTranslation = englishTranslation;
        mSerbianTranslation = serbianTranslation;
        mAudioResourceId = audioResourceId;
    }

    public Word(String englishTranslation, String serbianTranslation, int imageResourceId, int audioResourceId){
        mEnglishTranslation = englishTranslation;
        mSerbianTranslation = serbianTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    //Get English translation for the word
    public String getEnglishTranslation(){
        return mEnglishTranslation;
    }

    //Get Serbian translation for the word
    public String getSerbianTranslation(){
        return mSerbianTranslation;
    }

    //Return the image resource ID of the word
    public int getImageResourceId(){
        return mImageResourceId;
    }

    //Returns whether or not there is an image for this word
    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    public int getAudioResourceId(){
        return mAudioResourceId;
    }

}
