import kotlin.random.Random
import kotlin.system.exitProcess

const val BOARD_SIZE = 10 // Размер игрового поля
const val EMPTY_CELL = '.' // Символ пустой ячейки
const val SNAKE_CELL = 'O' // Символ змейки
const val FOOD_CELL = 'X' // Символ еды

// Класс точки с координатами на игровом поле
data class Point(var x: Int, var y: Int)

fun main() {
    // Создание и инициализация игрового поля
    val board = Array(BOARD_SIZE) { CharArray(BOARD_SIZE) { EMPTY_CELL } }
    // Инициализация змейки в центре поля
    val snake = mutableListOf(Point(BOARD_SIZE / 2, BOARD_SIZE / 2))
    // Генерация еды на игровом поле
    var food = generateFood(board, snake)

    // Размещение еды на игровом поле
    board[food.x][food.y] = FOOD_CELL
    // Установка начального направления движения змейки
    var direction = 'R'

    while (true) {
        // Печать игрового поля
        printBoard(board)
        println("Управление: W (вверх), A (влево), S (вниз), D (вправо)")
        // Считывание направления движения от пользователя
        val input = readLine()?.toUpperCase()

        // Обновление направления движения, если введено корректное значение
        if (input != null && input in listOf("W", "A", "S", "D")) {
            direction = input.first()
        }

        // Получение текущей позиции головы змейки
        val head = snake.first()
        val newHead = Point(head.x, head.y)
        // Обновление позиции головы змейки в зависимости от направления
        when (direction) {
            'W' -> newHead.x--
            'A' -> newHead.y--
            'S' -> newHead.x++
            'D' -> newHead.y++
        }

        // Проверка на столкновение с телом змейки
        if (isCollision(newHead, snake)) {
            println("Игра окончена! Вы столкнулись с телом змейки.")
            exitProcess(0)
        }

        // Проверка на столкновение с границами поля
        if (newHead.x !in 0 until BOARD_SIZE || newHead.y !in 0 until BOARD_SIZE) {
            println("Игра окончена! Вы столкнулись со стеной.")
            exitProcess(0)
        }

        // Добавление новой головы змейки в начало списка
        snake.add(0, newHead)
        // Проверка на съедание еды
        if (newHead == food) {
            // Генерация новой еды
            food = generateFood(board, snake)
            board[food.x][food.y] = FOOD_CELL
        } else {
            // Удаление хвоста змейки
            val tail = snake.removeAt(snake.size - 1)
            board[tail.x][tail.y] = EMPTY_CELL
        }

        // Обновление позиции головы змейки на игровом поле
        board[newHead.x][newHead.y] = SNAKE_CELL
    }
}

// Функция для печати игрового поля
fun printBoard(board: Array<CharArray>) {
    for (row in board) {
        for (cell in row) {
            print("$cell ")
        }
        println()
    }
}

// Функция для генерации новой еды на игровом поле
fun generateFood(board: Array<CharArray>, snake: List<Point>): Point {
    var food: Point
    // Генерация еды на пустом месте
    do {
        food = Point(Random.nextInt(BOARD_SIZE), Random.nextInt(BOARD_SIZE))
    } while (board[food.x][food.y] != EMPTY_CELL || snake.contains(food))
    return food
}

// Функция для проверки столкновения змейки с самой собой
fun isCollision(point: Point, snake: List<Point>): Boolean {
    return snake.contains(point)
}
