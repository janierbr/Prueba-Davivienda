package com.taskapp.nativeui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import kotlin.math.abs

class AvatarView(context: Context) : View(context) {
    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private var initials: String = ""
    private var backgroundColor: Int = Color.GRAY

    init {
        paintText.color = Color.WHITE
        paintText.textAlign = Paint.Align.CENTER
        paintText.textSize = 64f
    }

    fun setName(name: String) {
        this.initials = extractInitials(name)
        this.backgroundColor = generateColor(name)
        paintCircle.color = backgroundColor
        invalidate()
    }

    private fun extractInitials(name: String): String {
        if (name.isEmpty()) return ""
        val parts = name.trim().split("\\s+".toRegex())
        return if (parts.size >= 2) {
            (parts[0].take(1) + parts[1].take(1)).uppercase()
        } else {
            parts[0].take(2).uppercase()
        }
    }

    private fun generateColor(name: String): Int {
        val hash = name.hashCode()
        val r = abs((hash and 0xFF0000) shr 16) % 256
        val g = abs((hash and 0x00FF00) shr 8) % 256
        val b = abs(hash and 0x0000FF) % 256
        return Color.rgb(r, g, b)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY)

        canvas.drawCircle(centerX, centerY, radius, paintCircle)

        val textBounds = Paint()
        paintText.getTextBounds(initials, 0, initials.length, android.graphics.Rect())
        val textHeight = paintText.descent() - paintText.ascent()
        val textOffset = textHeight / 2 - paintText.descent()

        canvas.drawText(initials, centerX, centerY + textOffset, paintText)
    }
}
