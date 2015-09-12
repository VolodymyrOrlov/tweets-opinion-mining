package com.vorlov.util

import org.slf4j.Logger

object Log {

  def debug[T](value: T, text: String = "")(implicit log: Logger): T = {
    log.info(Seq(text, s"[$value]").mkString(" "))
    value
  }

}
