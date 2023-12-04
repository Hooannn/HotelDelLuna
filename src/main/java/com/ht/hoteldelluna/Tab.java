package com.ht.hoteldelluna;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;

public class Tab {
    private ToggleButton toggle;
    private FXMLLoader loader;

    public Tab(ToggleButton toggle, FXMLLoader loader) {
        this.toggle = toggle;
        this.loader = loader;
    }

    public ToggleButton getToggle() {
        return toggle;
    }

    public void setToggle(ToggleButton toggle) {
        this.toggle = toggle;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }
}
