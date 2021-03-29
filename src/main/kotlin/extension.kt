fun String.toHiragana () :String{

    val buf = StringBuilder()
    for (i in this.indices) {
        val code: Char = this.toCharArray()[i]
        if (code.toInt() in 0x3041..0x3093) {
            buf.append((code.toInt() + 0x60).toChar())
        } else {
            buf.append(code)
        }
    }
    return buf.toString()
}
fun String.adjust():String{
    return this.toLowerCase().toHiragana()
}