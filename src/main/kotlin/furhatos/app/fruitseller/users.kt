package furhatos.app.fruitseller.flow

import furhatos.app.fruitseller.nlu.FruitList
import furhatos.flow.kotlin.UserDataDelegate
import furhatos.nlu.common.Time
import furhatos.records.User

class FruitData (
    var fruits: FruitList = FruitList()
        )

val User.order : FruitData
    get() = data.getOrPut(FruitData::class.qualifiedName, FruitData())


val User.deliveryTime : Time
    get() = data.getOrPut(Time::class.qualifiedName, Time())


var User.name: String? by UserDataDelegate()