package furhatos.app.fruitseller.nlu

import furhatos.nlu.ComplexEnumEntity
import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.ListEntity
import furhatos.util.Language
import furhatos.nlu.common.Number



class FruitList : ListEntity<QuantifiedFruit>()

class QuantifiedFruit(
    val count : Number? = Number(1),
    val fruit : Fruit? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@count @fruit", "@fruit")
    }

    override fun toText(): String {
        return generate("$count $fruit")
    }
}

class Fruit: EnumEntity(stemming=true, speechRecPhrases=true) {
    override fun getEnum(lang: Language):List<String> {
        return listOf("banana","orange","apple","cherimoya")
    }
}

class BuyFruit(var fruits:FruitList? = null): Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@fruits", "I want @fruits", "I would like @fruits", "I want to buy @fruits")
    }
}


class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What options do you have?",
            "What fruits do you have?",
            "What are the alternatives?",
            "What do you have?")
    }
}
