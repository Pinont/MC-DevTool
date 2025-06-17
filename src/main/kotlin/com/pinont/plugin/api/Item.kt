package com.pinont.plugin.api

import com.pinont.lib.api.creator.items.ItemCreator

class Item {

    var itemList: MutableList<ItemCreator> = ArrayList()

    fun register(vararg item: ItemCreator) {
        itemList.addAll(item)
    }

}