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

def lines = file.readLines()

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

println "slides length is: ${slides.size()}"

def engine = new SimpleTemplateEngine()
def templateFilename = "reveal-template.html"
def reader = new FileReader(new File(templateFilename))
def writeable = engine.createTemplate(reader).make([slides: slides])
writeable.writeTo new FileWriter(outputFile)

