import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel

class Mandelbrot : JPanel() {
    private val MAX_ITER = 1000
    private val WIDTH = 800
    private val HEIGHT = 800
    private var zoom = 150.0
    private var offsetX = 0.0
    private var offsetY = 0.0
    private var image: BufferedImage? = null

    init {
        createImage()
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                super.mouseClicked(e)
                val clickX = e!!.x
                val clickY = e.y
                val newZoom = if (e.button == MouseEvent.BUTTON1) zoom * 0.9 else zoom / 0.9
                val offsetXRatio = (clickX - WIDTH / 2) / zoom
                val offsetYRatio = (clickY - HEIGHT / 2) / zoom
                offsetX -= offsetXRatio * (newZoom - zoom)
                offsetY -= offsetYRatio * (newZoom - zoom)
                zoom = newZoom
                createImage()
                repaint()
            }
        })
        addMouseWheelListener(object : MouseAdapter() {
            override fun mouseWheelMoved(e: MouseWheelEvent?) {
                super.mouseWheelMoved(e)
                val notches = e!!.wheelRotation
                val newZoom = if (notches < 0) zoom * 0.9 else zoom / 0.9
                val clickX = e.x
                val clickY = e.y
                val offsetXRatio = (clickX - WIDTH / 2) / zoom
                val offsetYRatio = (clickY - HEIGHT / 2) / zoom
                offsetX -= offsetXRatio * (newZoom - zoom)
                offsetY -= offsetYRatio * (newZoom - zoom)
                zoom = newZoom
                createImage()
                repaint()
            }
        })
    }

    private fun createImage() {
        image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
        for (y in 0 until HEIGHT) {
            for (x in 0 until WIDTH) {
                var zx = 0.0
                var zy = 0.0
                var cX = (x - WIDTH / 2 + offsetX) / zoom
                var cY = (y - HEIGHT / 2 + offsetY) / zoom
                var i = MAX_ITER
                while (zx * zx + zy * zy < 4 && i > 0) {
                    val tmp = zx * zx - zy * zy + cX
                    zy = 2.0 * zx * zy + cY
                    zx = tmp
                    i--
                }
                val color = if (i == 0) Color.BLACK else Color(i % 256, (i / 2) % 256, (i / 3) % 256)
                image!!.setRGB(x, y, color.rgb)
            }
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.drawImage(image, 0, 0, null)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val frame = JFrame("Mandelbrot Set")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.setSize(WIDTH, HEIGHT)
            frame.add(Mandelbrot())
            frame.isVisible = true
        }
    }
}
