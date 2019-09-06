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
        next.click = b -> {
            this.selectedIndex++;
            if (this.selectedIndex >= this.elements.size()) {
                this.selectedIndex = 0;
            }
        };
        prior.click = b -> {
            this.selectedIndex--;
            if (this.selectedIndex < 0) {
                this.selectedIndex = this.elements.size() - 1;
            }
        };
        this.next = next;
        this.prior = prior;
        this.elements = new ArrayList<>();
        for (int i = 0; i < elements.size(); ++i) {
            this.elements.add(elements.get(i));
        }
    }

    public void render(final SpriteBatch spriteBatch) {
        this.prior.render(spriteBatch);
        this.next.render(spriteBatch);
        this.elements.get(this.selectedIndex).render(spriteBatch);
    }

    public void update() {
        this.prior.update();
        this.next.update();
        this.elements.get(this.selectedIndex).update();
    }

    public int renderLayer() {
        return 1;
    }

    public int updateOrder() {
        return 1;
    }

}
