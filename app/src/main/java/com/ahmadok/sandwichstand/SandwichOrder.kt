package com.ahmadok.sandwichstand
enum class Status{
    Waiting, Ready, Done
}
data class SandwichOrder(val id: String = "",
                         val customerName : String = "",
                         var pickles : Int = 0,
                         var hummus : Boolean = false,
                         var tahini : Boolean = false,
                         var status: Status = Status.Waiting)