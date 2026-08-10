package com.echoes.echoes.ui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;

public class IconFactory {

    private IconFactory() {
    }

    public static FontAwesomeIconView create(FontAwesomeIcon icon) {

        FontAwesomeIconView view = new FontAwesomeIconView(icon);

        view.setGlyphSize(16);
        view.setFill(Color.web("#C9D1F5"));

        return view;
    }
}