package com.wolin.warehouseapp.utils.common;

import android.content.res.Resources;

import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.utils.model.Shop;

import java.util.ArrayList;

public class ShopLoader {

    public static ArrayList<Shop> loadShop(Resources resources) {
        ArrayList<Shop> shops = new ArrayList<>();
        shops.add(new Shop("Dowolny", resources.getResourceEntryName(R.drawable.noneshop)));
        shops.add(new Shop("Auchan", resources.getResourceEntryName(R.drawable.auchan)));
        shops.add(new Shop("Biedronka", resources.getResourceEntryName(R.drawable.biedronka)));
        shops.add(new Shop("Carrefour", resources.getResourceEntryName(R.drawable.carrefour)));
        shops.add(new Shop("Delikatesy-Centrum", resources.getResourceEntryName(R.drawable.delikatesy)));
        shops.add(new Shop("Dino", resources.getResourceEntryName(R.drawable.dino)));
        shops.add(new Shop("Kaufland", resources.getResourceEntryName(R.drawable.kaufland)));
        shops.add(new Shop("Lewiatan", resources.getResourceEntryName(R.drawable.lewiatan)));
        shops.add(new Shop("Lidl", resources.getResourceEntryName(R.drawable.lidl)));
        shops.add(new Shop("Top Market", resources.getResourceEntryName(R.drawable.topmarket)));
        shops.add(new Shop("Żabka", resources.getResourceEntryName(R.drawable.zabka)));

        return shops;
    }
}
