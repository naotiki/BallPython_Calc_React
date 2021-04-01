import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import kotlinx.browser.document
import org.w3c.workers.ServiceWorker
import react.*
import react.dom.render


fun main() {
    val api = Api()

    api.about { a, b ->
        GeneticPair.geneticPairs = a
        Genetic.geneticList = b

        render(document.getElementById("root")) {
            child(Welcome::class) {

            }
        }
    }


}




