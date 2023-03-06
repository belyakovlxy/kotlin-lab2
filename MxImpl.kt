@file:Suppress("UNUSED_PARAMETER")
package mmcs

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int
    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)
}

/**
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E>
{
    if (height < 1)
        throw Exception("Can't create matrix (height < 1)")
    if (width < 1)
        throw Exception("Can't create matrix (width < 1)")

    var matrix = MatrixImpl<E>(height, width)
    for (i in 0..(height * width - 1))
        matrix.data.add(e)

    return  matrix
}

/**
 * Реализация интерфейса "матрица"
 */

@Suppress("EqualsOrHashCode")
class MatrixImpl<E>( override val height: Int, override val width: Int) : Matrix<E> {

    val data : MutableList<E> = mutableListOf<E>()

    override fun get(row: Int, column: Int): E {
        return data[row * width + column]
    }

    override fun get(cell: Cell): E {
        return data[cell.row * width + cell.column]
    }

    override fun set(row: Int, column: Int, value: E) {
        data[row * width + column]  = value
    }

    override fun set(cell: Cell, value: E) {
        data[cell.row * width + cell.column] = value
    }

    override fun equals(other: Any?) : Boolean {
        if (this === other) return true
        if (other !is MatrixImpl<*>) return false
        if (this.height != other.height || this.width != other.width) return false

        for (row in 0 until this.height - 1)
            for (col in 0 until this.width - 1)
                if (this.get(row,  col) != other.get(row, col)) return false
        return true
    }

    override fun toString(): String {
        var str  = ""
        for (row in 0 until height)
        {
            for (col in 0 until width)
                str = str + get(row, col).toString() + " "
            str += "\n"
        }
        return str
    }
}