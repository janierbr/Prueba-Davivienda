package com.taskapp.nativeui

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class AvatarViewManager : SimpleViewManager<AvatarView>() {
    override fun getName(): String {
        return "AvatarView"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): AvatarView {
        return AvatarView(reactContext)
    }

    @ReactProp(name = "name")
    fun setName(view: AvatarView, name: String?) {
        view.setName(name ?: "")
    }
}
