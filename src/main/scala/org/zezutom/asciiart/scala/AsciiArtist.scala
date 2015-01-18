package org.zezutom.asciiart.scala

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.{File, FileWriter, IOException, PrintWriter}
import javax.imageio.ImageIO

/**
 * Created by tom on 18/01/2015.
 */
class AsciiArtist(imagePath:String, outPath:String) {

  require(isValidPath(imagePath, false), "Image path is either invalid or the file isn't readable.")
  require(isValidPath(outPath, true), "No write access to the output file.")

  val (red, green, blue) = (0.30D, 0.11D, 0.59D);

  val image:BufferedImage = loadImage()

  val (printWriter, fileWriter) = initWriters()

  val charList = List((240, ' '), (210, '.'), (190, '*'), (170, '+'),
    (120, '^'), (110, '&'), (80, '8'), (60, '#'))

  private def isValidPath(path:String, write:Boolean):Boolean = {
    try {
      val file = new File(path)
      if (write) true
      else file.exists && file.canRead
    } catch {
      case ex:IOException => false
    }
  }

  private def loadImage():BufferedImage = {
      return ImageIO.read(new File(imagePath))
  }

  private def initWriters():(PrintWriter, FileWriter) = {
    val fileWriter = new FileWriter(outPath)
    val printWriter = new PrintWriter(fileWriter)
    (printWriter, fileWriter)
  }

  private def px2Char(px:Double):Char = {
    charList.collectFirst({case x if px >= x._1 => x._2}).getOrElse('@')
  }

  private def printAndFlush(option:Option[Char]) {
    try {
      option match {
        case None => printWriter.println()
        case _ => printWriter.print(option.get)
      }
      printWriter.flush()
      fileWriter.flush()
    } catch {
      case ex:IOException => println("Couldn't write to the out file.")
    }
  }

  def toAscii() {
    val (y, x) = (image.getHeight - 1, image.getWidth - 1)
    for (i <- 0 to y) {
      for (j <- 0 to x) {
        //println(j + "x" + i)
        val pxColor = new Color(image.getRGB(j, i));
        val pxValue = (((pxColor.getRed() * red) + (pxColor.getGreen() * green) + (pxColor.getBlue() * blue)));
        printAndFlush(Option(px2Char(pxValue)))
      }
      printAndFlush(None:Option[Char])
    }
  }
}
