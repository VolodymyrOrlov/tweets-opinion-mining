package com.vorlov

import java.io.File

import com.typesafe.config.ConfigFactory
import com.vorlov.commands._
import com.vorlov.util.IOUtils

import scala.collection.mutable


object Main extends App {

  val commands = mutable.Map.empty[String, Command]

  def registerCommand(triggeredBy: String, command: Command) {
    commands(triggeredBy) = command
  }

  def help() {
    println(s"\nUsage: ${this.getClass.getName} command <configuration file>\n")
  }

  if (args.length < 2) {
    Console.err.println("Incorrect number of input arguments.")
    help()
    sys.exit(1)
  }

  if (!IOUtils.isReadable(args(1))) {
    Console.err.println("Could not open configuration file.")
    sys.exit(2)
  }

  val config = ConfigFactory.parseFile(new File(args(1)))

  registerCommands

  commands.find(_._1 == args(0)).collect{
    case (_, command) => command.run(config)
  }

}
