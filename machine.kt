package machine

import java.util.*

class Coffee{
    // Initializing variables
    val actionText = "Write action (buy, fill, take, remaining, exit): "
    val portions = listOf(
            listOf(250, 0, 16, 4),
            listOf(350, 75, 20, 7),
            listOf(200, 100, 12, 6)
    )
    var resources = mutableMapOf<String, Int>(
            "water" to 400,
            "milk" to 540,
            "coffee beans" to 120,
            "disposable cups" to 9,
            "money" to 550
    )

    /**
     * 0 - Ready For Command
     * 1 - Buy. Waiting for coffee type
     * 2 - Fill. Waiting for water
     * 3 - Fill. Waiting for milk
     * 4 - Fill. Waiting for coffee beans
     * 5 - Fill. Waiting for coffee cups
     * 6 - Fill. Complete
     */
    var state: Int = 0
    /**
     * Executes coffee machine comman
     * @param command - Command to coffee machine
     * @return boolean - on false ending program
     */
    fun execute(command: String):Boolean{
        if (command == "exit") return false
        else if (command == "fill") {
            println()
            state = 2
            fill(0)
            return true
        }
        else if (command == "remaining") {
            println()
            printResources()
            actionText()
            return true
        }
        else if (command == "take") {
            println()
            take()
            return true
        }
        else if (command == "buy") {
            println()
            buy(0)
            return true
        }
        else if (command == "back") {
            println()
            buy(-1)
            return true
        }
        else {
            when{
                state in 2..6 -> fill(command.toInt())
                state == 1 -> buy(command.toInt())
            }
            return true
        }
    }

    fun fill(value: Int) {
        val ask = listOf<String>(
                "Write how many ml of water do you want to add: ",
                "Write how many ml of milk do you want to add: ",
                "Write how many grams of coffee beans do you want to add: ",
                "Write how many disposable cups of coffee do you want to add: ",
                actionText
        )
        if (state == 6) {
            println()
            print(ask[state-2])
        } else {
            print(ask[state-2])
        }
        if (state != 2) {
            when (state-1) {
                2 -> resources["water"] = resources["water"]!! + value
                3 -> resources["milk"] = resources["milk"]!! + value
                4 -> resources["coffee beans"] = resources["coffee beans"]!! + value
                5 -> resources["disposable cups"] = resources["disposable cups"]!! + value
            }
        }
        state++
        if (state > 6) state = 0
    }
    fun actionText() {
        print(actionText)
    }
    fun printResources(){
        println("The coffee machine has:")
        resources.forEach{name, availible -> println("$availible of $name")}
        println()
    }
    fun take() {
        println("I gave you \$${resources["money"]}")
        resources["money"] = 0
        println()
        actionText()
    }
    fun buy(type: Int) {
        val scanner = Scanner(System.`in`)
        if (type == 0){
            print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
            state = 1
        }

        else {
            var error = "none"
            if (type != -1) {
                val resourcesAfter = listOf(resources["water"]!! - portions[type - 1][0],
                        resources["milk"]!! - portions[type - 1][1],
                        resources["coffee beans"]!! - portions[type - 1][2],
                        resources["disposable cups"]!! - 1)
                resourcesAfter.forEachIndexed { index, q ->
                    if (q < 0) {
                        if (error == "none") {
                            when (index) {
                                0 -> error = "water"
                                1 -> error = "milk"
                                2 -> error = "coffee beans"
                                3 -> error = "disposable cups"
                            }
                        }
                    }
                }
                if (error == "none") {
                    resources["water"] = resources["water"]!! - portions[type - 1][0]
                    resources["milk"] = resources["milk"]!! - portions[type - 1][1]
                    resources["coffee beans"] = resources["coffee beans"]!! - portions[type - 1][2]
                    resources["disposable cups"] = resources["disposable cups"]!! - 1
                    resources["money"] = resources["money"]!! + portions[type - 1][3]
                    println("I have enough resources, making you a coffee!")
                    state = 0
                    println()
                    actionText()
                } else {
                    println("Sorry, not enough $error!")
                    state = 0
                    println()
                    actionText()
                }
            } else {
                println()
                state = 0
                actionText()
            }
        }
    }
}
fun main(){
    val machine = Coffee()
    machine.actionText()
    while(machine.execute(readLine()!!)) {

    }
}