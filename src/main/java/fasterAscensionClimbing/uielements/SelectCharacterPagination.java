package fasterAscensionClimbing.uielements;

import basemod.*;
import com.badlogic.gdx.graphics.g2d.*;

import java.util.*;

public class SelectCharacterPagination implements IUIElement {
    private ImageButton next;
    private ImageButton prior;
    private List<ModLabel> elements;
    public int selectedIndex;

    public SelectCharacterPagination(final ImageButton next, final ImageButton prior, final List<ModLabel> elements) {
        this.selectedIndex = 0;
        next.click = (b -> ++this.selectedIndex);
        prior.click = (b -> --this.selectedIndex);
        this.next = next;
        this.prior = prior;
        this.elements = new ArrayList<>();
        for (int i = 0; i < elements.size(); ++i) {
            this.elements.add(elements.get(i));
        }
    }

    public void render(final SpriteBatch spriteBatch) {
        if (this.selectedIndex != 0) {
            this.prior.render(spriteBatch);
        }
        if ((this.selectedIndex + 1) < this.elements.size()) {
            this.next.render(spriteBatch);
        }
        for (final ModLabel element : this.elements.subList(this.selectedIndex, Math.min((this.selectedIndex + 1), this.elements.size()))) {
            element.render(spriteBatch);
        }
    }

    public void update() {
        if (this.selectedIndex != 0) {
            this.prior.update();
        }
        if ((this.selectedIndex + 1) < this.elements.size()) {
            this.next.update();
        }
        for (final ModLabel element : this.elements.subList(this.selectedIndex, Math.min((this.selectedIndex + 1), this.elements.size()))) {
            element.update();
        }
    }

    public int renderLayer() {
        return 1;
    }

    public int updateOrder() {
        return 1;
    }

}
