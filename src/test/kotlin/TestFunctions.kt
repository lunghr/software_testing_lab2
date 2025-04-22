import org.example.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals
import kotlin.math.*


class TestFunctions {
    private val sinTable = FunctionCsvLoader.load("/output/sin.csv")
    private val cosTable = FunctionCsvLoader.load("/output/cos.csv")
    private val tanTable = FunctionCsvLoader.load("/output/tan.csv")
    private val cotTable = FunctionCsvLoader.load("/output/cot.csv")
    private val secTable = FunctionCsvLoader.load("/output/sec.csv")
    private val cscTable = FunctionCsvLoader.load("/output/csc.csv")

    private val lnTable = FunctionCsvLoader.load("/output/ln.csv")
    private val log2Table = FunctionCsvLoader.load("/output/log2.csv")
    private val log3Table = FunctionCsvLoader.load("/output/log3.csv")
    private val log10Table = FunctionCsvLoader.load("/output/log10.csv")

    private val functions = Functions()

    @Nested
    inner class UnitTests {
        @ParameterizedTest
        @CsvSource(
            "-1.0",
            "0.5235987755982988",
            "3.141592653589793",
            "1.5707963267948966",
            "0.0",
            "1.0"
        )
        fun `test simple functions`(n: Double) {
            assertAll(
                { assertEquals(sin(n), functions.sin(n), 1e-10) },
                { assertEquals(cos(n), functions.cos(n), 1e-10) },
                { if (cos(n).absoluteValue > 1e-10) assertEquals(tan(n), functions.tan(n), 1e-10) },
                { if (sin(n).absoluteValue > 1e-10) assertEquals(1 / tan(n), functions.cot(n), 1e-10) },
                { if (cos(n).absoluteValue > 1e-10) assertEquals(1 / cos(n), functions.sec(n), 1e-10) },
                { if (sin(n).absoluteValue > 1e-10) assertEquals(1 / sin(n), functions.csc(n), 1e-10) }
            )
        }

        @ParameterizedTest
        @CsvSource(
            "1.0",
            "2.718281828459045",
            "3.0",
            "10.0"
        )
        fun `test ln function`(n: Double) {
            assertAll(
                { assertEquals(ln(n), functions.ln(n), 1e-10) },
                { assertEquals(log(n, 3.0), functions.log3(n), 1e-10) },
                { assertEquals(log(n, 10.0), functions.log10(n), 1e-10) }
            )
        }

        @Test
        fun `test ln exceptions`() {
            assertAll(
                { assertThrows<IllegalArgumentException> { functions.ln(-1.0) } },
                { assertThrows<IllegalArgumentException> { functions.ln(0.0) } }
            )
        }
    }

    @Nested
    inner class FirstLevelIntegrationTest {
        @ParameterizedTest
        @CsvFileSource(resources = ["/expected_results.csv"], numLinesToSkip = 1)
        @DisplayName("FullFunction with full mocked functions")
        fun `test full function with full mocked functions`(x: Double, expected: Double) {

            val trigMock = mock(TrigFunctions::class.java)
            val logMock = mock(LogFunctions::class.java)

            if (x <= 0) {
                `when`(trigMock.sin(x)).thenReturn(sinTable[x] ?: Double.NaN)
                `when`(trigMock.cos(x)).thenReturn(cosTable[x] ?: Double.NaN)
                `when`(trigMock.tan(x)).thenReturn(tanTable[x] ?: Double.NaN)
                `when`(trigMock.cot(x)).thenReturn(cotTable[x] ?: Double.NaN)
                `when`(trigMock.sec(x)).thenReturn(secTable[x] ?: Double.NaN)
                `when`(trigMock.csc(x)).thenReturn(cscTable[x] ?: Double.NaN)
            } else {
                `when`(logMock.ln(x)).thenReturn(lnTable[x] ?: Double.NaN)
                `when`(logMock.log2(x)).thenReturn(log2Table[x] ?: Double.NaN)
                `when`(logMock.log3(x)).thenReturn(log3Table[x] ?: Double.NaN)
                `when`(logMock.log10(x)).thenReturn(log10Table[x] ?: Double.NaN)
            }

            val function = FullFunction(trigMock, logMock)
            val res = function.calculate(x)

            println("x = $x -> result = $res, expected = $expected")
            assertEquals(expected, res, 1e-3)
        }
    }

    @Nested
    inner class SecondLevelIntegrationTest {
        @ParameterizedTest
        @CsvFileSource(resources = ["/expected_results.csv"], numLinesToSkip = 1)
        @DisplayName("FullFunction with real sin and ln functions")
        fun `test full function with real sin and ln functions`(x: Double, expected: Double) {
            val cosMock = mock(UnaryFunction::class.java)
            val tanMock = mock(UnaryFunction::class.java)
            val secMock = mock(UnaryFunction::class.java)
            val cscMock = mock(UnaryFunction::class.java)
            val cotMock = mock(UnaryFunction::class.java)

            val log2Mock = mock(UnaryFunction::class.java)
            val log3Mock = mock(UnaryFunction::class.java)
            val log10Mock = mock(UnaryFunction::class.java)

            if (x <= 0) {
                `when`(cosMock.calculate(x)).thenReturn(cosTable[x] ?: Double.NaN)
                `when`(tanMock.calculate(x)).thenReturn(tanTable[x] ?: Double.NaN)
                `when`(secMock.calculate(x)).thenReturn(secTable[x] ?: Double.NaN)
                `when`(cscMock.calculate(x)).thenReturn(cscTable[x] ?: Double.NaN)
                `when`(cotMock.calculate(x)).thenReturn(cotTable[x] ?: Double.NaN)
            } else {
                `when`(log2Mock.calculate(x)).thenReturn(log2Table[x] ?: Double.NaN)
                `when`(log3Mock.calculate(x)).thenReturn(log3Table[x] ?: Double.NaN)
                `when`(log10Mock.calculate(x)).thenReturn(log10Table[x] ?: Double.NaN)
            }

            val function = FullFunction(
                object : TrigFunctions {
                    override fun sin(x: Double): Double = functions.sin(x)
                    override fun cos(x: Double): Double = cosMock.calculate(x)
                    override fun tan(x: Double): Double = tanMock.calculate(x)
                    override fun cot(x: Double): Double = cotMock.calculate(x)
                    override fun sec(x: Double): Double = secMock.calculate(x)
                    override fun csc(x: Double): Double = cscMock.calculate(x)
                },
                object : LogFunctions {
                    override fun ln(x: Double): Double = functions.ln(x)
                    override fun log2(x: Double): Double = log2Mock.calculate(x)
                    override fun log3(x: Double): Double = log3Mock.calculate(x)
                    override fun log10(x: Double): Double = log10Mock.calculate(x)
                }
            )
            val res = function.calculate(x)

            println("x = $x -> result = $res, expected = $expected")
            assertEquals(expected, res, 1e-3)
        }
    }

    @Nested
    inner class ThirdLevelIntegrationTest {
        @ParameterizedTest
        @CsvFileSource(resources = ["/expected_results.csv"], numLinesToSkip = 1)
        @DisplayName("FullFunction with mocked trig functions and real log functions")
        fun `test full function with mocked trig functions and real log functions`(x: Double, expected: Double) {
            val tanMock = mock(UnaryFunction::class.java)
            val secMock = mock(UnaryFunction::class.java)
            val cscMock = mock(UnaryFunction::class.java)
            val cotMock = mock(UnaryFunction::class.java)

            if (x <= 0) {
                `when`(tanMock.calculate(x)).thenReturn(tanTable[x] ?: Double.NaN)
                `when`(secMock.calculate(x)).thenReturn(secTable[x] ?: Double.NaN)
                `when`(cscMock.calculate(x)).thenReturn(cscTable[x] ?: Double.NaN)
                `when`(cotMock.calculate(x)).thenReturn(cotTable[x] ?: Double.NaN)
            }

            val function = FullFunction(
                object : TrigFunctions {
                    override fun sin(x: Double): Double = functions.sin(x)
                    override fun cos(x: Double): Double = functions.cos(x)
                    override fun tan(x: Double): Double = tanMock.calculate(x)
                    override fun cot(x: Double): Double = cotMock.calculate(x)
                    override fun sec(x: Double): Double = secMock.calculate(x)
                    override fun csc(x: Double): Double = cscMock.calculate(x)
                },
                object : LogFunctions {
                    override fun ln(x: Double): Double = functions.ln(x)
                    override fun log2(x: Double): Double = functions.log2(x)
                    override fun log3(x: Double): Double = functions.log3(x)
                    override fun log10(x: Double): Double = functions.log10(x)
                }
            )
            val res = function.calculate(x)

            println("x = $x -> result = $res, expected = $expected")
            assertEquals(expected, res, 1e-3)
        }
    }

    @Nested
    inner class FourthLevelIntegrationTest {
        @ParameterizedTest
        @CsvFileSource(resources = ["/expected_results.csv"], numLinesToSkip = 1)
        @DisplayName("FullFunction with full real functions")
        fun `test full function`(x: Double, expected: Double) {
            val function = FullFunction(
                object : TrigFunctions {
                    override fun sin(x: Double): Double = functions.sin(x)
                    override fun cos(x: Double): Double = functions.cos(x)
                    override fun tan(x: Double): Double = functions.tan(x)
                    override fun cot(x: Double): Double = functions.cot(x)
                    override fun sec(x: Double): Double = functions.sec(x)
                    override fun csc(x: Double): Double = functions.csc(x)
                },
                object : LogFunctions {
                    override fun ln(x: Double): Double = functions.ln(x)
                    override fun log2(x: Double): Double = functions.log2(x)
                    override fun log3(x: Double): Double = functions.log3(x)
                    override fun log10(x: Double): Double = functions.log10(x)
                }
            )
            val res = function.calculate(x)

            println("x = $x -> result = $res, expected = $expected")
            assertEquals(expected, res, 1e-3)
        }
    }
}