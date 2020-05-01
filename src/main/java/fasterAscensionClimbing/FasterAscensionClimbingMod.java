package fasterAscensionClimbing;

import basemod.*;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import fasterAscensionClimbing.uielements.ImageButton;
import fasterAscensionClimbing.uielements.SelectCharacterPagination;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpireInitializer
public class FasterAscensionClimbingMod implements PostInitializeSubscriber, EditStringsSubscriber {

    //Logger
    public static final Logger logger = LogManager.getLogger(FasterAscensionClimbingMod.class.getName());

    //Mod metadata
    private static final String MOD_NAME = "Faster Ascension Unlocking";
    private static final String AUTHOR = "JohnnyBazooka89";
    private static final String DESCRIPTION = "";

    //Badge
    private static final String BADGE_IMG = "fasterAscensionClimbing/img/ModBadge.png";

    //Localization strings
    private static final String UI_STRINGS_PATH = "fasterAscensionClimbing/localization/%s/UIStrings.json";

    //Mod constants
    public static final String ASCENSION_CLIMBING_VALUE_PREF_KEY = "ascensionClimbingValue";
    public static final int ASCENSION_CLIMBING_DEFAULT_VALUE = 1;

    //Mod prefs
    private static Prefs modPrefs;

    //Settings
    public static final String UI_SETTINGS_ID = "fasterAscensionClimbing:Settings";

    //UI elements in mod settings
    public static ModSlider amountOfAscensionsClimbedOnWinSlider;
    public static ModSlider setAscensionsSlider;
    public static SelectCharacterPagination selectCharacterPagination;
    public static int setAscensionsValue = 20;

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

        UIStrings uiSettingsStrings = CardCrawlGame.languagePack.getUIString(UI_SETTINGS_ID);

        amountOfAscensionsClimbedOnWinSlider = new ModSlider(uiSettingsStrings.TEXT[0], 525.0f, 725.0f, 19.0f, "", settingsPanel, me -> {
            int valueToSave = 1 + Math.round((me.value * me.multiplier));
            modPrefs.putInteger(ASCENSION_CLIMBING_VALUE_PREF_KEY, valueToSave);
            modPrefs.flush();
        });

        amountOfAscensionsClimbedOnWinSlider.setValue((getAscensionClimbingValue() - 1.0f) / 19.0f);

        List<ModLabel> charactersLabels = new ArrayList<>();
        addCharacterLabel(charactersLabels, settingsPanel, uiSettingsStrings.TEXT[1]);
        addCharacterLabel(charactersLabels, settingsPanel, uiSettingsStrings.TEXT[2]);

        ArrayList<AbstractPlayer> allCharacters = CardCrawlGame.characterManager.getAllCharacters();

        for (AbstractPlayer character : allCharacters) {
            addCharacterLabel(charactersLabels, settingsPanel, character.getLocalizedCharacterName());
        }

        selectCharacterPagination = new SelectCharacterPagination(
                new ImageButton("fasterAscensionClimbing/img/RightArrow.png", 775, 585, b -> {
                }),
                new ImageButton("fasterAscensionClimbing/img/LeftArrow.png", 375, 585, b -> {
                }),
                charactersLabels);

        setAscensionsSlider = new ModSlider(uiSettingsStrings.TEXT[3], 975.0f, 600, 19.0f, "", settingsPanel, me ->
                setAscensionsValue = 1 + Math.round((me.value * me.multiplier))
        );

        setAscensionsSlider.setValue(1);

        ModButton setAscensionsButton = new ModButton(1275.0f, 535, settingsPanel, me -> {
            CardCrawlGame.sound.play("UNLOCK_PING");

            int selectedIndex = selectCharacterPagination.selectedIndex;

            if (selectedIndex == 0) {
                for (AbstractPlayer character : allCharacters) {
                    setAscensionForCharacter(character);
                }
            } else if (selectedIndex == 1) {
                List<AbstractPlayer> moddedCharacters = allCharacters.stream().filter(character -> !BaseMod.isBaseGameCharacter(character)).collect(Collectors.toList());
                for (AbstractPlayer character : moddedCharacters) {
                    setAscensionForCharacter(character);
                }
            } else {
                setAscensionForCharacter(allCharacters.get(selectedIndex - 2));
            }
        });

        settingsPanel.addUIElement(amountOfAscensionsClimbedOnWinSlider);
        settingsPanel.addUIElement(selectCharacterPagination);
        settingsPanel.addUIElement(setAscensionsSlider);
        settingsPanel.addUIElement(setAscensionsButton);

        BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, settingsPanel);

    }

    private void setAscensionForCharacter(AbstractPlayer character) {
        CharStat charStat = character.getCharStat();
        Prefs pref = (Prefs) ReflectionHacks.getPrivate(charStat, CharStat.class, "pref");
        if (pref.getInteger("WIN_COUNT", 0) == 0) {
            ReflectionHacks.setPrivate(charStat, CharStat.class, "numVictory", 1);
            pref.putInteger("WIN_COUNT", 1);
        }
        pref.putInteger("ASCENSION_LEVEL", setAscensionsValue);
        pref.putInteger("LAST_ASCENSION_LEVEL", setAscensionsValue);
        pref.flush();
    }

    private void addCharacterLabel(List<ModLabel> charactersLabels, ModPanel settingsPanel, String label) {
        charactersLabels.add(new ModLabel(label, 425.0f, 590, settingsPanel, me -> {
        }));
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Begin editing strings");

        //UI Strings
        BaseMod.loadCustomStringsFile(UIStrings.class, String.format(UI_STRINGS_PATH, getLanguageFolder()));

        logger.info("Done editing strings");
    }

    public static int getAscensionClimbingValue() {
        return modPrefs.getInteger(ASCENSION_CLIMBING_VALUE_PREF_KEY, ASCENSION_CLIMBING_DEFAULT_VALUE);
    }

    public String getLanguageFolder() {
        switch (Settings.language) {
            case ZHS:
                return "zhs";
            default:
                return "eng";
        }
    }

}
