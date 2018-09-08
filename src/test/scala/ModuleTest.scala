import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.{ExecutionContext, Future}

class ModuleTest extends WordSpec with MockitoSugar {

  implicit val ex: ExecutionContext = ExecutionContext.global

  "MyModuleFor3rdParty" should {

    "go normally" in {
      val httpClient: HttpClient = new HttpClient()
      val module: MyModuleFor3rdParty = new MyModuleFor3rdParty(httpClient)

      val response: Option[ModuleResponse] = module.call(username = "Giorgio", password = "Desideri")

      response.isDefined shouldBe true
    }

    "handle 404 response correctly - with Mock" in {
      val httpClient: HttpClient = mock[HttpClient]
      val module: MyModuleFor3rdParty = new MyModuleFor3rdParty(httpClient)

      when(httpClient.send(any())).thenReturn(
        Future.successful(new HttpResponse(code = 404, body = "Match not Found"))
      )

      val response: Option[ModuleResponse] = module.call(username = "Giorgio", password = "Desideri")

      response.isEmpty shouldBe false
      response.map(res => {
        res.code shouldEqual 404
        res.status shouldEqual true
        res.reason.isEmpty shouldEqual true
        res.result.asInstanceOf[String] shouldEqual "Match not Found"
      })
    }
  }
}
