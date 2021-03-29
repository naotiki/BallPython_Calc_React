/**
 * 分数の値、および計算メソッドを保持するクラス。計算メソッドを利用すると自信の分数の値が書き換わる。(引数の値は書き換わりません)
 *
 */
class Fraction(numerator: Int, denominator: Int) {
    /**
     * 分子を返す
     * @return
     */
    /**
     * 分子
     */
    var numerator: Int
        private set
    /**
     * 分母を返す
     * @return
     */
    /**
     * 分母
     */
    var denominator: Int
        private set

    /**
     * 約分を行う
     */
    private fun reduction() {
        val gcdi = gcdi(numerator, denominator)
        numerator = numerator / gcdi
        denominator = denominator / gcdi
    }

    /**
     * 引数の分数を加える
     * @param fraction
     * @return
     */
    fun addition(fraction: Fraction) {
        numerator = fraction.denominator * numerator + fraction.numerator * denominator
        denominator *= fraction.denominator
        reduction()
    }

    /**
     * 引数の分数を引く
     * @param fraction 引かれる数
     * @return
     */
    fun subtraction(fraction: Fraction) {
        numerator = fraction.denominator * numerator - fraction.numerator * denominator
        denominator *= fraction.denominator
        reduction()
    }

    /**
     * 引数の分数を掛ける
     * @param fraction
     * @return
     */
    fun multiplication(fraction: Fraction) {
        denominator *= fraction.denominator
        numerator *= fraction.numerator
        reduction()
    }

    /**
     * 引数の分数を割る
     * @param fraction 割られる数
     * @return
     */
    fun division(fraction: Fraction) {
        denominator *= fraction.numerator
        numerator *= fraction.denominator
        reduction()
    }

    override fun toString(): String {
        //分母が1のときは分子だけ返す
        return if (denominator == 1) {
            numerator.toString()
        } else "$numerator/$denominator"
    }

    companion object {
        /**
         * 最大公約数を求める
         * @param a
         * @param b
         * @return
         */
        private fun gcdi(a: Int, b: Int): Int {
            var a = a
            var b = b
            while (b > 0) {
                val c = a
                a = b
                b = c % b
            }
            return a
        }
    }

    /**分数を作る
     * @param numerator 分子
     * @param denominator 分母
     */
    init {
        //分母がゼロならエラー
        if (denominator == 0) {
            throw RuntimeException("denominator is not permission 'zero'")
        }
        this.numerator = numerator
        this.denominator = denominator
        reduction()
    }
}