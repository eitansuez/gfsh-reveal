#!/usr/bin/env groovy

import groovy.text.SimpleTemplateEngine

if (args.length != 1) {
  println "Usage: invoke with a single argument (file to be processed)"
  return
}

def filename = args[0]

def file = new File(filename)
if (!file.exists()) {
  println "file ${filename} does not exist."
  return
}

def outputFilename = "${filename}.html"
def outputFile = new File(outputFilename)
if (outputFile.exists()) {
  println "won't overwrite existing file ${outputFilename}"
  return
}

println "ok, we're good! let's have a look at $filename"

boolean isCommand(line) {
  line.startsWith('$') || line.startsWith('gfsh>')
}

String processLine(line) {
  line.replaceAll(~/</, "&lt;")
}

def extractSlideInfo(lines) {
  def slides = []
  def cmd = null, output = ""
  lines.each { line ->
    if (isCommand(line)) {
      if (cmd != null) {
        slides << [cmd: cmd, output: output]
      }
      cmd = line
      output = ""
    } else {
      output += processLine(line) + "\n"
    }
  }
  slides << [cmd: cmd, output: output]
  slides
}


void makeSlides(slideData, File outputFile) {
  def engine = new SimpleTemplateEngine()
  def templateFilename = "reveal-template.html"
  def reader = new FileReader(new File(templateFilename))
  def writeable = engine.createTemplate(reader).make([slides: slideData])
  writeable.writeTo new FileWriter(outputFile)
}

def lines = file.readLines()
def slideData = extractSlideInfo(lines)
println "slides length is: ${slideData.size()}"
makeSlides(slideData, outputFile)

