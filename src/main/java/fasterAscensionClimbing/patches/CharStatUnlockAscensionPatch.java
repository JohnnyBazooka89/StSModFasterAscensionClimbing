package fasterAscensionClimbing.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import fasterAscensionClimbing.FasterAscensionClimbingMod;

@SpirePatch(clz = CharStat.class, method = "unlockAscension")
public class CharStatUnlockAscensionPatch {

    public static void Postfix(CharStat charStat) {
        Prefs pref = (Prefs) ReflectionHacks.getPrivate(charStat, CharStat.class, "pref");
        pref.putInteger("ASCENSION_LEVEL", FasterAscensionClimbingMod.getAscensionClimbingValue());
        pref.putInteger("LAST_ASCENSION_LEVEL", FasterAscensionClimbingMod.getAscensionClimbingValue());
        pref.flush();
    }

}
