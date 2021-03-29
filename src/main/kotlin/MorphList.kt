import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import react.*

interface MorphListProps : RProps {
    var filter: String
}
data class MorphListState(
    var filter:String
):RState
class MorphList(props: MorphListProps): RComponent<MorphListProps,MorphListState>(props){
    override fun MorphListState.init(props: MorphListProps) {

   filter=props.filter
    }



    override fun RBuilder.render() {

    }


}
interface MLProps:RProps{
    var filter:String
}
