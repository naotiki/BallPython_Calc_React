import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import react.*
import react.dom.p

external interface MorphListProps : RProps {
    var filter: String
    var onAddPair: (GeneticPair) -> Unit
}



val AllList:FunctionalComponent<MorphListProps> ={
    useMemo({
        buildElement {
            mList {
                GeneticPair.geneticPairs.forEach { pair ->

                    mListItem(button = true, onClick = {_->

                        it.onAddPair(pair)

                    }) {

                        mListItemText(primary = pair.toString())
                    }
                }
            }
        }

    }, arrayOf())
}



val MorphListMemo = memo<MorphListProps> { prop: MorphListProps ->
    if (prop.filter == "") {
        AllList(prop)
    } else {
        buildElement {
            mList {
                GeneticPair.geneticPairs.filter {
                    it.toString().adjust().contains(prop.filter.adjust()) || (prop.filter == "")
                }.forEach { pair ->

                    mListItem(button = true, onClick = {

                        prop.onAddPair(pair)

                    }) {

                        mListItemText(primary = pair.toString())
                    }
                }
            }
        }
    }


}

