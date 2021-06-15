package com.ahmadok.sandwichstand
enum class Status{
    waiting, `in-progress`, ready
}
data class SandwichOrder(val id: String = "",
                         var customerName : String = "",
                         var pickles : Int = 0,
                         var hummus : Boolean = false,
                         var tahini : Boolean = false,
                         var status: Status = Status.waiting)