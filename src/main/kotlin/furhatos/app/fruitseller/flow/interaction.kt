package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.util.Language


val Start : State = state(Interaction) {

    onEntry {
        random (
            { furhat.say("Hi there") },
            {furhat.say("Oh, hello there") }
        )
        goto(CatchName)
    }
}


val CatchName:State = state(Interaction) {
    onEntry {
        furhat.ask("What is your name?")
    }

    onResponse<TellName> {
        users.current.name = "${it.intent.name}"
        furhat.say("Hello ${users.current.name}. Welcome to the store.")
        goto(TakingOrder)
    }


    onResponse<TellNameBriefly> {
        users.current.name = "${it.intent.name}"
        furhat.say("Hello ${users.current.name}. Welcome to the store.")
        goto(TakingOrder)
    }

}

val Options = state(Interaction) {
    onResponse<Yes> {
        random(
            {furhat.ask("What kind of fruit do you want?")},
            {furhat.ask("What type of fruit?")}
        )
    }

    onResponse<BuyFruit> {
        val fruits = it.intent.fruits
        if (fruits != null) {
            goto(OrderReceived(fruits))
        } else {
            propagate()
        }

    }

    onResponse<RequestOptions> {
//        Fruit().getEnumItems()
        val fruits = Fruit().getEnumItems(Language.ENGLISH_US)
        var fruitOptions:MutableList<String> = mutableListOf<String>()
        for (fruit in fruits) {
            fruitOptions.add(fruit.wordString)
        }
        furhat.say("We have ${fruitOptions.joinToString(",")}")
        furhat.ask("Do you want some ${users.current.name}?")
    }

    onResponse<FruitPriceRequestPerItem> {
        val requestedFruit: String = "${it.intent.fruit}"
        furhat.say("The ${requestedFruit} costs ${fruitPrices.get(requestedFruit)} dollars per item.")
        furhat.ask("Which fruits would you like to buy?")
    }

}


val TakingOrder = state(Options) {
    onEntry {
        random(
            {furhat.ask("How about some fruits?")},
            {furhat.ask("Do you want some fruits?")}
        )
    }

    onResponse<No> {
        furhat.say("Okay ${users.current.name}, that's a shame. Have a splendid day!")
        goto(Idle)
    }
}


fun OrderReceived(fruitList: FruitList) : State = state(Options) {
    onEntry {
        furhat.say("${fruitList.text}, what a lovely choice!")
        fruitList.list.forEach {
            users.current.order.fruits.list.add(it)
        }
        furhat.ask("Anything else?")
    }

    onReentry {
        furhat.ask("Did you want something else?")
    }

    onResponse<No> {
        furhat.say("Okay ${users.current.name}, here is your order of ${users.current.order.fruits}.")
        var price = 0.0
        for (fruit in users.current.order.fruits.list) {
            val numFruits = fruit.count?.value ?: 0
            val fruitPrice = fruitPrices.getOrDefault(fruit.fruit.toString(), 0.0)
            price += numFruits * fruitPrice

        }
        furhat.say("Your total price is ${price} dollars.")
        goto(ConfirmOrder)
    }
}

val ConfirmOrder = state(Options) {
    onEntry {
        furhat.ask("Can you confirm the order?")
    }

    onResponse<Yes> {
        users.current.order.fruits = FruitList()
        goto(DeliveryTime)

    }

    onResponse<No> {
        users.current.order.fruits = FruitList()
        goto(TakingOrder)
    }
}


val DeliveryTime = state(Options) {
    onEntry {
        furhat.ask("When should I deliver the fruits ${users.current.name}?")
    }

    onResponse<Time> {
        users.current.deliveryTime.value = it.intent.value
        furhat.say("Ok ${users.current.name}. I will deliver your order at ${users.current.deliveryTime.value}.")
        goto(Idle)
    }
}