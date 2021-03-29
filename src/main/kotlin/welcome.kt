import GeneticPair.Companion.normal
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.variant
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.lab.alert.MAlertSeverity
import com.ccfraser.muirwik.components.lab.alert.mAlert
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.table.*
import kotlinx.coroutines.Job
import kotlinx.css.*
import org.w3c.dom.Worker
import react.*
import react.dom.li
import styled.css
import styled.styledDiv
import kotlin.math.roundToInt



data class AppState(

    var maleMorphList: MutableSet<GeneticPair>,

    var femaleMorphList: MutableSet<GeneticPair>,

    var maleFind: String,
    var femaleFind: String,
    var order: Order,
    var orderBy: Column,
    var result: List<Pair<String, Double>>,
    var waitForResult: Boolean
) : RState


class Welcome : RComponent<RProps, AppState>() {
private val api:Api=Api()
    override fun AppState.init() {
        console.log(normal)

        maleFind = ""
        maleMorphList = mutableSetOf()

        femaleFind = ""
        femaleMorphList = mutableSetOf()

        order = Order.ascending
        orderBy = Column.Name
        result = emptyList()

        waitForResult = false

    }


    fun SetOrder(newOrder: Column) {
        if (newOrder == state.orderBy) {
            if (state.order == Order.ascending) {
                setState {
                    order = Order.descending
                }

            } else {
                setState {
                    order = Order.ascending
                }
            }
        } else {
            setState {
                orderBy = newOrder
                order = Order.ascending
            }
        }

    }

    override fun RBuilder.render() {


        mAppBar() {
            mToolbar {
                mTypography(variant = MTypographyVariant.h6, text = "Ball Python Calc β")
            }
        }
//OK
        styledDiv {
            css {
                margin(top = 100.px)

            }
            mGridContainer {
                attrs {
                    spacing = MGridSpacing.spacing1
                    direction = MGridDirection.row

                }
                //オス
                mGridItem(xs = MGridSize.cells6) {
                    mTypography(variant = MTypographyVariant.h3) {
                        mIcon("male", fontSize = MIconFontSize.large)
                        +"オス"
                    }
                    mPaper {
                        css {
                            height = 200.px
                            overflow = Overflow.auto
                        }

                        mList {
                            GeneticPair.geneticPairs.filter {
                                it.toString().adjust().contains(state.maleFind.adjust()) || (state.maleFind == "")
                            }.forEach { pair ->

                                mListItem(button = true, onClick = {

                                    setState {
                                        maleMorphList.add(pair)
                                    }

                                }) {

                                    mListItemText(primary = pair.toString())
                                }
                            }

                        }
                    }
                    mTextField(

                        label = "オス",
                        helperText = "何も選択しないとノーマルになります",
                        variant = MFormControlVariant.outlined,
                        onChange = {
                            console.log(it.targetValue)
                            setState {
                                maleFind = it.targetValue as String
                            }

                        }, fullWidth = true
                    )
                    mPaper(component = "ul") {

                        css {
                            height = 75.px
                            overflow = Overflow.auto
                            display = Display.flex
                            justifyContent = JustifyContent.center
                            flexWrap = FlexWrap.wrap
                            listStyleType = ListStyleType.none
                            padding(0.5.px)
                            margin(0.px)
                        }
                        state.maleMorphList.forEach {
                            li {
                                key = it.toString()
                                mChip(label = it.toString(), onDelete = { _ ->

                                    setState {
                                        maleMorphList.remove(it)
                                    }
                                })
                            }
                        }
                    }
                }

                mGridItem(xs = MGridSize.cells6) {
                    mTypography(variant = MTypographyVariant.h3) {
                        mIcon("female", fontSize = MIconFontSize.large)
                        +"メス"
                    }
                    mPaper() {
                        css {
                            height = 200.px
                            overflow = Overflow.auto
                        }

                        mList {
                            GeneticPair.geneticPairs.filter {
                                it.toString().adjust().contains(state.femaleFind.adjust())
                            }
                                .forEach { pair ->

                                    mListItem(button = true, onClick = {

                                        setState {
                                            femaleMorphList.add(pair)
                                        }

                                    }

                                    ) {

                                        mListItemText(primary = pair.toString())
                                    }
                                }
                        }
                    }

                    mTextField(

                        label = "メス",
                        helperText = "何も選択しないとノーマルになります",
                        variant = MFormControlVariant.outlined,

                        onChange = {
                            console.log(it.targetValue)
                            setState {
                                femaleFind = it.targetValue as String
                            }
                        }, fullWidth = true
                    )

                    mPaper(component = "ul") {
                        // mThemeProvider(createMuiTheme(js("({})")))
                        css {
                            height = 75.px
                            overflow = Overflow.auto
                            display = Display.flex
                            justifyContent = JustifyContent.center
                            flexWrap = FlexWrap.wrap
                            listStyleType = ListStyleType.none
                            padding(0.5.px)
                            margin(0.px)
                        }
                        state.femaleMorphList.forEach {
                            li {
                                key = it.toString()
                                mChip(label = it.toString(), onDelete = { _ ->

                                    setState {
                                        femaleMorphList.remove(it)
                                    }

                                })
                            }
                        }
                    }
                }
            }


        }
        if (state.maleMorphList.size + state.femaleMorphList.size >= 10) {
            mAlert("遺伝子の数が多いため計算に時間がかかる場合があります", severity = MAlertSeverity.warning)
        }
        var job:Job?=null
        styledDiv {
            css {
                textAlign = TextAlign.center
                marginTop = 10.px
            }
            mButton("計算", color = MColor.primary, size = MButtonSize.large, onClick = { _ ->

                setState {
                    waitForResult = true
                }
                // job=
              api.Start(Snake(state.maleMorphList.toList()), Snake(state.femaleMorphList.toList())) {
                   if (it != null) {
                       setState {
                           result = it
                       }
                   }
                    setState {
                        waitForResult = false
                    }

                }

                job?.invokeOnCompletion {
                    job=null
                }


            }) {

                attrs {
                    variant = MButtonVariant.contained
                    startIcon = mIcon("calculate", addAsChild = false)
                }
            }

            mPaper {
                mToolbar {
                    mTypography("結果", variant = MTypographyVariant.h6, component = "div")

                }
                mTableContainer {
                    mTable {
                        attrs.stickyHeader = true

                        mTableHead {
                            mTableRow {
                                Column.values().forEach {
                                    mTableCell {
                                        attrs.align =
                                            if (it != Column.Name) MTableCellAlign.right else MTableCellAlign.inherit
                                        attrs.sortDirection =
                                            if (state.orderBy == it && state.order == Order.ascending) MTableCellSortDirection.asc
                                            else if (state.orderBy == it && state.order == Order.descending) MTableCellSortDirection.desc
                                            else MTableCellSortDirection.False

                                        mTableSortLabel(onClick = { _ -> SetOrder(it) }) {
                                            attrs.active = it == state.orderBy
                                            attrs.direction =
                                                if (it == state.orderBy && state.order == Order.descending) MTableSortLabelDirection.desc else MTableSortLabelDirection.asc
                                            +it.label
                                            if (state.orderBy == it) {
                                                mIcon("sorted " + state.order.name)
                                            }
                                        }
                                    }
                                }


                            }
                        }
                        mTableBody {
                            val results =
                                if (state.order == Order.ascending) {
                                    when (state.orderBy) {
                                        Column.Name -> state.result.sortedBy { it.first }
                                        Column.Percentage -> state.result.sortedBy { it.first }
                                        Column.Probability -> state.result.sortedBy { it.second }
                                    }
                                } else {
                                    when (state.orderBy) {
                                        Column.Name -> state.result.sortedByDescending { it.first }
                                        Column.Percentage -> state.result.sortedByDescending { it.first }
                                        Column.Probability -> state.result.sortedByDescending { it.second }
                                    }
                                }
                            results.forEach {
                                mTableRow(hover = true, key = it.first) {
                                    mTableCell {
                                        alias[it.first]?.also { str ->
                                            +str
                                        } ?: run {
                                            +it.first
                                        }

                                    }
                                    mTableCell(align = MTableCellAlign.right) {
                                        +(it.second * 100).toString()
                                    }
                                    mTableCell(align = MTableCellAlign.right) {
                                        +Fraction((it.second * 100).roundToInt(), 100).toString()
                                    }
                                }
                            }


                        }
                    }
                }
            }
        }

        mBackdrop(state.waitForResult){

            css {
                zIndex=10
                color= Color.black
            }
            mCircularProgress(color = MCircularProgressColor.primary)
            mTypography("計算中"){
                css {
                    color= Color.white
                }
            }
            mButton("キャンセル",color = MColor.secondary,onClick = {
                job?.cancel()
                setState { waitForResult=false }
            })
        }
    }


}


