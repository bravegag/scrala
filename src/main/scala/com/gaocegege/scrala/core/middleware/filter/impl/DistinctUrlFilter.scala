package com.gaocegege.scrala.core.middleware.filter.impl

import com.gaocegege.scrala.core.middleware.filter.Filter
import scala.collection.mutable
import org.slf4j.LoggerFactory
import com.typesafe.scalalogging.Logger

/**
 * Filter implementation that keeps track of seen url's and
 * will filter out duplicates.
 */
class DistinctUrlFilter extends Filter {
  /** record the urls that have been crawled already */
  private val seens = mutable.Set[String]()
  private val logger = Logger(LoggerFactory getLogger ("distinct-url-filter"))

  def filter(url: String): Boolean = {
    if (seens contains url) {
      logger debug ("duplicated url: " + url)
      false
    } else {
      seens.add(url)
      true
    }
  }
}
