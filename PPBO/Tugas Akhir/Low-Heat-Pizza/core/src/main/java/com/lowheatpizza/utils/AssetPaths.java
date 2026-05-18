package com.lowheatpizza.utils;

public final class AssetPaths {
    public static final String PROJECT_ASSETS_ROOT = resolveAssetsRoot();

    private static String resolveAssetsRoot() {
        if (new java.io.File("assest-low-heat-pizza").exists()) {
            return "assest-low-heat-pizza";
        } else if (new java.io.File("../assest-low-heat-pizza").exists()) {
            return "../assest-low-heat-pizza";
        }
        return "assest-low-heat-pizza";
    }

    public static final String CUSTOMER_CONFIG = PROJECT_ASSETS_ROOT + "\\costumer.json";
    public static final String MAIN_MENU_BG = "images/ui/main_menu_bg.png";
    public static final String LOGO = "images/ui/logo_low_heat_pizza.png";
    public static final String SHOP_BG = "images/shop/cashier_background.png";
    public static final String SHOP_FOREGROUND = "images/shop/foreground_counter.png";
    public static final String KITCHEN_BG = "images/kitchen/kitchen_bg.png";
    public static final String KITCHEN_BG_NUMBERED = "images/kitchen/kitchen_bg.png";
    public static final String KITCHEN_BG_CLEAN = "images/kitchen/bg_kitchen_clean.png";
    public static final String KITCHEN_FLOOR_BG = "images/kitchen/floor_checker.png";
    public static final String KITCHEN_ELEMENTS = "images/kitchen/kitchen_elements.png";
    public static final String KITCHEN_STATION_DOUGH = "images/kitchen/station_dough.png";
    public static final String KITCHEN_STATION_SAUCE = "images/kitchen/station_sauce.png";
    public static final String KITCHEN_STATION_CHEESE = "images/kitchen/station_cheese.png";
    public static final String KITCHEN_STATION_PEPPERONI = "images/kitchen/station_pepperoni.png";
    public static final String KITCHEN_PIZZA_BOARD = "images/kitchen/pizza_board.png";
    public static final String KITCHEN_STATION_MUSHROOM_LOCKED = "images/kitchen/station_mushroom_locked.png";
    public static final String KITCHEN_STATION_ONION_LOCKED = "images/kitchen/station_onion_locked.png";
    public static final String KITCHEN_STATION_FISH_LOCKED = "images/kitchen/station_fish_locked.png";
    public static final String KITCHEN_PADLOCK = "images/kitchen/padlock.png";
    public static final String COUNTER = "images/shop/counter.png";
    public static final String DOUGH = "images/kitchen/dough.png";
    public static final String SAUCE_LAYER = "images/kitchen/sauce_layer.png";
    public static final String CHEESE_LAYER = "images/kitchen/cheese_layer.png";
    public static final String TOPPING_PEPPERONI = "images/toppings/pepperoni.png";
    public static final String TOPPING_MUSHROOM = "images/toppings/mushroom.png";
    public static final String TOPPING_ONION = "images/toppings/onion.png";
    public static final String TOPPING_BELL_PEPPER = "images/toppings/bell_pepper.png";
    public static final String TOPPING_SAUSAGE = "images/toppings/sausage.png";
    public static final String CUSTOMER_ONE = "images/shop/customer_1.png";
    public static final String CUSTOMER_TWO = "images/shop/customer_2.png";
    public static final String CUSTOMER_THREE = "images/shop/customer_3.png";
    public static final String UI_MONEY_BAR = "images/ui/money_bar.png";
    public static final String UI_CUSTOMER_TEXT = "images/ui/customer_text.png";
    public static final String UI_ICON_SUN = "images/ui/icon_sun.png";
    public static final String UI_ICON_MONEY = "images/ui/icon_money.png";
    public static final String UI_SPEECH_BUBBLE = "images/ui/speech_bubble.png";
    public static final String UI_BUTTON_RESPONSE = "images/ui/button_response.9.png";
    public static final String UI_HUD_PANEL = "images/ui/hud_panel.9.png";
    public static final String FONT_HANDWRITTEN = "fonts/PatrickHand-Regular.ttf";
    public static final String AUDIO_MAIN_MENU = "audio/main_menu.mp3";
    public static final String AUDIO_GAMEPLAY = "audio/gameplay.mp3";
    public static final String AUDIO_PAGE_SWITCH = "audio/page_switch.wav";
    public static final String AUDIO_CLICK_TOPPING = "audio/click_topping.wav";
    public static final String AUDIO_CLICK_BUTTON = "audio/click_button.wav";
    public static final String AUDIO_OVEN_BAKE = "audio/oven_bake.wav";
    public static final String AUDIO_CUSTOMER_ARRIVE = "audio/customer_arrive.wav";
    public static final String AUDIO_UNLOCK_TOPPING = "audio/unlock_topping.wav";
    public static final String TOPPINGS_DATA = "data/toppings.json";
    public static final String KITCHEN_LAYOUT = "data/kitchen_layout.json";
    public static final String INGREDIENT_CONFIG = "data/ingredient_config.json";

    private AssetPaths() {
    }
}

