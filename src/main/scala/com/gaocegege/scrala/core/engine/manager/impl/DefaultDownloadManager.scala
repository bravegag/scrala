package com.gaocegege.scrala.core.engine.manager.impl

import com.gaocegege.scrala.core.engine.manager.DownloadManager
import akka.actor.{ Props, ActorRef }
import com.gaocegege.scrala.core.downloader.impl.HttpDownloader
import com.gaocegege.scrala.core.common.request.impl.HttpRequest
import scala.util.Random
import com.gaocegege.scrala.core.common.util.Constant
import java.net.URI
import com.gaocegege.scrala.core.common.response.impl.HttpResponse
import org.apache.http.util.EntityUtils

class DefaultDownloadManager(engine: ActorRef, val threadCount: Int = 4) extends DownloadManager(engine) {
  for (i <- 1 to threadCount) {
    workers = (context actorOf (Props[HttpDownloader], "worker-" + (i toString))) :: workers
  }

  /**
   * request, work; end, tell me.
   */
  def receive = {
    case request: HttpRequest => {
      val index = Random nextInt (threadCount)
      logger info ("Worker " + index + " has a new work to do")

      // push callback function to map
      callBackMap += (((request request) getURI) -> request.callback)

      // whenever we get a new job
      pendingJobsCount = pendingJobsCount + 1

      // tell the worker to do
      workers(index) tell ((request, index), self)
    }
    case (Constant.workDownMessage, uri: URI, response: HttpResponse) => {
      // whenever a job is completed
      pendingJobsCount = pendingJobsCount - 1

      // do the callback function
      if (!(response isSuccess)) {
        logger.error("Error getting the response for " + uri)
      } else {
        // callback with arguments
        callBackMap(uri)(response)

        // consume the response
        (response httpResponse) match {
          case Some(closeableHttpResponse) => EntityUtils consume (closeableHttpResponse getEntity)
          case None                        => logger.error("The response status is success but response is None.")
        }
      }

      // all jobs have been completed
      if (pendingJobsCount <= 0) {
        engine tell (Constant.workDownMessage, self)
      }
    }
    case _ => {
      logger warn ("Unexpected message")
    }
  }
}
