package com.example.listview.structure

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout

internal class SlidingTabStrip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    LinearLayout(context, attrs) {
    private val mBottomBorderThickness: Int
    private val mBottomBorderPaint: Paint
    private val mSelectedIndicatorThickness: Int
    private val mSelectedIndicatorPaint: Paint
    private val mDefaultBottomBorderColor: Int
    private var mSelectedPosition = 0
    private var mSelectionOffset = 0f
    private var mCustomTabColorizer: SlidingTabLayout.TabColorizer? = null
    private val mDefaultTabColorizer: SimpleTabColorizer
    fun setCustomTabColorizer(customTabColorizer: SlidingTabLayout.TabColorizer?) {
        mCustomTabColorizer = customTabColorizer
        invalidate()
    }

    fun setSelectedIndicatorColors(vararg colors: Int) {
        // Проверяем, что кастомный колоризатор удален
        mCustomTabColorizer = null
        mDefaultTabColorizer.setIndicatorColors(*colors)
        invalidate()
    }

    fun onViewPagerPageChanged(position: Int, positionOffset: Float) {
        mSelectedPosition = position
        mSelectionOffset = positionOffset
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val height = height - 30
        val childCount = childCount
        val tabColorizer: SlidingTabLayout.TabColorizer = mCustomTabColorizer ?: mDefaultTabColorizer

        // Толстое цветное подчеркивание ниже текущего выбора
        if (childCount > 0) {
            val selectedTitle = getChildAt(mSelectedPosition)
            var left = selectedTitle.left + 160
            var right = selectedTitle.right - 160
            var color: Int = tabColorizer.getIndicatorColor(mSelectedPosition)
            if (mSelectionOffset > 0f && mSelectedPosition < getChildCount() - 1) {
                val nextColor: Int = tabColorizer.getIndicatorColor(mSelectedPosition + 1)
                if (color != nextColor)
                    color = blendColors(nextColor, color, mSelectionOffset)


                // Отрисовка выделения между вкладками
                val nextTitle = getChildAt(mSelectedPosition + 1)
                left = (mSelectionOffset * (nextTitle.left + 160) + (1.0f - mSelectionOffset) * left).toInt()
                right = (mSelectionOffset * (nextTitle.right - 160) + (1.0f - mSelectionOffset) * right).toInt()
            }
            mSelectedIndicatorPaint.color = color
            canvas.drawRect(
                left.toFloat(), height - mSelectedIndicatorThickness.toFloat(), right.toFloat(),
                height.toFloat(), mSelectedIndicatorPaint
            )
        }

        // Тонкое подчеркивание по всему нижнему краю
        canvas.drawRect(0f, height - mBottomBorderThickness.toFloat(), width.toFloat(), height.toFloat(), mBottomBorderPaint)
    }

    private class SimpleTabColorizer : SlidingTabLayout.TabColorizer {
        private lateinit var mIndicatorColors: IntArray
        override fun getIndicatorColor(position: Int): Int {
            return mIndicatorColors[position % mIndicatorColors.size]
        }

        fun setIndicatorColors(vararg colors: Int) {
            mIndicatorColors = colors
        }
    }

    companion object {
        private const val DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0
        private const val DEFAULT_BOTTOM_BORDER_COLOR_ALPHA: Byte = 0x26
        private const val SELECTED_INDICATOR_THICKNESS_DIPS = 2
        private const val DEFAULT_SELECTED_INDICATOR_COLOR = -0xcc4a1b

        /**
         * Устанавливает альфа-значение "color" как заданное значение "alpha"
         */
        private fun setColorAlpha(color: Int, alpha: Byte): Int {
            return Color.argb(alpha.toInt(), Color.red(color), Color.green(color), Color.blue(color))
        }

        /**
         * Смешивает `color1` и `color2`, использую заданное соотношение
         *
         * @param ratio - соотношение с которым нужно смшать. 1.0 вернет `color1`, 0.5 смшает в равной пропорции,
         * 0.0 вернет `color2`.
         */
        private fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
            val inverseRation = 1f - ratio
            val r = Color.red(color1) * ratio + Color.red(color2) * inverseRation
            val g = Color.green(color1) * ratio + Color.green(color2) * inverseRation
            val b = Color.blue(color1) * ratio + Color.blue(color2) * inverseRation
            return Color.rgb(r.toInt(), g.toInt(), b.toInt())
        }
    }

    init {
        setWillNotDraw(false)
        val density = resources.displayMetrics.density
        val outValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorForeground, outValue, true)
        val themeForegroundColor = outValue.data
        mDefaultBottomBorderColor = setColorAlpha(themeForegroundColor, DEFAULT_BOTTOM_BORDER_COLOR_ALPHA)
        mDefaultTabColorizer = SimpleTabColorizer()
        mDefaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR)
        mBottomBorderThickness = (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density).toInt()
        mBottomBorderPaint = Paint()
        mBottomBorderPaint.color = mDefaultBottomBorderColor
        mSelectedIndicatorThickness = (SELECTED_INDICATOR_THICKNESS_DIPS * density).toInt()
        mSelectedIndicatorPaint = Paint()
    }
}