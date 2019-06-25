package fasterAscensionClimbing.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AscensionLevelUpTextEffect;
import fasterAscensionClimbing.FasterAscensionClimbingMod;

@SpirePatch(clz = AscensionLevelUpTextEffect.class, method = SpirePatch.CONSTRUCTOR)
public class AscensionLevelUpTextEffectPatch {

    public static void Postfix(AscensionLevelUpTextEffect ascensionLevelUpTextEffect) {
        int newLevel = AbstractDungeon.ascensionLevel + FasterAscensionClimbingMod.getAscensionClimbingValue();
        if (newLevel > 20) {
            newLevel = 20;
        }
        ReflectionHacks.setPrivate(ascensionLevelUpTextEffect, AscensionLevelUpTextEffect.class, "level", newLevel);
    }

}
