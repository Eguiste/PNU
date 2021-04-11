package com.example.listview.structure

import android.R
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_logo.*


class SlidingTabLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) : HorizontalScrollView(context, attrs, defStyle)
{
    /**
     Позволяет полностью контролировать цвета, нарисованные в макете вкладки.
     Можно установить с помощью [.setCustomTabColorizer].
     */
    interface TabColorizer {
        fun getIndicatorColor(position: Int): Int
    }

    private val mTitleOffset: Int
    private var mTabViewLayoutId = 0
    private var mTabViewTextViewId = 0
    private var mDistributeEvenly = false
    private var mViewPager: ViewPager? = null
    private val mContentDescriptions = SparseArray<String?>()
    private var mViewPagerPageChangeListener: ViewPager.OnPageChangeListener? = null
    private val mTabStrip: SlidingTabStrip

    /**
     * Можно установить кастомный [TabColorizer], который будет использоваться.
     *
     * Если нужна простоя кастомизация, можно использовать [.setSelectedIndicatorColors] для достижения аналогичных эффектов
     */
    fun setCustomTabColorizer(tabColorizer: TabColorizer?) {
        mTabStrip.setCustomTabColorizer(tabColorizer)
    }

    fun setDistributeEvenly(distributeEvenly: Boolean) {
        mDistributeEvenly = distributeEvenly
    }

    /**
     * Устанавливает цвета, которые будут использоваться для обозначения выбранной вкладки. Эти цвета рассматриваются как
     * круговой массив. Предоставление одного цвета будет означать, что все вкладки обозначены одинаковым цветом.
     */

    fun setSelectedIndicatorColors(vararg colors: Int) {
        mTabStrip.setSelectedIndicatorColors(R.color.white)
    }

    /**
     * Установим [ViewPager.OnPageChangeListener]. При использвании [SlidingTabLayout]
     * должен быть установлен [ViewPager.OnPageChangeListener] с помощью этого метода.
     * Это нужно, чтобы макет мог правильно обновлять своб позицию прокрутки
     *
     * @see ViewPager.setOnPageChangeListener
     */
    fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener?) {
        mViewPagerPageChangeListener = listener
    }

    /**
     * Устанавливая кастомный layout, который будет найден для tab views.
     *
     * @параметр layoutResId Layout id, для его нахождения
     * @параметр textViewId id of the [TextView]
     */
    fun setCustomTabView(layoutResId: Int, textViewId: Int) {
        mTabViewLayoutId = layoutResId
        mTabViewTextViewId = textViewId
    }

    /**
     * Устанавливает связанный ViewPager. Предполагается, что содержимое ViewPager
     * (число владок и их заголовков) не изменяется после выполнения этого вызова.
     */
    fun setViewPager(viewPager: ViewPager?) {
        mTabStrip.removeAllViews()
        mViewPager = viewPager
        if (viewPager != null)
        {
            viewPager.setOnPageChangeListener(InternalViewPagerListener())
            populateTabStrip()
        }
    }

    /**
     * Создание дефолтного вида, который будет использоваться для вкладок.
     * Это вызывается, если кастомный вид вкладок не установлен через [.setCustomTabView].
     */
    private fun createDefaultTabView(context: Context): TextView {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP.toFloat())
        textView.typeface =  ResourcesCompat.getFont(context, com.example.listview.R.font.circularstdbook)
        textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val outValue = TypedValue()
        //getContext().theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)

        textView.setBackgroundResource(outValue.resourceId)
        val padding = (TAB_VIEW_PADDING_DIPS * resources.displayMetrics.density).toInt()
        textView.setPadding(padding, padding, padding, padding)
        return textView
    }

    private fun populateTabStrip() {
        val adapter = mViewPager!!.adapter
        val tabClickListener: OnClickListener = TabClickListener()
        for (i in 0 until adapter!!.count) {
            var tabView: View? = null
            var tabTitleView: TextView? = null
            if (mTabViewLayoutId != 0) {
                // Если установлен кастомный id у tab view layout, попробовать найти его
                tabView = LayoutInflater.from(context).inflate(mTabViewLayoutId, mTabStrip, false)
                tabTitleView = tabView.findViewById<View>(mTabViewTextViewId) as TextView
            }
            if (tabView == null) {
                tabView = createDefaultTabView(context)
            }
            if (tabTitleView == null && TextView::class.java.isInstance(tabView)) {
                tabTitleView = tabView as TextView
            }
            if (mDistributeEvenly) {
                val lp = tabView.layoutParams as LinearLayout.LayoutParams
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
                lp.weight = 1f
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            tabTitleView!!.text = adapter.getPageTitle(i)
            tabView.setOnClickListener(tabClickListener)
            val desc = mContentDescriptions[i, null]
            if (desc != null) {
                tabView.contentDescription = desc
            }
            mTabStrip.addView(tabView)
            if (i == mViewPager!!.currentItem) {
                tabView.isSelected = true
            }
            tabTitleView.setTextColor(resources.getColorStateList(R.color.white))
            tabTitleView.textSize = 18f
        }
    }

    fun setContentDescription(i: Int, desc: String?) {
        mContentDescriptions.put(i, desc)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mViewPager != null) {
            scrollToTab(mViewPager!!.currentItem, 0)
        }
    }

    private fun scrollToTab(tabIndex: Int, positionOffset: Int) {
        val tabStripChildCount: Int = mTabStrip.childCount
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return
        }
        val selectedChild: View = mTabStrip.getChildAt(tabIndex)
        var targetScrollX = selectedChild.left + positionOffset
        if (tabIndex > 0 || positionOffset > 0) {
            // If we're not at the first child and are mid-scroll, make sure we obey the offset
            targetScrollX -= mTitleOffset
        }
        scrollTo(targetScrollX, 0)
    }

    private inner class InternalViewPagerListener : ViewPager.OnPageChangeListener {
        private var mScrollState = 0
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            val tabStripChildCount: Int = mTabStrip.childCount
            if (tabStripChildCount == 0 || position < 0 || position >= tabStripChildCount) {
                return
            }
            mTabStrip.onViewPagerPageChanged(position, positionOffset)
            val selectedTitle: View = mTabStrip.getChildAt(position)
            val extraOffset = (positionOffset * selectedTitle.width).toInt()
            scrollToTab(position, extraOffset)
            mViewPagerPageChangeListener?.onPageScrolled(
                position, positionOffset,
                positionOffsetPixels
            )
        }

        override fun onPageScrollStateChanged(state: Int) {
            mScrollState = state
            mViewPagerPageChangeListener?.onPageScrollStateChanged(state)
        }

        override fun onPageSelected(position: Int) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f)
                scrollToTab(position, 0)
            }
            for (i in 0 until mTabStrip.childCount) {
                mTabStrip.getChildAt(i).isSelected = position == i
            }
            mViewPagerPageChangeListener?.onPageSelected(position)
        }
    }

    private inner class TabClickListener : OnClickListener {
        override fun onClick(v: View) {
            for (i in 0 until mTabStrip.childCount) {
                if (v === mTabStrip.getChildAt(i)) {
                    mViewPager?.currentItem = i
                    return
                }
            }
        }
    }

    companion object {
        private const val TITLE_OFFSET_DIPS = 24
        private const val TAB_VIEW_PADDING_DIPS = 16
        private const val TAB_VIEW_TEXT_SIZE_SP = 18
    }

    init {

        // Отключить ScrollBar
        isHorizontalScrollBarEnabled = false
        // Проверка заполненности Tab Strips этим View
        isFillViewport = true
        mTitleOffset = (TITLE_OFFSET_DIPS * resources.displayMetrics.density).toInt()
        mTabStrip = SlidingTabStrip(context!!)
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }
}