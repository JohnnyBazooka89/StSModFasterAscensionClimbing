package fasterAscensionClimbing.patches;

import basemod.ModSlider;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import fasterAscensionClimbing.FasterAscensionClimbingMod;
import javassist.CtBehavior;

@SpirePatch(clz = ModSlider.class, method = "render")
public class SliderRenderPatch {

    @SpireInsertPatch(locator = Locator.class, localvars = "renderVal")
    public static void Insert(ModSlider modSlider, SpriteBatch sb, @ByRef String[] renderVal) {
        if (modSlider == FasterAscensionClimbingMod.amountOfAscensionsClimbedOnWinSlider || modSlider == FasterAscensionClimbingMod.setAscensionsSlider) {
            renderVal[0] = Integer.toString(Integer.valueOf(renderVal[0]) + 1);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior method) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(ModSlider.class, "sliderGrabbed");
            return LineFinder.findInOrder(method, matcher);
        }
    }

}
