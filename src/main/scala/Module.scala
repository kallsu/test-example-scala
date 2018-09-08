import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try

/**
  * HTTP request representation.
  *
  * @param body
  */
case class HttpRequest(body: String)

/**
  * HTTP Response representation.
  *
  * @param code
  * @param body
  */
case class HttpResponse(code: Int, body: String)

trait HttpExecutor {
  def send(request: HttpRequest): Future[HttpResponse]
}

/**
  * HTTP Client implementation, executor of the call
  */
class HttpClient()(implicit ec: ExecutionContext) extends HttpExecutor {
  override def send(request: HttpRequest): Future[HttpResponse] = {
    Future.successful(new HttpResponse(200,
      """{
        | "response_message" : "Hello World"
        |}""".stripMargin))
  }
}

/**
  * Module response. Output of the computation.
  *
  * @param status
  * @param result
  * @param reason
  */
case class ModuleResponse(status: Boolean, code: Int, result: Any, reason: Option[String])

/**
  * Module created to interact with 3rd party API.
  *
  * @param httpClient
  */
class MyModuleFor3rdParty(httpClient: HttpExecutor)(implicit ec: ExecutionContext) {

  def call(username: String, password: String): Option[ModuleResponse] = {
    val body =
      s"""
         | {
         |  "username" : "${username}",
         |  "password" : "${password}"
         | }
      """.stripMargin

    val future: Future[HttpResponse] = httpClient.send(HttpRequest(body))

    Try {
      val response: HttpResponse = Await.result(future, Duration.Inf)

      ModuleResponse(status = true, code = response.code, result = response.body, reason = None)
    }.toOption
  }
}