package fasterAscensionClimbing.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import fasterAscensionClimbing.FasterAscensionClimbingMod;
import javassist.CtBehavior;

@SpirePatch(clz = CharStat.class, method = "incrementAscension")
public class CharStatIncrementAscensionPart1Patch {

    @SpireInsertPatch(locator = Derp2ndAccessLocator.class, localvars = "derp")
    public static void Insert(CharStat charStat, @ByRef int[] derp) {
        int playedAscension = AbstractDungeon.ascensionLevel;
        int playerCurrentMaxAscension = derp[0];
        if (playedAscension + FasterAscensionClimbingMod.getAscensionClimbingValue() > playerCurrentMaxAscension) {
            derp[0] = playedAscension;
        }
    }

    private static class Derp2ndAccessLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "ascensionLevel");
            return LineFinder.findInOrder(method, matcher);
        }
    }

}
