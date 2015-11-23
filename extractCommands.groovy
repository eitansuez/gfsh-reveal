#!/usr/bin/env groovy

if (args.length != 1) {
  println "Usage: invoke with a single argument (file to be processed)"
  return
}

String filename = args[0]

def file = new File(filename)
if (!file.exists()) {
  println "file ${filename} does not exist."
  return
}

boolean isCommand(line) {
  line.startsWith('$') || line.startsWith('gfsh>')
}

String processLine(line) {
  line.replaceAll(~/</, "&lt;")
}

def commands = file.readLines().findAll { isCommand(it) }
println commands.join('\n')


