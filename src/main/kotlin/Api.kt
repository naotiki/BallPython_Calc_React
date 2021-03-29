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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Api {
    var httpStatusCallback: (HttpStatusCode) -> Unit = {

    }
    val endpoint =
        "http://127.0.0.1:4545"//window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved
    val jsonClient = HttpClient(Js) {

        install(JsonFeature) {

            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
            })

        }


        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                httpStatusCallback(response.status)
                console.log(response.receive())

            }
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

    fun Calc(male: Snake, female: Snake, callback: (List<Pair<String, Double>>) -> Unit) {
        if (male.pairs == listOf<GeneticPair>()) {
            male.pairs = listOf(GeneticPair.normal)
        }
        if (female.pairs == listOf<GeneticPair>()) {
            female.pairs = listOf(GeneticPair.normal)
        }
        GlobalScope.launch(Dispatchers.Default) {
            /*jsonClient.get<List<GeneticPair>>(endpoint+GeneticPair.path)
            Genetic.geneticList =jsonClient.get<List<Genetic>>(endpoint+Genetic.path)*/
            /* val two:String=jsonClient.get(endpoint+Genetic.path)
             console.log(two)*/
            val status = jsonClient.use {
                val serializer = PairSerializer(
                    ListSerializer(PairSerializer(Int.serializer(), Int.serializer())),
                    ListSerializer(PairSerializer(Int.serializer(), Int.serializer()))
                )

                val bodyText = Json.encodeToString(serializer, Pair(male.ConvertToInt(), female.ConvertToInt()))
                val response =
                    it.post<HttpResponse>(
                        urlString =
                        "$endpoint/api/calc"
                    ) {
                        body = TextContent(
                            bodyText, ContentType.Application.Json
                        )
                    }

                console.log(response.status)
                if (response.status == HttpStatusCode.OK) {
                    val s = response.readText()
                    console.log(s)

                    callback(Json.decodeFromString(s))
                }

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
