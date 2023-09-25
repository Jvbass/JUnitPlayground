package cl.jpino.junit5app.models;

import cl.jpino.junit5app.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.w3c.dom.ls.LSOutput;

import java.math.BigDecimal;
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


    @Nested
    @DisplayName("Test de cuenta bancaria")
    class testCuentaBancaria{


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
//        cuenta = new Cuenta("Juan", new BigDecimal("1000.12345"));
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

}