package com.vorlov

package object commands {

  def registerCommands: Unit = {

    Main.registerCommand("fetch-data", FetchData)
    Main.registerCommand("naive-bayes", NaiveBayes)

  }

}
