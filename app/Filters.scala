package filters

import play.filters.gzip.GzipFilter


object Filters {

  val headersToGzip = Seq("text/html", "text/json", "application/json", "application/javascript", "text/javascript", "text/css")

  val gzipFilter = new GzipFilter(shouldGzip=(request, response) => headersToGzip.exists(h => response.headers.get("Content-Type").exists(header => header.startsWith(h))))

}