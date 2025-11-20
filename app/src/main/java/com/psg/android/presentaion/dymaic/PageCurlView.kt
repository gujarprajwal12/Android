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

    private val frontPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
    }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Paths
    private val frontPath = Path()
    private val foldPath = Path()
    private val backPath = Path()

    // Page list
    private var pages: List<Bitmap> = emptyList()
    private var currentPage = 0

    // Touch
    private var touchX = 0f
    private var touchY = 0f
    private var isCurling = false

    fun setPages(bitmaps: List<Bitmap>) {
        pages = bitmaps
        currentPage = 0
        invalidate()
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

            MotionEvent.ACTION_UP -> {
                isCurling = false

                // go to next page if drag is big
                if (touchX < width / 2 && currentPage < pages.size - 1) {
                    currentPage++
                }

                touchX = width.toFloat()
                touchY = height.toFloat()

                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (pages.isEmpty()) return

        // Draw current page
        canvas.drawBitmap(
            pages[currentPage],
            null,
            Rect(0, 0, width, height),
            null
        )

        if (isCurling) drawCurl(canvas)
    }

    private fun drawCurl(canvas: Canvas) {
        val dx = width - touchX
        val dy = height - touchY
        val radius = hypot(dx, dy) / 2

        // FRONT PATH
        frontPath.reset()
        frontPath.addRect(0f, 0f, touchX, height.toFloat(), Path.Direction.CW)

        // DRAW front remaining bitmap
        canvas.save()
        canvas.clipPath(frontPath)
        canvas.drawBitmap(pages[currentPage], null, Rect(0, 0, width, height), null)
        canvas.restore()

        // FOLD PATH
        foldPath.reset()
        foldPath.moveTo(touchX, touchY)
        foldPath.quadTo(
            touchX + radius, touchY - radius,
            width.toFloat(), 0f
        )
        foldPath.lineTo(width.toFloat(), height.toFloat())
        foldPath.close()

        // BACK PAGE (simple color)
        backPath.reset()
        backPath.set(foldPath)

        canvas.save()
        canvas.clipPath(backPath)
        canvas.drawColor(Color.LTGRAY)
        canvas.restore()

        // SHADOW
        shadowPaint.shader = LinearGradient(
            touchX, touchY,
            touchX + radius, touchY,
            Color.BLACK, Color.TRANSPARENT,
            Shader.TileMode.CLAMP
        )

        canvas.drawPath(foldPath, shadowPaint)
    }
}
