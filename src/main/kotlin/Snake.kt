import GeneticPair.Companion.ToGeneticPairFromID
import kotlinx.serialization.Serializable

@Serializable
data class Snake(var pairs:List<GeneticPair>){


    constructor() : this(emptyList<GeneticPair>())

    fun ConvertToInt():List<Pair<Int,Int>>{
        val r= mutableListOf<Pair<Int,Int>>()
        pairs.forEach {
            r.add(it.toIntPair())
        }
        return r
    }

    fun addFromId(a:Pair<Int,Int>){
        val intPair= if (a.first < a.second) a.second to a.first else a
        val ml= pairs.toMutableList()
        ml.add(ToGeneticPairFromID(intPair))

        pairs=ml

    }

    override fun toString(): String {
        var str=""
       val tmp= pairs.sortedBy { it.mainGenetic.id}
        tmp.forEach {
            str+=it.toString()
        }
        str=str.replace("ノーマル","")
       str= if(str!="") str else  "ノーマル"
        return str

    }

    companion object{
        fun Convert(list: List<List<Int>>): Snake {

            val r= mutableListOf<GeneticPair>()
            list.forEach {
                r.add( ToGeneticPairFromID(Pair(it[0],it[1])))
            }
            return Snake(r)
        }

    }


}
