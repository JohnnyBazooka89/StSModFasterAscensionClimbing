package fasterAscensionClimbing;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.ModSlider;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@SpireInitializer
public class FasterAscensionClimbingMod implements PostInitializeSubscriber, EditStringsSubscriber {

    public static final Logger logger = LogManager.getLogger(FasterAscensionClimbingMod.class.getName());

    //Mod metadata
    private static final String MOD_NAME = "Faster Ascension Unlocking";
    private static final String AUTHOR = "JohnnyBazooka89";
    private static final String DESCRIPTION = "";

    //Badge
    private static final String BADGE_IMG = "fasterAscensionClimbing/img/ModBadge.png";

    //Localization strings
    private static final String UI_STRINGS_PATH = "fasterAscensionClimbing/localization/UIStrings.json";
    public static final String ASCENSION_CLIMBING_VALUE_PREF_KEY = "ascensionClimbingValue";
    public static final int ASCENSION_CLIMBING_DEFAULT_VALUE = 5;

    public static ModSlider slider;

    private static Prefs modPrefs;
    private static List<Runnable> onSelectionChanged = new ArrayList<>();

    public FasterAscensionClimbingMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        logger.info("================= FASTER ASCENSION CLIMBING INIT ===================");

        new FasterAscensionClimbingMod();

        logger.info("====================================================================");
    }

    @Override
    public void receivePostInitialize() {

        modPrefs = SaveHelper.getPrefs("FasterAscensionClimbingPrefs");
        if (!modPrefs.data.containsKey(ASCENSION_CLIMBING_VALUE_PREF_KEY)) {
            modPrefs.putInteger(ASCENSION_CLIMBING_VALUE_PREF_KEY, ASCENSION_CLIMBING_DEFAULT_VALUE);
            modPrefs.flush();
        }

        // Mod badge
        Texture badgeTexture = ImageMaster.loadImage(BADGE_IMG);
        ModPanel settingsPanel = new ModPanel();

        slider = new ModSlider("Amount of \nascensions \nclimbed on win", 525.0f, 725.0f, 19.0f, "", settingsPanel, (me) -> {
            int valueToSave = 1 + Math.round((me.value * me.multiplier));
            modPrefs.putInteger(ASCENSION_CLIMBING_VALUE_PREF_KEY, valueToSave);
            modPrefs.flush();
        });

        slider.setValue((getAscensionClimbingValue() - 1.0f) / 19.0f);

        settingsPanel.addUIElement(slider);

        BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, settingsPanel);

    }

    @Override
    public void receiveEditStrings() {
        logger.info("Begin editing strings");

        //UI Strings
        BaseMod.loadCustomStringsFile(UIStrings.class, UI_STRINGS_PATH);

        logger.info("Done editing strings");
    }

    public static int getAscensionClimbingValue(){
        return modPrefs.getInteger(ASCENSION_CLIMBING_VALUE_PREF_KEY, ASCENSION_CLIMBING_DEFAULT_VALUE);
    }

}
