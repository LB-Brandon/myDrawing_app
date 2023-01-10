package com.branden.kidsdrawingapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


//    메인 엑티비티에 띄울 뷰를 위한 클래스 (액티비티가 아님)
//    무언가를 그리려면 뷰를 사용해야 함
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var canvas: Canvas? = null

    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK

    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {

        mDrawPaint = Paint()
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND

        mDrawPath = CustomPath(color, mBrushSize)
        mBrushSize = 20.toFloat()

        mCanvasPaint = Paint(Paint.DITHER_FLAG)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    // Change Canvas to Canvas? if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                if (touchX != null) {
                    mDrawPath!!.moveTo(touchX, touchY!!)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    mDrawPath!!.lineTo(touchX, touchY!!)
                }
            }
            MotionEvent.ACTION_UP -> {
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }

        invalidate()

        return true
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {

    }


}