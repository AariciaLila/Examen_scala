import scopt.OParser
import scalaj.http.Http
import scala.util.{Try, Success, Failure}
case class Config(limit: Int = 10, keyword: String = "")
import play.api.libs.json.{Json, JsValue,JsArray}


case class WikiPage(title : String, word : Int)

object Main extends App {
  parseArguments(args) match {
    case Some(config) => run(config)
    case _            => println("Unable to parse arguments")
  }

  def parseArguments(args: Array[String]): Option[Config] = {
    val builder = OParser.builder[Config]
    val parser = {
      import builder._
      OParser.sequence(
        programName("WikiStats"),
        opt[Int]('l', "limit")
          .action((value, config) => config.copy(limit = value))
          .text("Limit the number of pages returned by the API"),
        arg[String]("<keyword>")
          .required()
          .action((value, config) => config.copy(keyword = value))
          .text("Keyword to search for")
      )
    }

    OParser.parse(parser, args, Config())
  }

  def run(config : Config): Unit = {
    val url = formatUrl(config.keyword, config.limit)
    val resultat = getPages(url)
    resultat match {
      case Left(errorCode) => println(s"Error! Response code: $errorCode")
      case Right(body) => println(s"Nombre page : ${parseJson(body).size} \n Liste des pages : ${parseJson(body)} \n Nombre total : ${totalWords(parseJson(body))}")
    }
  }

  
  // Cette fonction prend deux arguments en entrée et formate l'url de base en ajoutant le mot clé et la limite de page.
  def formatUrl(keyword: String, limit: Int): String = {
    val url_base = s"https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&sroffset=0&list=search&srsearch=$keyword&srlimit=$limit"
    url_base
  }


  // 4. Cette  fonction prend une URL est renvoie un Either avec deux possibilités 
  // Left : Si le code de la réponse est différent de 200, avec comme valeur ce code d’erreur
  // Right : Si le code de la réponse vaut 200, avec comme valeur le corps de la page

  def getPages(url: String): Either[Int, String] = {
    try {
      val result = Http(url).asString
      result.code match {
        case 200 => Right(result.body)
        case _ => Left(result.code)
      }
    } catch {
      case ex: Exception => Left(8000) 
    }
  }

  def parseJson(rawJson: String): Seq[WikiPage] = {
     val json = Json.parse(rawJson)
    val pages = (json \ "query" \ "search").as[JsArray].value

    pages.map { page =>
      val title = (page \ "title").as[String]
      val wordCount = (page \ "wordcount").asOpt[Int].getOrElse(0)
      WikiPage(title, wordCount)
    }.toList
  }

  def totalWords(pages: Seq[WikiPage]): Int = {
    pages.foldLeft(0) { (total, page) =>
      total + page.word
    }
  }





  // val response = getPages("https://google.com/404/")
  // println(response)

 


}
