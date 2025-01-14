package winterhold.cards

import basemod.helpers.TooltipInfo
import winterhold.coloredkeywords.KeywordColorer
import winterhold.spelldamage.SpellDamageType

abstract class AbstractKeywordColorerCard(
    id: String,
    name: String,
    img: String,
    cost: Int,
    rawDescription: String,
    type: CardType,
    color: CardColor,
    rarity: CardRarity,
    target: CardTarget
) : AbstractSecondMagicNumberCard(
    id,
    name,
    img,
    cost,
    KeywordColorer.replaceColoredKeywords(rawDescription),
    type,
    color,
    rarity,
    target
) {
    private val tooltipsForPresentColoredKeywords = SpellDamageType.values()
        .filter { rawDescription.contains(it.fullName) }
        .map { TooltipInfo(it.prettyName, it.description) }

    override fun getCustomTooltipsTop(): List<TooltipInfo> {
        return tooltipsForPresentColoredKeywords
    }

}