import kotlinx.serialization.Serializable


@Serializable
data class Genetic(val id: Int, val name: String, val order: Order) {

    companion object {
        val path="/Genetics.json"
        var geneticList = listOf<Genetic>();
        val normal:Genetic get() {
            return geneticList[0]
        }
        fun FromInt(value:Int):Genetic {
          return  geneticList.filter {
                it.id==value
            }.first()
        }
    }

    override fun toString(): String {
        return if (id != 0) name else ""
    }
    @Serializable
    enum class Order(val rawValue: Int) {
        ///# 劣勢
        Recessive(1),

        ///# 優性
        Dominance(2),

        /// # 共優性
        CoDominance(3),

        // 例外
        Normal(0);

        companion object {
            fun fromInt(value: Int): Order =
                values().find { it.rawValue == value } ?: throw Exception("Enum Convert Error")
        }

    }
}


