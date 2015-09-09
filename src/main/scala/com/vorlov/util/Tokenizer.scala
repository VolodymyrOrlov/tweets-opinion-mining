package com.vorlov.util

import java.io.{Reader, StringReader}

import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.core.{StopAnalyzer, StopFilter}
import org.apache.lucene.analysis.standard.{UAX29URLEmailTokenizer, StandardFilter}
import org.apache.lucene.analysis.tokenattributes.{TypeAttribute, CharTermAttribute}
import org.apache.lucene.analysis.util.FilteringTokenFilter


object TwitterTokenizer {

  private def tokenStreamComponents(reader: Reader): TokenStreamComponents = {
    val tokenizer = new UAX29URLEmailTokenizer()
    tokenizer.setReader(reader)
    val filter = new StopFilter(new StandardFilter(new URLTokenFilter(tokenizer)), StopAnalyzer.ENGLISH_STOP_WORDS_SET)
    new TokenStreamComponents(tokenizer, filter)
  }

  implicit class TwitterTokenizerRichString(str: String) {

    def tokens: Stream[String] = {
      val tokenStream = tokenStreamComponents(new StringReader(str)).getTokenStream
      tokenStream.reset()

      val term = tokenStream.addAttribute(classOf[CharTermAttribute])

      Stream.continually(tokenStream.incrementToken()).takeWhile(v => v).map(v => term.toString)

    }

  }

  class URLTokenFilter(tokenStream: TokenStream) extends FilteringTokenFilter(tokenStream) {
    private val termType = addAttribute(classOf[TypeAttribute])
    override def accept(): Boolean = termType.`type` == "<ALPHANUM>"
  }

}
