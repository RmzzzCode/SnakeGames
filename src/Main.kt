import kotlin.random.Random
import kotlin.system.exitProcess

const val BOARD_SIZE = 10
const val EMPTY_CELL = '.'
const val SNAKE_CELL = 'O'
const val FOOD_CELL = 'X'

data class Point(var x: Int, var y: Int)

fun main() {
    val board = Array(BOARD_SIZE) { CharArray(BOARD_SIZE) { EMPTY_CELL } }
    val snake = mutableListOf(Point(BOARD_SIZE / 2, BOARD_SIZE / 2))
    var food = generateFood(board, snake)

    board[food.x][food.y] = FOOD_CELL
    var direction = 'R'

    while (true) {
        printBoard(board)
        println("Управление: W (вверх), A (влево), S (вниз), D (вправо)")
        val input = readLine()?.toUpperCase()

        if (input != null && input in listOf("W", "A", "S", "D")) {
            direction = input.first()
        }

        val head = snake.first()
        val newHead = Point(head.x, head.y)
        when (direction) {
            'W' -> newHead.x--
            'A' -> newHead.y--
            'S' -> newHead.x++
            'D' -> newHead.y++
        }

        if (isCollision(newHead, snake)) {
            println("Игра окончена! Вы столкнулись с телом змейки.")
            exitProcess(0)
        }

        if (newHead.x !in 0 until BOARD_SIZE || newHead.y !in 0 until BOARD_SIZE) {
            println("Игра окончена! Вы столкнулись со стеной.")
            exitProcess(0)
        }

        snake.add(0, newHead)
        if (newHead == food) {
            food = generateFood(board, snake)
            board[food.x][food.y] = FOOD_CELL
        } else {
            val tail = snake.removeAt(snake.size - 1)
            board[tail.x][tail.y] = EMPTY_CELL
        }

        board[newHead.x][newHead.y] = SNAKE_CELL
    }
}

fun printBoard(board: Array<CharArray>) {
    for (row in board) {
        for (cell in row) {
            print(cell)
        }
        println()
    }
}

fun generateFood(board: Array<CharArray>, snake: List<Point>): Point {
    var food: Point
    do {
        food = Point(Random.nextInt(BOARD_SIZE), Random.nextInt(BOARD_SIZE))
    } while (board[food.x][food.y] != EMPTY_CELL || snake.contains(food))
    return food
}

fun isCollision(point: Point, snake: List<Point>): Boolean {
    return snake.contains(point)
}
