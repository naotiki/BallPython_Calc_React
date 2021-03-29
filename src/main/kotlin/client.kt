import kotlinx.browser.document
import react.dom.render


fun main() {
    val api = Api()
    api.about { a, b ->
        GeneticPair.geneticPairs = a
        Genetic.geneticList = b
        render(document.getElementById("root")) {
            child(Welcome::class) {
                attrs.api = api
            }
        }
    }

}




