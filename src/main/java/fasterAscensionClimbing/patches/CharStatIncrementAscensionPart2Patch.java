package fasterAscensionClimbing.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import fasterAscensionClimbing.FasterAscensionClimbingMod;

@SpirePatch(clz = CharStat.class, method = "incrementAscension")
public class CharStatIncrementAscensionPart2Patch {

    @SpireInsertPatch(rloc = 5, localvars = "derp") /*just before 4th access of local variable derp - not possible with locator (?)*/
    public static void Insert(CharStat charStat, @ByRef int[] derp) {
        Prefs pref = (Prefs) ReflectionHacks.getPrivate(charStat, CharStat.class, "pref");
        int playerCurrentMaxAscension = pref.getInteger("ASCENSION_LEVEL", 1);
        derp[0] = playerCurrentMaxAscension + FasterAscensionClimbingMod.getAscensionClimbingValue();
    }

}
