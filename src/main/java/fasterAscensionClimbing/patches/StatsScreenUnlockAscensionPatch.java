package fasterAscensionClimbing.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;
import fasterAscensionClimbing.FasterAscensionClimbingMod;

@SpirePatch(clz = StatsScreen.class, method = "isPlayingHighestAscension")
public class StatsScreenUnlockAscensionPatch {

    public static SpireReturn<Boolean> Prefix(Prefs p) {
        if (AbstractDungeon.ascensionLevel + FasterAscensionClimbingMod.getAscensionClimbingValue() > p.getInteger("ASCENSION_LEVEL", 1))
            return SpireReturn.Return(true);
        return SpireReturn.Continue();
    }

}
