package com.example.wilson.humancharacteristics.model;

import android.content.res.AssetManager;

/**
 * Created by Wilson on 4/24/2018.
 */

public class HumanCharacteristics {
    private HumanCharacteristicAttractiveness attracttiveHuman;
    private HumanCharacteristicCompetent competentHuman;
    private HumanCharacteristicDominant dominantHuman;
    private HumanCharacteristicExtroverted extrovertedHuman;
    private HumanCharacteristicLikeability likeabilityHuman;
    private HumanCharacteristicThread threadHuman;
    private HumanCharacteristicTrustworthy trustworthyHuman;
    public HumanCharacteristics(AssetManager assetManager) {
        trustworthyHuman = new HumanCharacteristicTrustworthy(assetManager);
        attracttiveHuman = new HumanCharacteristicAttractiveness(assetManager);
        dominantHuman = new HumanCharacteristicDominant(assetManager);
        competentHuman = new HumanCharacteristicCompetent(assetManager);
        extrovertedHuman = new HumanCharacteristicExtroverted(assetManager);
        likeabilityHuman = new HumanCharacteristicLikeability(assetManager);
        threadHuman = new HumanCharacteristicThread(assetManager);
    }
    public HumanCharacteristicAttractiveness getAttracttiveHuman() {return  attracttiveHuman;}
    public HumanCharacteristicCompetent getCompetentHuman() {return competentHuman;}
    public HumanCharacteristicDominant getDominantHuman() {return dominantHuman;}
    public HumanCharacteristicExtroverted getExtrovertedHuman() {return extrovertedHuman;}
    public HumanCharacteristicLikeability getLikeabilityHuman() { return likeabilityHuman;}
    public HumanCharacteristicThread getThreadHuman() {return threadHuman;}
    public HumanCharacteristicTrustworthy getTrustworthyHuman() {return trustworthyHuman;}
}
