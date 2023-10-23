package cl.jpino.junit5app.models;

import cl.jpino.junit5app.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Juan", new BigDecimal("1000.12345"));
        System.out.println("iniciando el metodo.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("finalizando el metodo.");
    }


    @Tag("cuenta")
    @Nested
    @DisplayName("Test de cuenta bancaria")
    class testCuentaBancaria{

    @Tag("cuenta")
    @Tag("banco")
    @Test
    @DisplayName("probando assertNotNull y assertEquals con variables")
    void testNombreCuenta() {
        //Cuenta cuenta = new Cuenta("Juan", new BigDecimal(1000.12345));
//        cuenta.setPersona("Juan");

        String esperado = "Juan";
        String real = cuenta.getPersona();

        assertNotNull(real, "la cuenta no puede ser nula");
        //sin la expresion lamda se crea el mensaje y se envia, si falla la prueba se crea de igual froma ocupando espacio en memoria
        assertEquals(esperado, real, ()-> "el nombre esperado no coincide");
        //con la expresion lamda ()-> se crea ejecuta el mensaje en caso de fallar la prueba
        assertTrue(real.equals("Juan"));
    }

    @Test
    @DisplayName("probando valor en formato double")
    void testSaldocuenta() {
       // Cuenta cuenta = new Cuenta("Juan", new BigDecimal(1000.12345));

        assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), ()-> "El saldo de la cuenta no coincide, el saldo es "+
                cuenta.getSaldo().doubleValue() + " y el valor esperado es " + 1000.12345);
    }

    @Test
    @DisplayName("convirtiendo bigdecimal a double y usando compareTO(BigDecimal.ZERO)")
    void testSaldoCuenta2() {

       // Cuenta cuenta = new Cuenta("Juan", new BigDecimal(1000.12345));
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertNotNull(cuenta.getSaldo());

        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        // se compara el valor del saldo con BigDecimal.ZERO, como es mayor devuelve 1, y 1 < 0 es falso por tanto la prueba pasa
    }

    @Test
    @DisplayName("probando con metodologia TDD, test driven development ")
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta); // pasa la prueba ya que son dos objetos diferentes con valores iguales
        assertEquals(cuenta2, cuenta);// no pasa el test ya que son objetos diferentes, apuntan a espacios de la memoria diferentes
        // con la metodologia TDD sobreescribimos el metodo equals de la clase Cuenta para que compare los objetos por los valores de sus atributos
    }

    @Test
    @DisplayName("probando con metodologia TDD, valores int y string ")
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("TDD para credito y debito")
    void testCreditoCuenta() {
//      Cuenta cuenta = new Cuenta("Juan", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("TDD con metodos en clase cuenta")
    void testDineroInsuficienteException() {
 //   Cuenta cuenta = new Cuenta("Juan", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);

    }

    @Test
    @DisplayName("TDD con nueva clase Banco")
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Juan", new BigDecimal("1500.8989"));
        Banco banco = new Banco();

        banco.setNombre("Banco del estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Tag("banco")
    @Test
    @DisplayName("probando anotacion Disabled y metodo de assertions 'fail' ")
    @Disabled //se salta esta prueba, como pendiente

    void testRelacionBancoCuentas() {
        fail(); //fuerza el error del test
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Juan", new BigDecimal("1500.8989"));
        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertAll(() -> {
                },
                () -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString()),
                () -> assertEquals("3000", cuenta1.getSaldo().toPlainString()),
                () -> assertEquals(2, banco.getCuentas().size()),
                () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre()),
                () -> {
                    assertEquals("Juan", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Juan")).findFirst().get().getPersona());
                },
                () -> {
                    assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("Juan")));
                });
    }
    }

//Test Condicionales

    @Tag("so")
    @Nested
    @DisplayName("Test de SO")
    class sistemaOperativoTest{
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testOnlyWindows(){
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testOnLinuxMac(){
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows(){
        }
    }

    @Tag("version")
    @Nested
    @DisplayName("Test de Java Version")
    class javaVersionTest{
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJk8() {
        }

        @Test
        @EnabledOnJre(JRE.OTHER)
        void testOtroJre() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_15)
        void testJava15Jre() {
        }
    }

    @Nested
    @DisplayName("Test de propiedades de sistema")
    class systemPropertiesTest{
        @Test
        void imprimirSystemProperties() {
            Properties properties =  System.getProperties();
            properties.forEach((k, v)->System.out.println(k + " : " + v));
        }

        @Test
        @EnabledIfSystemProperty(named="user.country", matches="CL")
        void testSystemCountry(){
        }

        @Test
        @EnabledIfSystemProperty(named="java.version", matches=".*17.*")
            //.* 17.* todas las versiones desde JRE 17. sino poner la especifica 17.0.1
        void testJavaVersion(){
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches=".*32.*")
        void testNo32() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }
    }

//Test condicional a varialbes de ambiente
    @Nested
    @DisplayName("Test de variables de ambiente")
    class variableDeAmbienteTest{
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k,v)-> System.out.println(k+ " = " + v));
        }
        @Test
        @EnabledIfEnvironmentVariable(named="JAVA_HOME", matches = ".*jdk-17.0.6.10-hotspot.*")
        void testJavaHome(){
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "16")
        void testProcesadores(){}

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "dev")
        void testEnv(){
        // CuentaTest > Edit Configurations > Enviroment Variables
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "prod")
        void testEnvProdDisabled(){
        }
    }


// Suposiciones Assumptions
    @Nested
    @DisplayName("Test de suposiciones")
    class assumptionsTests{
        @Test
        @DisplayName("assumeTrue test Saldo Cuenta Dev")
        void testSaldoCuentaDev() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(esDev);
            // todo lo que este abajo de esta linea donde esta assumeTrue se deshabilita si el boolean es falso

            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertNotNull(cuenta.getSaldo());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        }

        @Test
        @DisplayName("Assuming that ENV = dev")
        void testSaldoCuentaDev2() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumingThat(esDev, ()->{
                // todo lo que este abajo de esta linea donde esta assumeTrue se deshabilita si el boolean es falso

                assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
                assertNotNull(cuenta.getSaldo());
                assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            });

        }
    }

    // RepeatedTest para repertir pruebas cuando sea necesario, por ejemplo cuando se generan numeros aleatorios, cuando hay asincronismo, tiempos de ejecucion. etc..
    @RepeatedTest(value = 5, name = "{displayName} - Repeticion numero {currentRepetition} de {totalRepetitions}")
    @DisplayName("Probando Repetir en debito cuenta ")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if (info.getCurrentRepetition()>=3){
            System.out.println("Estamos en la repeticion " + info.getCurrentRepetition());
        }
//        cuenta = new Cuenta("Juan", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    // Pruebas Parametrizadas : se entregan parametros para realizar pruebas, en este caso con metodo debito de la clase cuenta.
    @Tag("param")
    @Nested
    class PruebasParametrizadasTest{

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @ValueSource(strings = {"100", "200", "300", "500", "700", "1000.1234"})
        //usamos String ya que double tiene limitaciones de precisión de los números de punto flotante, la representación interna de este valor puede no ser exacta y podría ser una aproximación cercana a 1000.12345
    void testDebitoCuentaValueSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

        //usamos CsvSource para entregar indice y valor.
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvSource ({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000.1234"})
    void testDebitoCuentaCsvSource(String index, String monto) {
        System.out.println(index + " -> " + monto);
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }
        //usamos CsvSource para asignar dos enteros, a y b, y una variable resultado
    @ParameterizedTest
    @CsvSource({ "2, 3, 3", "5, 7, 12", "10, 20, 30" })
    void testSuma(int a, int b, int resultado) {
        int suma = a + b;
        assertEquals(resultado, suma);
    }


    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvFileSource(resources = "/data.csv")
    void testDebitoCuentaCsvFileSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvSource ({"200,100, roberto, Roberto", "250,200,Andres,Andres", "300,300,Pepe,Pepe", "501,500,Cata,Cata", "750,700,Pipe,Pipe", "1000.12345,1000.12345,jose,Jose"})
    void testDebitoCuentaCsvSource2(String saldo, String monto, String esperado, String actual) {
        System.out.println(saldo + " -> " + monto);
        cuenta.setSaldo(new BigDecimal(saldo));
        cuenta.setPersona(actual);
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertNotNull(cuenta.getPersona());
        assertEquals(esperado,actual);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvFileSource (resources = "/data2.csv")
    void testDebitoCuentaCsvFileSource2(String saldo, String monto, String esperado, String actual) {
        System.out.println(saldo + " -> " + monto);
        cuenta.setSaldo(new BigDecimal(saldo));
        cuenta.setPersona(actual);
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertNotNull(cuenta.getPersona());
        assertEquals(esperado,actual);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }
    }
    //Los tags permiten filtrar ejecucion de pruebas segun tags-> edit configurations/Build and run/ tag -> nombre tag
    @Tag("param")
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @MethodSource ("montoList")
    void testDebitoCuentaMetodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> montoList(){
        return Arrays.asList("100", "200", "300", "500", "700", "1000.1234");
    }

}