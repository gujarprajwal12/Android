package com.psg.android.presentaion.dymaic

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.hypot

class PageCurlView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
    }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var pages: List<Bitmap> = emptyList()
    private var currentPage = 0

    private var touchX = 0f
    private var touchY = 0f
    private var isCurling = false

    fun setPages(bitmaps: List<Bitmap>) {
        pages = bitmaps
        currentPage = 0
        invalidate()
    }

    fun nextPage() {
        if (currentPage < pages.size - 1) {
            currentPage++
            invalidate()
        }
    }

    fun previousPage() {
        if (currentPage > 0) {
            currentPage--
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (pages.isEmpty()) return true

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isCurling = true
                touchX = event.x
                touchY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                touchX = event.x
                touchY = event.y
                invalidate()
            }
//
//            MotionEvent.ACTION_UP -> {
//                isCurling = false
//
//                if (touchX < width / 3 && currentPage < pages.size - 1) {
//                    nextPage()
//                } else if (touchX > width * 2 / 3 && currentPage > 0) {
//                    previousPage()
//                }
//
//                touchX = width.toFloat()
//                touchY = height.toFloat()
//                invalidate()
//            }
            MotionEvent.ACTION_UP -> {
                isCurling = false

                if (touchX < width / 3 && currentPage < pages.size - 1) {
                    currentPage++
                }

                touchX = width.toFloat() - 10
                touchY = height.toFloat() - 10

                invalidate()
            }

        }
        return true
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        if (pages.isEmpty()) return
//
//        canvas.drawBitmap(
//            pages[currentPage],
//            null,
//            Rect(0, 0, width, height),
//            null
//        )
//
//        if (isCurling) drawCurl(canvas)
//    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (pages.isEmpty()) return

        // Draw NEXT page first (under front page)
        if (currentPage < pages.size - 1) {
            canvas.drawBitmap(
                pages[currentPage + 1],
                null,
                Rect(0, 0, width, height),
                null
            )
        }

        // Draw current page on top
        canvas.drawBitmap(
            pages[currentPage],
            null,
            Rect(0, 0, width, height),
            null
        )

        if (isCurling) drawCurl(canvas)
    }


    private fun drawCurl(canvas: Canvas) {

        val curlX = touchX.coerceIn(0f, width.toFloat())
        val foldFactor = (width - curlX) / width
        val foldWidth = foldFactor * width / 2f

        val frontRect = RectF(0f, 0f, curlX, height.toFloat())
        val backRect = RectF(curlX, 0f, width.toFloat(), height.toFloat())

        canvas.save()
        canvas.clipRect(frontRect)
        canvas.drawBitmap(pages[currentPage], null, Rect(0, 0, width, height), null)
        canvas.restore()

        if (currentPage < pages.size - 1) {
            canvas.save()
            canvas.clipRect(backRect)

            val matrix = Matrix().apply {
                setScale(-1f, 1f, curlX, height / 2f)
            }

            canvas.drawBitmap(pages[currentPage], matrix, backPaint)
            canvas.restore()
        }

        val gradient = LinearGradient(
            curlX - foldWidth, 0f,
            curlX, 0f,
            Color.BLACK, Color.TRANSPARENT,
            Shader.TileMode.CLAMP
        )
        shadowPaint.shader = gradient

        canvas.drawRect(
            curlX - foldWidth, 0f,
            curlX, height.toFloat(),
            shadowPaint
        )
    }
}
