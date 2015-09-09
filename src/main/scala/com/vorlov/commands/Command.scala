package com.vorlov.commands

import com.typesafe.config.Config

import scala.collection.mutable.ListBuffer

trait Command extends DelayedInit {

  protected def configuration: Config = _configuration

  private var _configuration: Config = _

  private val initCode = new ListBuffer[() => Unit]

  override def delayedInit(body: => Unit) {
    initCode += (() => body)
  }

  def run(configuration: Config): Unit = {

    _configuration = configuration

    for (proc <- initCode) proc()

  }

}
