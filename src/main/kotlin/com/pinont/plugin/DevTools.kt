package com.pinont.plugin

import com.pinont.lib.plugin.CorePlugin
import com.pinont.plugin.commands.Base

class DevTools : CorePlugin() {

    override fun onReload() {
    }

    override fun onPluginStart() {
        sendConsoleMessage("Starting DevTools Plugin...")
        registerCommand(Base())
        sendConsoleMessage("Registered DevTools Command")
    }

    override fun onPluginStop() {
    }
}
