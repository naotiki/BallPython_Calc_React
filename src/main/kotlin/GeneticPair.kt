import kotlinx.serialization.Serializable


@Serializable
data class GeneticPair(
    val mainGenetic: Genetic, var info: Info = Info.None
) //Nothing To Do
{

    val pair: Pair<Genetic, Genetic>
        get() {
            var p = Pair(mainGenetic, Genetic.normal)
            when (mainGenetic.order) {
                Genetic.Order.Recessive -> {
                    if (info != Info.Het) {
                        p = mainGenetic to mainGenetic

                    }

                }
                Genetic.Order.Dominance -> {
                    //Nothing To Do
                }
                Genetic.Order.CoDominance -> {
                    if (info == Info.Super) {
                        p = mainGenetic to mainGenetic
                    }
                }
                Genetic.Order.Normal -> {
                    //Nothing To Do
                }
            }
            return p
        }

    fun toIntPair(): Pair<Int, Int> {
        return pair.first.id to pair.second.id
    }


    constructor(a: Genetic, b: Genetic) : this(FromPair(a, b).first, FromPair(a, b).second)


    @Serializable
    enum class Info {
        None, Het, Super
    }

    override fun toString(): String {
        var str = "";
        str += when (info) {
            Info.None -> pair.first.toString()
            Info.Het -> "ヘテロ " + pair.first.toString()
            Info.Super -> "スーパー " + pair.first.toString()
        }
        return if (str != "") str else "ノーマル"
    }

    companion object {

        var geneticPairs: List<GeneticPair> = listOf<GeneticPair>();
        val normal: GeneticPair
            get() {
                return geneticPairs[0]
            }
        val path: String = "/GeneticPairs.json"

        fun ToGeneticPairFromID(pair: Pair<Int, Int>): GeneticPair {
            return GeneticPair(Genetic.FromInt(pair.first), Genetic.FromInt(pair.second))
        }

        val FromPair: (a: Genetic, b: Genetic) -> Pair<Genetic, Info> = { a, b ->
            if (a.id != 0 && b.id != 0 && a != b) {
                throw  Exception("不可能な組み合わせ");
            }
            var info: Info = Info.None

            when (a.order) {
                Genetic.Order.Recessive -> {
                    if (b.order == Genetic.Order.Normal) {
                        info = Info.Het;
                    }
                }
                Genetic.Order.Dominance -> {

                }
                Genetic.Order.CoDominance -> {
                    if (a == b) {
                        info = Info.Super;
                    }
                }
                Genetic.Order.Normal -> {
                }
            }
            Pair(a, info)
        }
    }

}