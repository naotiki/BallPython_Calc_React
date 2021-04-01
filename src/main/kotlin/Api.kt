import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToDynamic
import org.w3c.dom.Worker
import kotlin.math.pow

class Api {

    val endpoint =
        window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved

    fun GetHttpClient() {

    }

    val jsonClient = HttpClient(Js) {

        install(JsonFeature) {

            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
            })

        }


        HttpResponseValidator {
            validateResponse { response: HttpResponse ->


            }
            /*  handleResponseException { cause: Throwable ->
                  window.alert(
  """エラーが発生しました。
  サーバーが落ちている可能性があります。
  ErrorMessage:${cause.message.toString()}"""
                  )
              }*/
        }

    }



    fun about(callback: (List<GeneticPair>, List<Genetic>) -> Unit) {
        GlobalScope.launch(Dispatchers.Default) {


            val one: List<GeneticPair> =
                jsonClient.get(endpoint + GeneticPair.path)//jsonClient.get(endpoint+GeneticPair.path)
            val two: List<Genetic> = jsonClient.get(endpoint + Genetic.path)
            console.log(one)
            console.log(two)

            callback(one, two)

        }
    }

    fun Start(male: Snake, female: Snake, callback: (List<Pair<String, Double>>?) -> Unit): Worker {
        if (male.pairs == listOf<GeneticPair>()) {
            male.pairs = listOf(GeneticPair.normal)
        }
        if (female.pairs == listOf<GeneticPair>()) {
            female.pairs = listOf(GeneticPair.normal)
        }


      val s=  Json.encodeToString(
            PairSerializer(
                ListSerializer(PairSerializer(Int.serializer(), Int.serializer())),
                ListSerializer(PairSerializer(Int.serializer(), Int.serializer()))
            ), Pair(
                male.ConvertToInt(),
                female.ConvertToInt()
            )
        )
       val w= Worker("calc.js")
        w.onmessage={
            console.log(it.data)

          val e=  Json.decodeFromString<List<List<List<Int>>>>(

               it.data.toString()
            )
            console.log(e)
            val result = mutableListOf<Pair<String, Double>>()
            e.toSet().forEach { list ->
                val l= e.filter {it2-> it2==list }.size
                result.add(Pair(Snake.Convert(list).toString(),l.toDouble()/e.size.toDouble()))
            }
            callback(result)



        }
        w.postMessage(s)
        return w
    }


    fun CalcLocal(male: Snake, female: Snake, callback: (List<Pair<String, Double>>?) -> Unit): Job {
        if (male.pairs == listOf<GeneticPair>()) {
            male.pairs = listOf(GeneticPair.normal)
        }
        if (female.pairs == listOf<GeneticPair>()) {
            female.pairs = listOf(GeneticPair.normal)
        }
        return GlobalScope.launch(Dispatchers.Default) {
            var pairs = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
            val f = female.pairs.toMutableList()
            male.pairs.forEach {
                val i = female.pairs.indexOf(it)
                if (i != -1) {
                    pairs.add(Pair(it.toIntPair(), female.pairs[i].toIntPair()))
                    f.removeAt(i)
                } else {
                    pairs.add(Pair(it.toIntPair(), GeneticPair.normal.toIntPair()))
                }
            }
            f.forEach {
                pairs.add(Pair(it.toIntPair(), GeneticPair.normal.toIntPair()))
            }

            if (pairs.size != 1) {
                pairs = pairs.filter {
                    return@filter it.first != Pair(0, 0)
                }.toMutableList()
            }

            val all = 4.0.pow(pairs.size).toInt()

            val children = mutableListOf<Snake>()
            for (i in 0 until all) {
                children.add(Snake())
            }

            for (i in 0 until pairs.size) {
                val pair = pairs[i]
                val groups = listOf(
                    Pair(pair.first.first, pair.second.first),
                    Pair(pair.first.first, pair.second.second),
                    Pair(pair.first.second, pair.second.first),
                    Pair(pair.first.second, pair.second.second),
                )
                var groupIndex = 0
                var index = 0
                while (index < all) {
                    for (i2 in 0 until all / (4.0.pow(i).toInt())) {
                        children[index].addFromId(groups[groupIndex])
                        index++
                    }
                    groupIndex++
                    if (groupIndex >= 4) groupIndex = 0
                }
            }

            val result = mutableListOf<Pair<String, Double>>()
            val allMorphs = mutableListOf<String>()
            children.forEach {
                allMorphs.add(it.toString())
            }
            val morphs = allMorphs.toSet()
            morphs.forEach { element ->
                val l = allMorphs.filter { it == element }.size.toDouble()
                result.add(element to l / allMorphs.size.toDouble())
            }
            callback(result)


        }
    }


    fun Calc(male: Snake, female: Snake, callback: (List<Pair<String, Double>>?) -> Unit): Job {
        if (male.pairs == listOf<GeneticPair>()) {
            male.pairs = listOf(GeneticPair.normal)
        }
        if (female.pairs == listOf<GeneticPair>()) {
            female.pairs = listOf(GeneticPair.normal)
        }
        return GlobalScope.launch(Dispatchers.Default) {
            /*jsonClient.get<List<GeneticPair>>(endpoint+GeneticPair.path)
            Genetic.geneticList =jsonClient.get<List<Genetic>>(endpoint+Genetic.path)*/
            /* val two:String=jsonClient.get(endpoint+Genetic.path)
             console.log(two)*/

            val serializer = PairSerializer(
                ListSerializer(PairSerializer(Int.serializer(), Int.serializer())),
                ListSerializer(PairSerializer(Int.serializer(), Int.serializer()))
            )

            val bodyText = Json.encodeToString(serializer, Pair(male.ConvertToInt(), female.ConvertToInt()))
            val status = jsonClient.post<HttpResponse>(
                urlString =
                "$endpoint/api/calc"
            ) {
                body = TextContent(
                    bodyText, ContentType.Application.Json
                )
            }

            console.log(status.status)
            if (status.status == HttpStatusCode.OK) {
                val s = status.readText()
                console.log(s)

                callback(Json.decodeFromString(s))
            } else {

                callback(null)

            }


//"$endpoint/api/calc", body = Pair(male.ConvertToInt(), female.ConvertToInt())

            //jsonClient.get<List<Pair<String,Double>>>()


        }
    }

    suspend fun getGeneticList(): List<Genetic> {
        return jsonClient.get(endpoint + Genetic.path)
    }

    suspend fun getMorphList(): List<GeneticPair> {
        return jsonClient.get(endpoint + GeneticPair.path)
    }
}


/*
suspend fun Calc(male: Snake,female: Snake):List<Pair<String,Double>> = coroutineScope {
    if (male.pairs == listOf<GeneticPair>()) {
        male.pairs= listOf(GeneticPair.normal)
    }
    if (female.pairs == listOf<GeneticPair>()) {
        female.pairs= listOf(GeneticPair.normal)
    }

    return@coroutineScope async {
        jsonClient.get<List<Pair<String,Double>>>("$endpoint/api/calc", body = Pair(male.ConvertToInt(), female.ConvertToInt()))
    }.await()



}*/
