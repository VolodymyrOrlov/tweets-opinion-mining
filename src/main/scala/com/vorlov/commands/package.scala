package com.vorlov

package object commands {

  def registerCommands: Unit = {

    Main.registerCommand(FetchData, "fetch-data", "fetch")
    Main.registerCommand(Normalize, "normalize")
    Main.registerCommand("naive-bayes", NaiveBayes)

  }

}
