package com.lowheatpizza.kitchen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.lowheatpizza.utils.AssetPaths;

public class KitchenLayoutLoader {
    public KitchenLayout load() {
        FileHandle handle = Gdx.files.internal(AssetPaths.KITCHEN_LAYOUT);
        if (!handle.exists()) {
            throw new IllegalStateException("Missing kitchen layout: " + AssetPaths.KITCHEN_LAYOUT);
        }
        return new Json().fromJson(KitchenLayout.class, handle);
    }
}

