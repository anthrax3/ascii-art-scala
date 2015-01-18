package org.zezutom.asciiart.scala

/**
 * Runs the app.
 */
object AsciiApp extends App {
  new AsciiArtist("src/main/resources/html.png", "html.txt").toAscii()
}
