package tasks

import mmcs.Matrix
import mmcs.MatrixImpl
import mmcs.createMatrix



/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

fun <E> rotate(matrix: Matrix<E>): Matrix<E>
{
    var result = createMatrix(matrix.width, matrix.height, matrix.get(0, 0))
    for (row in 0 until matrix.width)
        for (col in 0 until matrix.height)
        {
            result.set(matrix.width - 1 - row, col, matrix.get(col, row))
        }

    return result
}

/**
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int>
{
    if (this.height != other.height || this.width != other.width)
        throw Exception("can't summarize different size matrix")

    var matrix = createMatrix(this.height, this.width, 0)
    for (row in 0 until this.height)
        for (col in 0 until this.width)
        {
            matrix.set(row, col, this.get(row, col) + other.get(row, col))
        }
    return matrix
}

/**
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int>
{
    var matrix = MatrixImpl<Int>(this.height, this.width)
    for (row in 0 until this.height)
        for (col in 0 until this.width)
        {
            matrix.data.add(-this.get(row, col))
        }

    return matrix
}

/**
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int>
{
    if (this.width != other.height)
        throw Exception("can't multiply matrix (M1.width != M2.height)")

    var matrix = createMatrix(this.height, other.width, this.get(0,0))
    for (row in 0 until this.height)
        for (col in 0 until other.width)
        {
            var mltpl = 0
            for (k in 0 until this.width)
            {
                mltpl = mltpl + this.get(row, k) * other.get(k, col)
            }
            matrix.set(row, col, mltpl)
        }
    return matrix
}


/**
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes
{
    var holes = Holes((0 until matrix.height).toMutableList(), (0 until matrix.width).toMutableList())
    var i = 0

    print(matrix.height.toString() + matrix.width.toString() + "\n")
    while (i < matrix.height)
    {
        var j = 0
        while (j < matrix.width)
        {
            print(matrix.get(i, j).toString() + "\n")
            if (matrix.get(i, j) == 1)
            {
                print(i.toString() + " " + j.toString() + "\n")
                if (holes.rows.contains(i))
                    holes.rows.removeAt(holes.rows.indexOf(i))
                if (holes.columns.contains(j))
                    holes.columns.removeAt(holes.columns.indexOf(j))
            }
            j++
        }
        i++
    }
    return holes
}

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: MutableList<Int>, val columns: MutableList<Int>)

/**
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int>
{
    for (i in 0 until (lock.height - key.height + 1))
    {
        for (j in 0 until (lock.width - key.width + 1))
        {
            if (key.get(0, 0) != lock.get(i, j))
            {
                var flag = true
                var keyI = 0

                while (keyI < key.height && flag)
                {
                    var keyJ = 0
                    while (keyJ < key.width && flag)
                    {
                        flag = key.get(keyI, keyJ) != lock.get(i + keyI, j + keyJ)
                        keyJ++
                    }
                    keyI++
                }
                if (flag)
                {
                    return Triple(true, i, j)
                }
            }
        }

    }
    return Triple(false, 0, 0)
}