package furhatos.app.fruitseller.nlu

import furhatos.nlu.*
import furhatos.util.Language
import furhatos.nlu.common.Number


class FruitList : ListEntity<QuantifiedFruit>()

val fruitPrices = mapOf(
    "bananas" to 3.2,
    "orange" to 1.5,
    "apple" to 2.5,
    "cherimoya" to 1.7
)


class FruitPriceRequestPerItem(val fruit:Fruit? = null): Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "How much does it cost the @fruit",
            "What is the price of @fruit"
        )
    }
}

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
        return listOf( "bananas", "orange", "apple", "cherimoya")
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

/*
Game Setup and Name Fetching
 */
class Name : EnumEntity(stemming = false, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf(
            "Stefanos", "Wille", "Joao", "Victor", "Philipp", "Katie", "Manuel", "Alex", "Sofia", "Olivia", "Liam", "Emma", "Noah", "Amelia",
            "Oliver", "Sophia", "Lucas", "Charlotte", "Levi", "James", "Dennis", "Elsa", "Marcel-Robert", "Iolanda", "Thomas", "Manuel", "Arzu", "Emil", "Laura",
            "Edlidir", "Ola", "João", "Philip", "Kristin", "Isak", "Divya", "Alexander", "Mikael", "Miklovana", "Katie", "Elmira", "Ilaria")
    }
}

class TellNameBriefly(val name: Name? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "@name",
            "I am @name")
    }
}
