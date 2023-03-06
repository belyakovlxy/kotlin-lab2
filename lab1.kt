import mmcs.*
import tasks.*
import java.lang.Exception

fun main() {

    try
    {
        var lock = createMatrix(3,3,1)
        lock.set(0,1,0)
        lock.set(1,0,0)
        lock.set(1,2,0)

        print(lock.toString() + "\n")

        var key = createMatrix(2,2,1)
        key.set(0,1,0)
        key.set(1,0,0)

        print(key.toString() + "\n")

        print(canOpenLock(key, lock))
    }
    catch (e: Exception) {
        println(e)
    }

}
