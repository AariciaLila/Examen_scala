import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalaj.http.Http
import scala.util.{Try, Success, Failure}
import play.api.libs.json.{Json, JsValue, JsArray}

// Classe de test unitaire pour la classe Main
class MainSpec extends AnyFlatSpec with Matchers {
  // Teste la fonction formatUrl (renvoie l'URL correctement formatée)
  "formatUrl" should "return the correctly formatted URL" in {
    val keyword = "Scala" // Mot clé
    val limit = 10 // Limite
    val expectedUrl =
      "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&sroffset=0&list=search&srsearch=Scala&srlimit=10" // URL attendue

    val result = Main.formatUrl(keyword, limit) // Appel de la fonction à tester

    result should be(expectedUrl) // Vérification du résultat attendu
  }

  // Teste la fonction parseArguments (renvoie None si les arguments ne sont pas analysables)
  // 1er test : un avec des arguments non parsable
  "parseArguments" should "return None when arguments are not parsable" in {
    val args = Array("--limit", "invalid", "scala") // Arguments non parsables

    val result = Main.parseArguments(args) // Appel de la fonction à tester

    result should be(None) // Vérification du résultat attendu
  }
  // 2ème test : un avec un mot clef
  it should "return Some(Config) with keyword when keyword is provided" in {
    val args = Array("scala") // Mot clé fourni en argument

    val result = Main.parseArguments(args) // Appel de la fonction à tester

    result should be(Some(Config(keyword = "scala"))) // Vérification du résultat attendu
  }
  // 3ème test : Un avec mot clef et limite
  it should "return Some(Config) with keyword and limit when both keyword and limit are provided" in {
    val args = Array("--limit", "5", "scala") // Mot clé et limite fournis en argument

    val result = Main.parseArguments(args) // Appel de la fonction à tester

    result should be(Some(Config(limit = 5, keyword = "scala"))) // Vérification du résultat attendu
  }

  // Teste la fonction getPages
  // 1er test : retourner Right avec le corps de la page lorsque le code de réponse est 200
  "getPages" should "return Right with the page body when the response code is 200" in {
    val url = "https://example.com/" // URL avec un code de réponse 200
    val expectedBody = "<!doctype html>" // Corps de la page attendu

    val result = Main.getPages(url) // Appel de la fonction à tester

    result match {
      case Right(body) => body should include(expectedBody) // Vérification du résultat attendu
      case _           => fail("Expected Right") // Échec si le résultat n'est pas Right
    }
  }
  // 2ème test : retourner Gauche avec le code de réponse si le code de réponse n'est pas 200
  it should "return Left with the response code when the response code is not 200" in {
    val url = "https://example.com/404" // URL avec un code de réponse différent de 200
    val expectedCode = 404 // Code de réponse attendu

    val result = Main.getPages(url) // Appel de la fonction à tester

    result should be(Left(expectedCode)) // Vérification du résultat attendu
  }
  // 3ème test : renvoie Left avec un code d'erreur personnalisé en cas d'exception
  it should "return Left with a custom error code when an exception occurs" in {
    val url = "https://invalid-url" // URL invalide
    val expectedCode = 8000 // Code d'erreur personnalisé attendu

    val result = Main.getPages(url) // Appel de la fonction à tester

    result should be(Left(expectedCode)) // Vérification du résultat attendu
  }

  // Teste la fonction parseJson (renvoie une liste d'objets WikiPage)
  "parseJson" should "return a list of WikiPage objects" in {
    val rawJson =
      """
        |{
        |  "query": {
        |    "search": [
        |      {
        |        "title": "Page 1",
        |        "wordcount": 100
        |      },
        |      {
        |        "title": "Page 2",
        |        "wordcount": 200
        |      },
        |      {
        |        "title": "Page 3",
        |        "wordcount": 300
        |      }
        |    ]
        |  }
        |}
        |""".stripMargin // JSON brut

    val expectedPages = Seq(
      WikiPage("Page 1", 100),
      WikiPage("Page 2", 200),
      WikiPage("Page 3", 300)
    ) // Pages Wiki attendues

    val result = Main.parseJson(rawJson) // Appel de la fonction à tester

    result should be(expectedPages) // Vérification du résultat attendu
  }
  // Teste la fonction totalWords
  // 1er test : Un avec une liste vide en entrée
  "totalWords" should "return 0 for an empty list of WikiPage objects" in {
    val pages = Seq.empty[WikiPage] // Liste vide de WikiPage

    val result = Main.totalWords(pages) // Appel de la fonction à tester

    result should be(0) // Vérification du résultat attendu
  }
  // 2ème test : Un avec une liste non vide
  it should "return the sum of word counts for a non-empty list of WikiPage objects" in {
    val pages = Seq(
      WikiPage("Page 1", 100),
      WikiPage("Page 2", 200),
      WikiPage("Page 3", 300)
    ) // Liste non vide de WikiPage

    val expectedTotalWords = 600 // Somme des nombres de mots attendue (100 + 200 + 300)

    val result = Main.totalWords(pages) // Appel de la fonction à tester

    result should be(expectedTotalWords) // Vérification du résultat attendu
  }
}