package winterhold.powers.spelldamageresistant

import basemod.interfaces.CloneablePowerInterface
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import winterhold.WinterholdMod.Companion.makeID
import winterhold.WinterholdMod.Companion.makePowerPath
import winterhold.coloredkeywords.KeywordColorer
import winterhold.spelldamage.SpellDamageTracker
import winterhold.spelldamage.SpellDamageType
import winterhold.util.TextureLoader

class FireResistancePower(owner: AbstractCreature?, amount: Int, isSourceMonster: Boolean) : AbstractPower(),
    CloneablePowerInterface {
    private var justApplied = false
    private val isSourceMonster: Boolean
    override fun atDamageReceive(damage: Float, damageType: DamageType): Float =
        if (SpellDamageTracker.inDamagePhaseOfElementalAttack && RESISTANT_TO === SpellDamageTracker.combo.currentDamageType) {
            damage / 2
        } else damage

    override fun atEndOfRound() {
        if (justApplied) {
            justApplied = false
            return
        }
        if (amount == 0) {
            addToBot(RemoveSpecificPowerAction(owner, owner, POWER_ID))
        } else {
            addToBot(ReducePowerAction(owner, owner, POWER_ID, 1))
        }
    }

    override fun updateDescription() {
        description = KeywordColorer.replaceColoredKeywords(
            DESCRIPTIONS[0] + "50" + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2]
        )
    }

    override fun makeCopy(): AbstractPower {
        return FireResistancePower(owner, amount, isSourceMonster)
    }

    companion object {
        val POWER_ID = makeID(FireResistancePower::class.java.simpleName)
        private val RESISTANT_TO = SpellDamageType.FIRE
        private val powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        val NAME = KeywordColorer.replaceColoredKeywords(powerStrings.NAME)
        val DESCRIPTIONS = powerStrings.DESCRIPTIONS
        private val tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"))
        private val tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"))
    }

    init {
        name = NAME
        ID = POWER_ID
        this.owner = owner
        this.amount = amount
        this.isSourceMonster = isSourceMonster
        if (AbstractDungeon.actionManager.turnHasEnded && isSourceMonster) {
            justApplied = true
        }
        type = PowerType.BUFF
        isTurnBased = true
        region128 = AtlasRegion(tex84, 0, 0, 84, 84)
        region48 = AtlasRegion(tex32, 0, 0, 32, 32)
        updateDescription()
    }
}