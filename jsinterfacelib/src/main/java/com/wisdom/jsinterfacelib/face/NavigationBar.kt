package com.wisdom.jsinterfacelib.face

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.wisdom.jsinterfacelib.R

import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by Tsing on 2020/7/14
 */
class NavigationBar(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private val iv_back: ImageView

    private val tv_left: TextView
    private val iv_right: ImageView
    private val tv_title: TextView
    private val tv_right: TextView
    private val line: View

    @JvmField
    val rl_root: RelativeLayout
    fun setNoPaddingTopStatusBarHeight() {
        rl_root.setPadding(0, 0, 0, 0)
    }

    fun hideTvRight() {
        tv_right.visibility = GONE
    }

    fun setTvRight(string: String?): View {
        tv_right.visibility = VISIBLE
        tv_right.text = string
        return tv_right
    }

    fun setTvRight(string: Int): View {
        tv_right.visibility = VISIBLE
        tv_right.text = context.getString(string)
        return tv_right
    }

    fun setTvRightOnClick(listener: OnClickListener?): View {
        tv_right.visibility = VISIBLE
        tv_right.setOnClickListener(listener)
        return tv_right
    }

    fun setTvRight(text: String?, resId: Int): View {
        tv_right.visibility = VISIBLE
        tv_right.text = text
        tv_right.setTextColor(Color.parseColor("#333333"))
        val drawable = resources.getDrawable(resId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tv_right.setCompoundDrawables(drawable, null, null, null)
        tv_right.compoundDrawablePadding = ConvertUtils.dp2px(8f)
        return tv_right
    }

    fun setTvRight(text: String?, resId: Int, textSize: Int): View {
        tv_right.visibility = VISIBLE
        tv_right.textSize = textSize.toFloat()
        tv_right.text = text
        tv_right.setTextColor(Color.parseColor("#333333"))
        val drawable = resources.getDrawable(resId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tv_right.setCompoundDrawables(drawable, null, null, null)
        tv_right.compoundDrawablePadding = ConvertUtils.dp2px(8f)
        return tv_right
    }
    fun setTvRight(text: String?, resId: Int, textSize: Int,textColor:String): View {
        tv_right.visibility = VISIBLE
        tv_right.textSize = textSize.toFloat()
        tv_right.text = text
        tv_right.setTextColor(Color.parseColor(textColor))
        val drawable = resources.getDrawable(resId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tv_right.setCompoundDrawables(drawable, null, null, null)
        tv_right.compoundDrawablePadding = ConvertUtils.dp2px(8f)
        return tv_right
    }


    fun setRightImage(resId: Int): View {
        iv_right.visibility = VISIBLE
        iv_right.setImageResource(resId)
        return iv_right
    }

    fun hideRightImage() {
        iv_right.visibility = GONE
    }

    fun setRightImageOnClick(listener: OnClickListener?) {
        iv_right.setOnClickListener(listener)
    }

    /**
     * 隐藏布局下边界的白色线
     */
    fun hideLine() {
        line.visibility = GONE
    }

    fun setTvLeft(text: String?, resId: Int): View {
        tv_left.visibility = VISIBLE
        tv_left.text = text
        val drawable = resources.getDrawable(resId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tv_left.setCompoundDrawables(drawable, null, null, null)
        tv_left.compoundDrawablePadding = ConvertUtils.dp2px(8f)
        return tv_left
    }

    private fun hideTvLeft() {
        tv_left.visibility = GONE
    }

    fun showBackImage(resId: Int): View {
        iv_back.visibility = VISIBLE
        iv_back.setImageResource(resId)
        return iv_back
    }

    fun showBackImage(): View {
        iv_back.visibility = VISIBLE
        iv_back.setImageResource(R.drawable.back)
        return iv_back
    }

    fun hideBackImage() {
        iv_back.visibility = GONE
    }





    fun setTitle(title: String?): View {
        tv_title.visibility = VISIBLE
        tv_title.text = title
        return tv_title
    }
    fun setTitle(title: String?,textColor:String): View {
        tv_title.visibility = VISIBLE
        tv_title.text = title
        tv_title.setTextColor(Color.parseColor(textColor))
        return tv_title
    }

    fun setTitleColor(color: Int) {
        tv_title.setTextColor(resources.getColor(color))
    }

    fun setTitle(title: Int) {
        tv_title.visibility = VISIBLE
        tv_title.text = context.resources.getString(title)
    }



    /**
     * @param title 标题
     * @param color 标题文字颜色
     */
    fun setTitle(title: Int, color: String?) {
        tv_title.visibility = VISIBLE
        tv_title.setTextColor(Color.parseColor(color))
        tv_title.text = context.resources.getString(title)
    }

    /**
     * @param title       标题
     * @param color       标题文字颜色
     * @param rootBgColor 背景颜色
     */
    fun setTitle(title: Int, color: String?, rootBgColor: String?) {
        tv_title.visibility = VISIBLE
        tv_title.setTextColor(Color.parseColor(color))
        rl_root.setBackgroundColor(Color.parseColor(rootBgColor))
        tv_title.text = context.resources.getString(title)
    }

    /**
     * @param title       标题
     * @param color       标题文字颜色
     * @param rootBgColor 背景颜色
     */
    fun setTitle(title: Int, color: Int, rootBgColor: Int) {
        tv_title.visibility = VISIBLE
        tv_title.setTextColor(resources.getColor(color))
        rl_root.setBackgroundColor(resources.getColor(rootBgColor))
        tv_title.text = resources.getString(title)
    }





    /**
     *  @describe 设置背景
     *  @return
     *  @author HanXueFeng
     *  @time 2021/7/1 0001  14:48
     */
    fun setBgColor(color:String){
        rl_root.setBackgroundColor(Color.parseColor(color))
    }



    init {
        val view = LayoutInflater.from(context).inflate(R.layout.navigation_bar_layout, this)
        val rl = view.findViewById<RelativeLayout>(R.id.rl)
        rl_root = view.findViewById(R.id.rl_root)
        rl_root.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0)
        val layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(45f))
        rl.layoutParams = layoutParams
        iv_back = view.findViewById(R.id.iv_back)
        iv_back.setOnClickListener { view1: View? -> (getContext() as AppCompatActivity).finish() }
        tv_left = view.findViewById(R.id.tv_left)
        iv_right = view.findViewById(R.id.iv_right)
        tv_title = view.findViewById(R.id.tv_title)
        line = view.findViewById(R.id.line)
        tv_right = view.findViewById(R.id.tv_right)

    }




}