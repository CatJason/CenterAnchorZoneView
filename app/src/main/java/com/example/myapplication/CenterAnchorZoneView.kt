package com.example.myapplication

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.math.roundToInt

class CenterAnchorZoneView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val sizeInPixels: Int
    private var hasRepositionView = false

    init {
        // 将dp转换为像素，这里将大小设为100dp
        val scale = context.resources.displayMetrics.density
        sizeInPixels = (100 * scale).roundToInt()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(sizeInPixels, sizeInPixels)
    }

    private fun repositionView() {
        if (hasRepositionView) return
        hasRepositionView = true
        val screenHeight = context.resources.displayMetrics.heightPixels
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenCenterX = screenWidth / 2
        val screenCenterY = screenHeight / 2

        // 获取父布局在屏幕上的位置
        val parentViewGroup = (parent as? View) ?: return
        val rect = Rect()
        val isVisible = parentViewGroup.getGlobalVisibleRect(rect)
        if (!isVisible) return

        val parentLeft = rect.left
        val parentTop = rect.top
        val parentRight = rect.right
        val parentBottom = rect.bottom

        val halfSize = sizeInPixels / 2
        val parentCenterY = (parentBottom - parentTop) / 2

        val y = if ((screenCenterY - halfSize) < parentTop) {
            parentCenterY - halfSize
        } else if ((screenCenterY + halfSize) > parentBottom) {
            parentCenterY - halfSize
        } else {
            screenCenterY - parentTop - halfSize
        }

        val parentCenterX = (parentRight - parentLeft) / 2
        val x = if ((screenCenterX + halfSize) > parentRight) {
            parentCenterX - halfSize
        } else if ((screenCenterX - halfSize) < parentLeft) {
            parentCenterX - halfSize
        } else {
            screenCenterX - parentLeft - halfSize
        }

        layout(
            x,
            y,
            x + sizeInPixels,
            y + sizeInPixels
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            repositionView()
        }
    }
}