import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import kotlinx.css.*
import react.*
import styled.css

external interface MorphListProps : RProps {
    var filter: String
    var onAddPair: (GeneticPair) -> Unit
}


val AllList: FunctionalComponent<MorphListProps> = {
    useMemo({
        buildElement {
            mList {
                GeneticPair.geneticPairs.forEach { pair ->

                    mListItem(button = true, onClick = { _ ->

                        it.onAddPair(pair)

                    }) {

                        mListItemText(primary = pair.toString())
                    }
                }
            }
        }

    }, arrayOf())
}


val MorphListMemo = memo<MorphListProps>({ prop: MorphListProps ->
    if (prop.filter == "") {
        buildElement {
            mTypography {
                css {
                    color = Color.gray

                }
                attrs {
                    align = MTypographyAlign.center

                }
                +"ここに検索結果が表示されます"
            }
        }
        //AllList(prop)
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


}, { a, b ->

    return@memo a.filter == b.filter
})

