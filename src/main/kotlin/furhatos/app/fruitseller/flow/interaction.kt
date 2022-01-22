package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.BuyFruit
import furhatos.app.fruitseller.nlu.Fruit
import furhatos.app.fruitseller.nlu.FruitList
import furhatos.app.fruitseller.nlu.RequestOptions
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.util.Language


val Start : State = state(Interaction) {

    onEntry {
        random (
            { furhat.say("Hi there") },
            {furhat.say("Oh, hello there") }
        )
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
        furhat.say("We have ${Fruit().getEnum(Language.ENGLISH_US).joinToString(", ")})")
        furhat.ask("Do you want some?")
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
        furhat.say("Okay, that's a shame. Have a splendid day!")
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
        furhat.say("Okay, here is your order of ${users.current.order.fruits}.")
        goto(ConfirmOrder)
    }
}

val ConfirmOrder = state(Options) {
    onEntry {
        furhat.ask("Can you confirm the order?")
    }


    onResponse<Yes> {
        goto(Idle)
    }

    onResponse<No> {
        users.current.order.fruits = FruitList()
        goto(TakingOrder)
    }
}
