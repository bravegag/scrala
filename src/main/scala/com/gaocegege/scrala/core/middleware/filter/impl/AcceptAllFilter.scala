package com.gaocegege.scrala.core.middleware.filter.impl

import com.gaocegege.scrala.core.middleware.filter.Filter

/**
 * The default filter, doesn't filter any url
 */
class AcceptAllFilter extends Filter {
  def filter(url: String): Boolean = {
    true
  }
}
